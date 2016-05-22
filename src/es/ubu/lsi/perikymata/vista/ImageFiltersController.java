package es.ubu.lsi.perikymata.vista;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.annotation.processing.Processor;

import es.ubu.lsi.perikymata.MainApp;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.GaussianBlur;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
     * Handler that applies a convolveFilter to the original image and stores it as a filtered image.
     */
    @FXML
    public void applyConvolveFilter(){
    	new Thread(()->{
    		ImageProcessor proc = new ColorProcessor(SwingFXUtils.fromFXImage(originalImage.getImage(),null));
    		System.out.println("iniciando Filtrado");
    		proc.convolve3x3(new int[]{10,0,-10,15,0,-15,10,0,-10});	
        	mainApp.setFilteredImage(SwingFXUtils.toFXImage((BufferedImage) proc.createImage(), null));
        	filteredImage.setImage(mainApp.getFilteredImage());
    		/*
    		ConvolveOp colv = new ConvolveOp(new Kernel(3, 3,new float[]{10,0,-10,15,0,-15,10,0,-10}));
        	BufferedImage temp = colv.filter(SwingFXUtils.fromFXImage(filteredImage.getImage(),null), null);
        	mainApp.setFilteredImage(SwingFXUtils.toFXImage(temp, null));
        	filteredImage.setImage(mainApp.getFilteredImage());
        	*/
    	}).start();
    }
    /**
     * Handler that applies a convolveFilter to the original image and stores it as a filtered image.
     */
    @FXML
    public void applyGaussFilter(){
    	new Thread(()->{
    		GaussianBlur gauss = new GaussianBlur();
    		ImageProcessor proc = new ColorProcessor(SwingFXUtils.fromFXImage(filteredImage.getImage(),null));
    		System.out.println("iniciando Filtrado");
    		gauss.blurGaussian(proc, 2);	
        	mainApp.setFilteredImage(SwingFXUtils.toFXImage((BufferedImage) proc.createImage(), null));
        	filteredImage.setImage(mainApp.getFilteredImage());
    	}).start();
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
        	filteredImage.setImage(mainApp.getFilteredImage());
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
