package es.ubu.lsi.perikymata.modelo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.util.FiltersXmlAdapter;

@XmlRootElement
public class Project {
	String projectName;
	List<Filter> filterList;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@XmlElement(name = "filter")
	@XmlJavaTypeAdapter(FiltersXmlAdapter.class)
	public List<Filter> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
	}
}
