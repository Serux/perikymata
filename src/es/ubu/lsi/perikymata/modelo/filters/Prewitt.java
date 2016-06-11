package es.ubu.lsi.perikymata.modelo.filters;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prewitt implements Filter {

	public static final String filterName = "Prewitt";
	private int size;
	private double force;
	
	public Prewitt(int size, double force) {
		
		this.size = size;
		this.force = force;
		

		
	}
	
	private float[] calculatePrewittMatrix(){
		int value = size/2;
		float[] matrix = new float[size*size];
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				matrix[j*size+i]=(float) (value*force);
			}
			value--;
		}
			return matrix;
		
	}
	@Override
	public BufferedImage run(BufferedImage image) {
		ImageProcessor proc = new ColorProcessor(image);
		//kernel,width,height
		proc.convolve(calculatePrewittMatrix(), size, size);
		return (BufferedImage) proc.createImage();
	}
	@Override
	public StringProperty getFiltername() {
		return new SimpleStringProperty(filterName);
	}

	@Override
	public StringProperty getFilterArgs() {
		return new SimpleStringProperty("Size: " + size + "; Force: " + new DecimalFormat("#.##").format(force));
	}

	@Override
	public String getSmallStringRepresentation() {
		return filterName + ":" + size + "," + force;
	}

}
