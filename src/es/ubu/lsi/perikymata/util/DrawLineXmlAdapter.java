package es.ubu.lsi.perikymata.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.ubu.lsi.perikymata.modelo.Project;
import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.modelo.filters.Gauss;
import es.ubu.lsi.perikymata.modelo.filters.Prewitt;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
/**
 * Class used to pass a pathElement from XML to an object and vice versa.
 * @see Project#getFilterList()
 * @author Sergio Chico Carrancio
 */
public class DrawLineXmlAdapter extends XmlAdapter<String, PathElement> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PathElement unmarshal(String v) throws Exception {
		String[] pathElementString = v.split(":");
		String[] parameters = pathElementString[1].split(",");
		PathElement pathElement = null;
		if(MoveTo.class.getName().equals(pathElementString[0])){
			pathElement = new MoveTo(Double.parseDouble(parameters[0]),Double.parseDouble(parameters[1]));
		} else{
			pathElement = new LineTo(Double.parseDouble(parameters[0]),Double.parseDouble(parameters[1]));
		}
		return pathElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(PathElement v) throws Exception {
		if(v instanceof MoveTo){
			MoveTo value = (MoveTo) v;
			return value.getClass().getName() + ":" +value.getX()+","+value.getY();
		}
		else{
			LineTo value = (LineTo) v;
			return value.getClass().getName() + ":" +value.getX()+","+value.getY();
		}
	}
}