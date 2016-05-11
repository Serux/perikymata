package es.ubu.lsi.perikymata.vista;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.Proyecto;
import es.ubu.lsi.perikymata.modelo.URIListWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;


/**
 * Controlador para el layour principal. Contiene un layout principal
 * con una barra de menú común a toda la aplicación y espacio para
 * introducir otros elementos de JavaFX.
 * 
 * @author Sergio Chico Carrancio
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    @FXML
    private void initialize() {
    	
    }
    
    /**
     * Método llamado por la aplicación principal para 
     * obtener una referencia a si misma para acceder a su funcionalidad.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    /**
     * Método que llama al controlador principal para cambiar a la
     * ventana de selección de imágenes.
     */
    @FXML
    private void windowImageSelection() {
        mainApp.showImageSelection();
    }
    
    /**
     * Método que llama al controlador principal para cambiar a la
     * ventana de aplicar filtros.
     */
    @FXML
    private void windowImageFilters() {
        mainApp.showImageFilters();
    }
    
    /**
     * Método que llama al controlador principal para cambiar a la
     * ventana de contar perikymata.
     */
    @FXML
    private void windowPerikymataCount() {
        mainApp.showPerikymataCount();
    }

    /**
     * Crea un proyecto vacío, junto a su estructura de carpetas.
     */
    @FXML
    private void handleNew() {
    	   FileChooser fileChooser = new FileChooser();

    	   
           // Extensión vacía para que sólo aparezcan carpetas.
           FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                   "Carpeta proyecto", "*");
           fileChooser.getExtensionFilters().add(extFilter);
           fileChooser.initialFileNameProperty().set("Nombre_Proyecto");

           // Muestra el diálogo de selección.
           File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

           if (file != null) {
               // Crea los directoriosdel proyecto.
        	   mainApp.setProyecto(new Proyecto());
        	   mainApp.getProyecto().setProjectName(file.getName());
        	   
        	   file.mkdir();
        	   new File(file.toString() + "\\Fragmentos").mkdir();
        	   new File(file.toString() + "\\Imagen_Original").mkdir();
        	   new File(file.toString() + "\\perikymata").mkdir();
        	   try {
   	            JAXBContext context = JAXBContext.newInstance(Proyecto.class);
   	            Marshaller m = context.createMarshaller();
   	            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
   	            // Marshalling and saving XML to the file.
   	            File projectXMLfile = new File(file.toString() + "\\" + file.getName() + ".xml");
   	            m.marshal(mainApp.getProyecto(), projectXMLfile );
   	           
   	            // Save the file path to the registry.
   	            mainApp.setProjectFilePath(projectXMLfile);
   	        } catch (Exception e) { // catches ANY exception
   	        	//TODO crear método en el MainApp para mostrar los errores.
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
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        // Filtro para el xml del proyecto
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Archivos XML (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Muestra el diálogo para abrir el fichero
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadProjectFromFile(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
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
            //mainApp.savePersonDataToFile(file);
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