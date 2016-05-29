package es.ubu.lsi.perikymata;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.Project;
import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.vista.ImageFiltersController;
import es.ubu.lsi.perikymata.vista.ImageSelectionController;
import es.ubu.lsi.perikymata.vista.PerikymataCountController;
import es.ubu.lsi.perikymata.vista.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Controller for the main application, contains the data that needs to be accessed 
 * by any of the other windows and has common operations, like navigation between
 * windows or data access.
 * 
 * @author Sergio Chico Carrancio
 */
public class MainApp extends Application  {

	 /**
	 * Main container of the application's layout.
	 */
	 private Stage primaryStage;
	 /**
	  * Main layout of the application.
	  */
	 private BorderPane rootLayout;
	 /**
	  * Full image of a tooth, used to count perikyma.
	  */
	 private Image fullImage;
	 /**
	  * Full image of a tooth with some applied filters
	  */
	 private Image filteredImage;
	 
	 /**
	  * List of applied filters.
	  */
	 private ObservableList<Filter> appliedFilters = FXCollections.observableArrayList();
	 
	 /**
	 * @return the appliedFilters
	 */
	public ObservableList<Filter> getAppliedFilters() {
		return appliedFilters;
	}

	/**
	  * Data of a perikymata project.
	  */
	 private Project project;
	
	 
	   /**
	     * Returns the main stage.
	     * @return
	     */
	    public Stage getPrimaryStage() {
	        return primaryStage;
	        
	    }

	    /**
	     * Launches the applications, no args needed.
	     * @param args
	     */
	    public static void main(String[] args) {
	        launch(args);
	    }

		@Override
		public void start(Stage primaryStage) {
		        this.primaryStage = primaryStage;
		        this.primaryStage.setTitle("Perikymata - Unsaved Project");
		        this.primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));
		        this.primaryStage.setMinHeight(450.0);
		        this.primaryStage.setMinWidth(650.0);
		        initRootLayout();
		        showImageSelection();

		}
		
		/**
	     * Loads and shows the RootLayout and tries to load the last opened file.
	     */
	    public void initRootLayout() {
	        try {
	        	// Loads the FXML view.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class
	                    .getResource("vista/RootLayout.fxml"));
	            rootLayout = (BorderPane) loader.load();

	            // Puts the rootlayout(menubar) into the scene.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);

	            // Gives a mainapp's reference to the controller.
	            RootLayoutController controller = loader.getController();
	            controller.setMainApp(this);

	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        // Tries to load the last opened file.
	        File file = getProjectFilePath();
	        if (file != null) {
	        	// if the file exists, it is loaded. If it doesn't the reference is erased.
	            if (file.exists())
	            	loadProjectFromFile(file);
	            else
	            	setProjectFilePath(null);
	        } 
	        
	    }
		
		/**
		 * Loads a Project XML file into the application.
		 * @param file XML project.
		 */
		public void loadProjectFromFile(File file) {
	        try {
	            JAXBContext context = JAXBContext.newInstance(Project.class);
	            Unmarshaller um = context.createUnmarshaller();

	            // reads the XML and saves its data into a Project class.
	            project = (Project) um.unmarshal(file);
	            this.primaryStage.setTitle("Perekimata - " + project.getProjectName());
	            
	            // Saves the path of the opened file.
	            setProjectFilePath(file);

	        } catch (Exception e) { // catches ANY exception
	        	//TODO put this into a method with arguments.
	        	Alert alert = new Alert(Alert.AlertType.ERROR);
	        	alert.setTitle("Error");
	        	alert.setHeaderText("Could not load data from file:\n" + file.getPath());
	        	
	        	Label label = new Label("The exception stacktrace was:");
	        	
	        	StringWriter sw = new StringWriter();
	        	PrintWriter pw = new PrintWriter(sw);
	        	e.printStackTrace(pw);
	        	
	        	TextArea textArea = new TextArea(sw.toString());
	        	textArea.setEditable(false);
	        	textArea.setWrapText(true);

	        	textArea.setMaxWidth(Double.MAX_VALUE);
	        	textArea.setMaxHeight(Double.MAX_VALUE);
	        	GridPane.setVgrow(textArea, Priority.ALWAYS);
	        	GridPane.setHgrow(textArea, Priority.ALWAYS);

	        	GridPane expContent = new GridPane();
	        	expContent.setMaxWidth(Double.MAX_VALUE);
	        	expContent.add(label, 0, 0);
	        	expContent.add(textArea, 0, 1);
	        	alert.getDialogPane().setExpandableContent(expContent);
	        	alert.showAndWait();
	        	
	        }
	    }
	    
		
	    
	    /**
	     * Saves the preference of the last opened project file.
	     * 
	     * @param file Project file to store into preferences or null to remove the preference. 
	     */
	    public void setProjectFilePath(File file) {
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        if (file != null) {
	            prefs.put("filePath", file.getPath());
	            // Updates the window's Title.
	            primaryStage.setTitle("Perikymata - " + file.getName());
	        } else {
	            prefs.remove("filePath");
	            // Updates the window's Title.
	            primaryStage.setTitle("Perikymata - Unsaved Project");
	        }
	    }
	    
	    /**
	     * Loads the preference of the last opened project file.
	     * 
	     * @return null if project found in the preferences, File of the last opened project otherwise.
	     */
	    public File getProjectFilePath() {
	    	//Looks into the preferences of this application for the preference of the last opened project.
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        String filePath = prefs.get("filePath", null);
	        if (filePath != null) {
	            return new File(filePath);
	        } else {
	            return null;
	        }
	    }
	    
	    /**
	     * Shows the Image Selection Window.
	     */
	    public void showImageSelection() {
	        try {
	        	// Loads the FXML view.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/ImageSelection.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();

	            // Shows this layout in the center of the rootLayout.
	            rootLayout.setCenter(personOverview);

	            // Gives a mainapp's reference to the controller of the layout.
	            ImageSelectionController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Shows the Filter Application Window.
	     */
	    public void showImageFilters() {
	        try {
	        	// Loads the FXML view.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/ImageFilters.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	          
	            // Shows this layout in the center of the rootLayout.
	            rootLayout.setCenter(personOverview);
	            
	            // Gives a mainapp's reference to the controller of the layout.
	            ImageFiltersController controller = loader.getController();
	            controller.setMainApp(this);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Shows the Perikymata counting window.
	     */
	    public void showPerikymataCount() {
	        try {
	        	// Loads the FXML view.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/PerikymataCount.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	            
	            // Shows this layout in the center of the rootLayout.
	            rootLayout.setCenter(personOverview);

	        	// Gives a mainapp's reference to the controller of the layout.
	            PerikymataCountController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Gets the full image of the tooth.
	     * @return Image of the tooth.
	     */
		public Image getFullImage() {
			return fullImage;
		}

		/**
		 * Sets the full image of the tooth.
		 * @param fullImage Full image of the tooth.
		 */
		public void setFullImage(Image fullImage) {
			this.fullImage = fullImage;
		}
		
	    /**
	     * Gets the full image of the tooth.
	     * @return Image of the tooth.
	     */
		public Image getFilteredImage() {
			return filteredImage;
		}

		/**
		 * Sets the full image of the tooth.
		 * @param fullImage Full image of the tooth.
		 */
		public void setFilteredImage(Image filteredImage) {
			this.filteredImage = filteredImage;
		}

		/**
		 * Gets the project data.
		 * @return Project with the project data.
		 */
		public Project getProject() {
			return project;
		}

		/**
		 * Sets the project data.
		 * @param project Project with the data of a perikymata project.
		 */
		public void setProject(Project project) {
			this.project = project;
		}
}
