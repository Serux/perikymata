package es.ubu.lsi.perikymata.vista;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;


/**
 * Controller for the rootLayout. The BorderLayout contains a common menu bar
 * for the entire application in the top border and a free space at the center
 * to put in other layouts. This controller grants access to the FXML
 * components and has the handlers for this window actions.
 * 
 * @author Sergio Chico Carrancio
 */
public class RootLayoutController {

    /**
     *  Reference to the main application
     */
    private MainApp mainApp;

    
    /**
     * Method called by the main application to set a reference to itself.
     * This is done to be able to call mainapp's methods.
     * 
     * @param mainApp Reference to the main controller.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    /**
     * Handler that calls the main controller and changes the content of the 
     * graphic interface to the Image Selection stage.
     */
    @FXML
    private void windowImageSelection() {
        mainApp.showImageSelection();
    }
    
    /**
     * Handler that calls the main controller and changes the content of the 
     * graphic interface to the Image filters application stage.
     */
    @FXML
    private void windowImageFilters() {
        mainApp.showImageFilters();
    }
    
    /**
     * Handler that calls the main controller and changes the content of the 
     * graphic interface to the perikymata counting stage.
     */
    @FXML
    private void windowPerikymataCount() {
        mainApp.showPerikymataCount();
    }

    /**
     * Handler that creates a new project (folder structure and project xml).
     */
    @FXML
    private void handleNew() {
    	   mainApp.createNewProject();
     }

 
    /**
     * Opens a FileChooser to let the user select a perikymata project file (xml) to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        // adds a XML project filter.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Perikymata XML file (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // shows the open project dialog.
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadProjectFromFile(file);
            mainApp.setProjectPath(file.getParent());
        }
    }

    /**
     * Saves the file to the project file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
    	//TODO
        /*File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }*/
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            //TODO
            //mainApp.saveProjectDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Perikymata");
    	alert.setHeaderText("About");
    	alert.setContentText("Autor: Sergio Chico Carrancio \nUniversidad de Burgos");
        alert.showAndWait();
    }
    

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}