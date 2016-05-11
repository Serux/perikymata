package es.ubu.lsi.perikymata.modelo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Project {
	String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		
		this.projectName = projectName;
	}
}
