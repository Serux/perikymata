package es.ubu.lsi.perikymata.vista;

import es.ubu.lsi.perikymata.MainApp;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * Controller for the layout that is used to count perikyma.
 * @author Sergio Chico Carrancio
 *
 */
public class PerikymataCountController {
	// Reference to the main application
    private MainApp mainApp;
	@FXML
	private ImageView fullImage;
	 /**
     * Is called by the main application to give a reference back to itself.
     * Also, sets the full Image. This is done here because when the method
     * initialize is called, there is no reference to the mainapp.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if(mainApp.getFullImage()!= null){
        	fullImage.setImage(mainApp.getFullImage());
        }
    }
}
