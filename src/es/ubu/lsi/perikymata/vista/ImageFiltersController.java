package es.ubu.lsi.perikymata.vista;

import es.ubu.lsi.perikymata.MainApp;

public class ImageFiltersController {

	 // Reference to the main application
    private MainApp mainApp;
	
	   /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
