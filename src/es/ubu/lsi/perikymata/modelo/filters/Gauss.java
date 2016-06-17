package es.ubu.lsi.perikymata.modelo.filters;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import ij.plugin.filter.GaussianBlur;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Gauss implements Filter{

	/**
	 * Strength of the filter.
	 */
	private double sigmaValue;
	
	/**
	 * Name of the filter.
	 */
	public static final String FILTERNAME = "Gaussian";
	
	/**
	 * Constructor of the filter, adds the arguments to the object.
	 * @param sigma Strenght of the filter.
	 */
	public Gauss( double sigma ){
		sigmaValue =  sigma;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BufferedImage run(BufferedImage image) {
		GaussianBlur gauss = new GaussianBlur();
		ImageProcessor proc = new ColorProcessor(image);
		gauss.blurGaussian(proc, sigmaValue);
		return (BufferedImage) proc.createImage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringProperty getFiltername() {
		return new SimpleStringProperty(FILTERNAME);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringProperty getFilterArgs() {
		return new SimpleStringProperty("Sigma: " + new DecimalFormat("#.##").format(sigmaValue));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSmallStringRepresentation() {
		return FILTERNAME + ":" + sigmaValue ;
	}
}

