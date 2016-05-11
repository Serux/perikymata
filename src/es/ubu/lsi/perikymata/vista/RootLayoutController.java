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
    	   FileChooser fileChooser = new FileChooser();

    	   
           // Adds a filter that shows all the files..
           FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                   "Project Folder", "*");
           fileChooser.getExtensionFilters().add(extFilter);
           fileChooser.initialFileNameProperty().set("Project_Name");

           // Shows the save dialog.
           File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

           if (file != null) {
               // Saves the project name.
        	   mainApp.setProject(new Project());
        	   mainApp.getProject().setProjectName(file.getName());
        	   
        	   // Makes the folder structure.
        	   file.mkdir();
        	   new File(file.toString() + "\\Fragmentos").mkdir();
        	   new File(file.toString() + "\\Imagen_Original").mkdir();
        	   new File(file.toString() + "\\perikymata").mkdir();
        	   //Creates the XML project file.
        	   try {
   	            JAXBContext context = JAXBContext.newInstance(Project.class);
   	            Marshaller m = context.createMarshaller();
   	            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
   	            // Marshalling and saving XML to the file.
   	            File projectXMLfile = new File(file.toString() + "\\" + file.getName() + ".xml");
   	            m.marshal(mainApp.getProject(), projectXMLfile );
   	           
   	            // Save the file path to the registry.
   	            mainApp.setProjectFilePath(projectXMLfile);
   	        } catch (Exception e) { // catches ANY exception
   	        	//TODO take this to a method in MainApp
   	        	Alert alert = new Alert(Alert.AlertType.ERROR);
   	        	alert.setTitle("Error");
   	        	alert.setHeaderText("No se puede guardar archivo :\n" + file.getPath());
   	        	
   	        	Label label = new Label("La traza de la excepción fue:");
   	        	
   	        	StringWriter sw = new StringWriter();
   	        	PrintWriter pw = new PrintWriter(sw);
   	        	e.printStackTrace(pw);
   	        	
   	        	TextArea textArea = new TextArea(sw.toString());
   	        	textArea.setEditable(false);
   	        	textArea.setWrapText(true);

   	        	textArea.setMaxWidth(Double.MAX_VALUE);
   	        	textArea.setMaxHeight(Double.MAX_VALUE);
   	        	GridPane.setVgrow(textArea, Priority.ALWAYS);
   	        	GridPane.setHgrow(textArea, Priority.ALWAYS);

   	        	GridPane expContent = new GridPane();
   	        	expContent.setMaxWidth(Double.MAX_VALUE);
   	        	expContent.add(label, 0, 0);
   	        	expContent.add(textArea, 0, 1);
   	        	alert.getDialogPane().setExpandableContent(expContent);
   	        	alert.showAndWait();
   	        }
   	    }
     }

 
    /**
     * Opens a FileChooser to let the user select a perikymata project file (xml) to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        // adds a XML project filter.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Perikymata XML ile (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // shows the open project dialog.
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadProjectFromFile(file);
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