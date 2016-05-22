package es.ubu.lsi.perikymata.vista;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

import javax.annotation.processing.Processor;

import es.ubu.lsi.perikymata.MainApp;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.GaussianBlur;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
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
	 * Slider for controlling the sigma of the gauss filter.
	 */
	@FXML
	private Slider gaussSlider;
	
	/**
	 * Label to show the exact size of sigma on a gaussian filter.
	 */
	@FXML
	private Label gaussLevel;
    
	/**
	 * Initializes the components when the controller is loaded.
	 */
    @FXML
    private void initialize() {
        // binds scroll properties together to make both scrolls move at the same time.
        scrollPane1.vvalueProperty().bindBidirectional(scrollPane2.vvalueProperty());
        scrollPane1.hvalueProperty().bindBidirectional(scrollPane2.hvalueProperty());

       //Handler that changes a label to show the value of the gauss slider.
        gaussLevel.textProperty().bindBidirectional(gaussSlider.valueProperty(), new DecimalFormat("#.##"));
        
    }
    
    /**
     * Handler that applies a convolveFilter to the original image and stores it as a filtered image.
     */
    @FXML
    public void applyConvolveFilter(){
    	new Thread(()->{
    		ImageProcessor proc = new ColorProcessor(SwingFXUtils.fromFXImage(originalImage.getImage(),null));
    		proc.convolve3x3(new int[]{10,0,-10,15,0,-15,10,0,-10});	
        	mainApp.setFilteredImage(SwingFXUtils.toFXImage((BufferedImage) proc.createImage(), null));
        	filteredImage.setImage(mainApp.getFilteredImage());
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
    		gauss.blurGaussian(proc, gaussSlider.getValue());	
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
