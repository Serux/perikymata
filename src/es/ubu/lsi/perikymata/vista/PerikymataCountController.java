package es.ubu.lsi.perikymata.vista;

import es.ubu.lsi.perikymata.MainApp;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PerikymataCountController {
	// Reference to the main application
    private MainApp mainApp;
	@FXML
	private ImageView imagenCompleta;
	   /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if(mainApp.getImagenCompleta()!= null){
        	imagenCompleta.setImage(mainApp.getImagenCompleta());
        }
    }
}
