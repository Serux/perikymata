package es.ubu.lsi.perikymata.vista;

import es.ubu.lsi.perikymata.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Button zoomMinusButton;
    
    
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
        	fullImage.setImage(mainApp.getFilteredImage());
        	fullImage.setFitHeight(fullImage.getImage().getHeight());
        	fullImage.setPreserveRatio(true);

        }
    }
 
    /**
     * Handles the zooming in.
     */
    @FXML
    public void zoomPlus(){
    	System.out.println(fullImage.getFitHeight());
    	fullImage.setFitHeight(fullImage.getFitHeight()*1.25);
    	if(fullImage.getFitHeight()>=5){
    		zoomMinusButton.setDisable(false);
    	}
    }
    
    /**
     * Handles the zooming out.
     */
    @FXML
    public void zoomMinus(){
    	
    	fullImage.setFitHeight(fullImage.getFitHeight()*0.75);
    	if(fullImage.getFitHeight()<=5){
    		zoomMinusButton.setDisable(true);
    	}
    }
}
