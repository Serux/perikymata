package es.ubu.lsi.perikymata.vista;

import es.ubu.lsi.perikymata.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
/**
 * Controller for the layout that filters images.
 * 
 * @author Sergio Chico Carrancio
 */
public class ImageFiltersController {

	/**
	 * Reference to the main application
	 */
    private MainApp mainApp;
    
    /**
     * Reference to the first Scrollpane.
     */
    @FXML
    private ScrollPane scrollPane1;
    /**
     * Reference to the second Scrollpane.
     */
    @FXML
    private ScrollPane scrollPane2;
    
    /**
     * Reference to the filtered image.
     */
	@FXML
	private ImageView filteredImage;
    /**
     * Reference to the original image.
     */
	@FXML
	private ImageView originalImage;
    
	/**
	 * Initializes the components when the controller is loaded.
	 */
    @FXML
    private void initialize() {
    	// Creates listeners to make the scrolls of both scrollviews move at the same time.
    	ChangeListener<Object> changeListenerVerticalScroll1 = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				scrollPane2.setVvalue(scrollPane1.getVvalue());
			}
        };
    	ChangeListener<Object> changeListenerVerticalScroll2 = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				scrollPane1.setVvalue(scrollPane2.getVvalue());
			}
        };
        ChangeListener<Object> changeListenerHorizontalScroll1 = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				scrollPane2.setHvalue(scrollPane1.getHvalue());
			}
        };
    	ChangeListener<Object> changeListenerHorizontalScroll2 = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				scrollPane1.setHvalue(scrollPane2.getHvalue());
			}
        };
        
        // Adds the listeners to make both scrolls move at the same time.
        scrollPane1.vvalueProperty().addListener(changeListenerVerticalScroll1);
        scrollPane2.vvalueProperty().addListener(changeListenerVerticalScroll2);
        scrollPane1.hvalueProperty().addListener(changeListenerHorizontalScroll1);
        scrollPane2.hvalueProperty().addListener(changeListenerHorizontalScroll2);
        
        
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
        	originalImage.setImage(mainApp.getFullImage());
        	filteredImage.setImage(mainApp.getFullImage());
        }
    }
    
    /**
     * Handler that changes to the perikymata counting stage when called.
     */
    @FXML
    public void nextScreen(){
    	mainApp.showPerikymataCount();
    }
}
