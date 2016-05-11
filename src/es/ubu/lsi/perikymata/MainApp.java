package es.ubu.lsi.perikymata;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.Proyecto;
import es.ubu.lsi.perikymata.vista.ImageFiltersController;
import es.ubu.lsi.perikymata.vista.ImageSelectionController;
import es.ubu.lsi.perikymata.vista.PerikymataCountController;
import es.ubu.lsi.perikymata.vista.RootLayoutController;
import javafx.application.Application;
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
 * Controlador principal de la aplicación, que contiene los datos comunes 
 * y realiza las operaciones comunes, como cambiar de ventana o acceso a datos.
 * 
 * @author Sergio Chico
 */
public class MainApp extends Application  {

	 private Stage primaryStage;
	 private BorderPane rootLayout;
	 private Image imagenCompleta;
	 private Proyecto proyecto;
	 
	   /**
	     * Returns the main stage.
	     * @return
	     */
	    public Stage getPrimaryStage() {
	        return primaryStage;
	        
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

		@Override
		public void start(Stage primaryStage) {
				
		        this.primaryStage = primaryStage;
		        this.primaryStage.setTitle("Perekimata - Proyecto sin guardar");
		        this.primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));
		        this.primaryStage.setMinHeight(450.0);
		        this.primaryStage.setMinWidth(650.0);
		        initRootLayout();
		        showImageSelection();

		}
		
		/**
	     * Initializes the root layout and tries to load the last opened
	     * person file.
	     */
	    public void initRootLayout() {
	        try {
	            // Load root layout from fxml file.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class
	                    .getResource("vista/RootLayout.fxml"));
	            rootLayout = (BorderPane) loader.load();

	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);

	            // Give the controller access to the main app.
	            RootLayoutController controller = loader.getController();
	            controller.setMainApp(this);

	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        // Try to load last opened person file.
	        File file = getProjectFilePath();
	        if (file != null) {
	            loadProjectFromFile(file);
	        }
	        
	    }
		
		/**
		 * Carga un proyecto en la aplicación
		 * @param file
		 */
		public void loadProjectFromFile(File file) {
	        try {
	            JAXBContext context = JAXBContext.newInstance(Proyecto.class);
	            Unmarshaller um = context.createUnmarshaller();

	            // Reading XML from the file and unmarshalling.
	            proyecto = (Proyecto) um.unmarshal(file);
	            this.primaryStage.setTitle("Perekimata - " + proyecto.getProjectName());
	            
	            // Save the file path to the registry.
	            setProjectFilePath(file);

	        } catch (Exception e) { // catches ANY exception
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
	     * Sets the file path of the currently loaded file. The path is persisted in
	     * the OS specific registry.
	     * 
	     * @param file the file or null to remove the path
	     */
	    public void setProjectFilePath(File file) {
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        if (file != null) {
	            prefs.put("filePath", file.getPath());
	            // Update the stage title.
	            primaryStage.setTitle("AddressApp - " + file.getName());
	        } else {
	            prefs.remove("filePath");
	            // Update the stage title.
	            primaryStage.setTitle("AddressApp");
	        }
	    }
	    
	    /**
	     * Returns the person file preference, i.e. the file that was last opened.
	     * The preference is read from the OS specific registry. If no such
	     * preference can be found, null is returned.
	     * 
	     * @return
	     */
	    public File getProjectFilePath() {
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        String filePath = prefs.get("filePath", null);
	        if (filePath != null) {
	            return new File(filePath);
	        } else {
	            return null;
	        }
	    }
	    
	    /**
	     * Shows the person overview inside the root layout.
	     */
	    public void showImageSelection() {
	        try {
	            // Load person overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/ImageSelection.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();

	            // Set person overview into the center of root layout.
	            rootLayout.setCenter(personOverview);

	            // Give the controller access to the main app.
	            ImageSelectionController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Shows the person overview inside the root layout.
	     */
	    public void showImageFilters() {
	        try {
	            // Load person overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/ImageFilters.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	          
	            // Set person overview into the center of root layout.
	            rootLayout.setCenter(personOverview);
	            
	            // Give the controller access to the main app.
	            ImageFiltersController controller = loader.getController();
	            controller.setMainApp(this);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Shows the person overview inside the root layout.
	     */
	    public void showPerikymataCount() {
	        try {
	            // Load person overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/PerikymataCount.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	            // Set person overview into the center of root layout

	            rootLayout.setCenter(personOverview);

	            // Give the controller access to the main app.
	            PerikymataCountController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

		public Image getImagenCompleta() {
			return imagenCompleta;
		}

		public void setImagenCompleta(Image imagenCompleta) {
			this.imagenCompleta = imagenCompleta;
		}

		public Proyecto getProyecto() {
			return proyecto;
		}

		public void setProyecto(Proyecto proyecto) {
			this.proyecto = proyecto;
		}
}
