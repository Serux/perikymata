package es.ubu.lsi.perikymata.vista;

import java.io.File;
import es.ubu.lsi.perikymata.MainApp;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class ImageSelectionController {
	
	@FXML
	private ImageView previewImage;
	// Reference to the main application.
    private MainApp mainApp;
	
    public ImageSelectionController() {
	}
    
	/**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "All files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        
        if (file != null) {
        	mainApp.setImagenCompleta(new Image(file.toURI().toString()));
        	previewImage.setImage(mainApp.getImagenCompleta());
        }
    }
    
    @FXML
    private void nextScreen(){
    	mainApp.showImageFilters();
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
