package es.ubu.lsi.perikymata.vista;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import es.ubu.lsi.perikymata.MainApp;
import ij.io.Opener;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class ImageSelectionController {
	
	/**
	 * preview of the full tooth Image.
	 */
	@FXML
	private ImageView previewImage;
	/**
	 *  Reference to the main application.
	 */
    private MainApp mainApp;
	  
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	previewImage.fitHeightProperty().bind(((Pane)previewImage.getParent()).heightProperty());
    	previewImage.fitWidthProperty().bind(((Pane)previewImage.getParent()).widthProperty());
    }
    
	/**
     * Opens a FileChooser to let the user select Image to load.
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
        	
        	java.awt.Image full = new Opener().openImage(file.getParent(),file.getName()).getImage();
        		
			mainApp.setFullImage( SwingFXUtils.toFXImage((BufferedImage) full,null));
			previewImage.setImage(mainApp.getFullImage());
        	mainApp.setFilteredImage(mainApp.getFullImage());
        	
    
        }
    }
    
    /**
     * Handler that rotates image.
     */
    @FXML
    private void handleRotate(){
    	BufferedImage im = SwingFXUtils.fromFXImage(previewImage.getImage(),null);
    	AffineTransform a = new AffineTransform();
    	a.translate(0.5*im.getHeight(), 0.5*im.getWidth());
    	a.rotate(Math.PI/2);
    	a.translate(-0.5*im.getWidth(), -0.5*im.getHeight());
    	AffineTransformOp op = new AffineTransformOp(a,AffineTransformOp.TYPE_BILINEAR);
    	Image i = SwingFXUtils.toFXImage(op.filter(im, null), null);
    	previewImage.setImage(i);
    	mainApp.setFullImage(i);
    	mainApp.setFilteredImage(i);
    	
    }
       
    
    /**
     * Handler that changes to the image filter stage when called.
     */
    @FXML
    private void nextScreen(){
    	mainApp.showImageFilters();
    }
        
    /**
     * Is called by the main application to give a reference back to itself.
     * Also, sets the Images. This is done here because when the method
     * initialize is called, there is no reference to the mainapp.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if(mainApp.getFullImage() != null){
        	previewImage.setImage(mainApp.getFullImage());
        }
    }
    

}
