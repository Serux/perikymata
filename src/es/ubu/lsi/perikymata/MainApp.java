package es.ubu.lsi.perikymata;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.URIListWrapper;
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

public class MainApp extends Application  {
	 private Stage primaryStage;
	 private BorderPane rootLayout;
	 private Image imagenCompleta;
	 private List<URI> imagenesURI;
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
		        this.primaryStage.setTitle("AddressApp");
		        
		        this.primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));
		        this.primaryStage.setMinHeight(450.0);
		        this.primaryStage.setMinWidth(650.0);
		        initRootLayout();

		        showImageSelection();

		}
		
		 /**
	     * Saves the current person data to the specified file.
	     * 
	     * @param file
	     */
	    public void saveURIsToFile(File file) {
	        try {
	            JAXBContext context = JAXBContext
	                    .newInstance(URIListWrapper.class);
	            Marshaller m = context.createMarshaller();
	            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	            // Wrapping our person data.
	            URIListWrapper wrapper = new URIListWrapper();
	            wrapper.setPersons(imagenesURI);

	            // Marshalling and saving XML to the file.
	            m.marshal(wrapper, file);
	           
	            // Save the file path to the registry.
	            //TODO implementar setFilePath.
	            //setPersonFilePath(file);
	        } catch (Exception e) { // catches ANY exception
	        	Alert alert = new Alert(Alert.AlertType.ERROR);
	        	alert.setTitle("Error");
	        	alert.setHeaderText("No se puede guardar archivo :\n" + file.getPath());
	        	
	        	Label label = new Label("La traza de la excepción fue:");
	        	
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
}
