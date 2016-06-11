package es.ubu.lsi.perikymata.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.modelo.filters.Gauss;
import es.ubu.lsi.perikymata.modelo.filters.Prewitt;

public class FiltersXmlAdapter extends XmlAdapter<String, Filter> {

	@Override
	public Filter unmarshal(String v) throws Exception {
		String[] filterString = v.split(":");
		Filter filter = null;
		if(filterString[0].equals(Gauss.filterName)){
			filter = new Gauss(Double.parseDouble(filterString[1]));
		} else if (filterString[0].equals(Prewitt.filterName)){
			String[] parameters = filterString[1].split(",");
			filter = new Prewitt(Integer.parseInt(parameters[0]),Double.parseDouble(parameters[1]));
		}
		return filter;
	}

	@Override
	public String marshal(Filter v) throws Exception {
		return v.getSmallStringRepresentation();
	}


}