package es.ubu.lsi.perikymata.modelo;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 * 
 * @author Sergio Chico
 */
@XmlRootElement(name = "URIs")
public class URIListWrapper {

    private List<URI> uris;

    @XmlElement(name = "uri")
    public List<URI> getPersons() {
        return uris;
    }

    public void setPersons(List<URI> uris) {
        this.uris = uris;
    }
}