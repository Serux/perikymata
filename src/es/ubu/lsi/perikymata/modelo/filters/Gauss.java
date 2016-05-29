package es.ubu.lsi.perikymata.modelo.filters;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import ij.plugin.filter.GaussianBlur;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Gauss implements Filter{

	private double sigmaValue;
	
	private static final String filterName = "Gaussian";
	
	public Gauss( double sigma ){
		
		sigmaValue =  sigma;
		
		
	}
	
	@Override
	public BufferedImage run(BufferedImage image) {
		GaussianBlur gauss = new GaussianBlur();
		ImageProcessor proc = new ColorProcessor(image);
		gauss.blurGaussian(proc, sigmaValue);
		return (BufferedImage) proc.createImage();
	}

	@Override
	public StringProperty getFiltername() {
		return new SimpleStringProperty(filterName);
	}

	@Override
	public StringProperty getFilterArgs() {
		return new SimpleStringProperty("Sigma: " + new DecimalFormat("#.##").format(sigmaValue));
	}
}

