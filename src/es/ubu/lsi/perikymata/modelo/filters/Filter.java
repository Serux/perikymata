package es.ubu.lsi.perikymata.modelo.filters;

import java.awt.image.BufferedImage;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.ubu.lsi.perikymata.util.FiltersXmlAdapter;
import javafx.beans.property.StringProperty;


public interface Filter {
	public StringProperty getFiltername();
	public StringProperty getFilterArgs();
	public BufferedImage run(BufferedImage image);
	public String getSmallStringRepresentation();
}
