package es.ubu.lsi.perikymata.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.ubu.lsi.perikymata.modelo.Project;
import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.modelo.filters.Gauss;
import es.ubu.lsi.perikymata.modelo.filters.Prewitt;
/**
 * Class used to pass a filter from XML to an object and vice versa.
 * @see Project#getFilterList()
 * @author Sergio Chico Carrancio
 */
public class FiltersXmlAdapter extends XmlAdapter<String, Filter> {

	/**
	 * @see Filter#getSmallStringRepresentation()
	 * {@inheritDoc}
	 */
	@Override
	public Filter unmarshal(String v) throws Exception {
		String[] filterString = v.split(":");
		Filter filter = null;
		if(filterString[0].equals(Gauss.FILTERNAME)){
			filter = new Gauss(Double.parseDouble(filterString[1]));
		} else if (filterString[0].equals(Prewitt.FILTERNAME)){
			String[] parameters = filterString[1].split(",");
			filter = new Prewitt(Integer.parseInt(parameters[0]),Double.parseDouble(parameters[1]));
		}
		return filter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(Filter v) throws Exception {
		return v.getSmallStringRepresentation();
	}


}