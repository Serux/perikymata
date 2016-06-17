package es.ubu.lsi.perikymata.modelo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.util.FiltersXmlAdapter;

/**
 * Project class, stores persistent data. 
 * Is used to read or create a XML file.
 * 
 * @author Sergio Chico Carrancio
 */
@XmlRootElement
public class Project {
	/**
	 * Name of the project.
	 */
	String projectName;
	/**
	 * List of applied filters.
	 */
	List<Filter> filterList;

	/**
	 * getter for project name, used to read from a XML.
	 * @return project name.
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Setter for project name.
	 * @param projectName name to set.
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Getter for the list of filters, used to read from a XML.
	 * If you want to add a new filter, see:
	 * @see FiltersXmlAdapter
	 * @return list of filters.
	 */
	@XmlElement(name = "filter")
	@XmlJavaTypeAdapter(FiltersXmlAdapter.class)
	public List<Filter> getFilterList() {
		return filterList;
	}

	/**
	 * Setter for the list of applied filters.
	 * 
	 * @param filterList applied filters.
	 */
	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
	}
}
