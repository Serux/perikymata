package es.ubu.lsi.perikymata.modelo.filters;

import java.awt.image.BufferedImage;

import javafx.beans.property.StringProperty;

public interface Filter {
	public StringProperty getFiltername();
	public StringProperty getFilterArgs();
	public BufferedImage run(BufferedImage image);
}
