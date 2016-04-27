package es.ubu.lsi.perikymata.vista;

import es.ubu.lsi.perikymata.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class ImageFiltersController {

	 // Reference to the main application
    private MainApp mainApp;
    
    @FXML
    private ScrollPane scroll1;
    @FXML
    private ScrollPane scroll2;
	
    @FXML
    private void initialize() {
    	ChangeListener<Object> changeListener1 = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				scroll1.setVvalue(scroll2.getVvalue());
			}
        };
        ChangeListener<Object> changeListener2 = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				scroll2.setVvalue(scroll1.getVvalue());
			}
        };
        scroll2.vvalueProperty().addListener(changeListener1);
        scroll1.vvalueProperty().addListener(changeListener2);
    }
    
	   /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    public void nextScreen(){
    	mainApp.showPerikymataCount();
    }
    
    public void VerticalScroll(){
    	scroll1.setVvalue(scroll2.getVvalue());
    }
}
