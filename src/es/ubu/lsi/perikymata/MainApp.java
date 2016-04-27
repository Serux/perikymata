package es.ubu.lsi.perikymata;

import java.io.File;
import java.io.IOException;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.vista.ImageFiltersController;
import es.ubu.lsi.perikymata.vista.ImageSelectionController;
import es.ubu.lsi.perikymata.vista.PerikymataCountController;
import es.ubu.lsi.perikymata.vista.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application  {
	 private Stage primaryStage;
	 private BorderPane rootLayout;
	 
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
		        this.primaryStage.setMinHeight(400.0);
		        this.primaryStage.setMinWidth(600.0);
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
}
