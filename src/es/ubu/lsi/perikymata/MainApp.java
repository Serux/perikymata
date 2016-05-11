package es.ubu.lsi.perikymata;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.Proyecto;
import es.ubu.lsi.perikymata.vista.ImageFiltersController;
import es.ubu.lsi.perikymata.vista.ImageSelectionController;
import es.ubu.lsi.perikymata.vista.PerikymataCountController;
import es.ubu.lsi.perikymata.vista.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Controlador principal de la aplicación, que contiene los datos comunes 
 * y realiza las operaciones comunes, como cambiar de ventana o acceso a datos.
 * 
 * @author Sergio Chico
 */
public class MainApp extends Application  {

	 private Stage primaryStage;
	 private BorderPane rootLayout;
	 private Image imagenCompleta;
	 private Proyecto proyecto;
	 
	   /**
	     * Devuelve la Stage principal.
	     * @return
	     */
	    public Stage getPrimaryStage() {
	        return primaryStage;
	        
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

		@Override
		public void start(Stage primaryStage) {
		        this.primaryStage = primaryStage;
		        this.primaryStage.setTitle("Perekimata - Proyecto sin guardar");
		        this.primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));
		        this.primaryStage.setMinHeight(450.0);
		        this.primaryStage.setMinWidth(650.0);
		        initRootLayout();
		        showImageSelection();

		}
		
		/**
	     * Inicializa el layout principal e intenta cargar el último fichero abierto.
	     */
	    public void initRootLayout() {
	        try {
	        	// Carga la clase FXML.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class
	                    .getResource("vista/RootLayout.fxml"));
	            rootLayout = (BorderPane) loader.load();

	            // Muestra añade a la ventana la barra de menú.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);

	            // Da al controlador acceso a la aplicación principal.
	            RootLayoutController controller = loader.getController();
	            controller.setMainApp(this);

	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        // Intenta cargar el último fichero abierto.
	        File file = getProjectFilePath();
	        if (file != null) {
	            loadProjectFromFile(file);
	        }
	        
	    }
		
		/**
		 * Carga los datos de un fichero de proyecto XML en la aplicación.
		 * @param file fichero XML del proyecto.
		 */
		public void loadProjectFromFile(File file) {
	        try {
	            JAXBContext context = JAXBContext.newInstance(Proyecto.class);
	            Unmarshaller um = context.createUnmarshaller();

	            // Lee el fichero XML y realiza el unmarshalling.
	            proyecto = (Proyecto) um.unmarshal(file);
	            this.primaryStage.setTitle("Perekimata - " + proyecto.getProjectName());
	            
	            // Guarda la ruta del fichero en las preferencias.
	            setProjectFilePath(file);

	        } catch (Exception e) { // catches ANY exception
	        	//TODO crear método general para mostrar los erroes.
	        	Alert alert = new Alert(Alert.AlertType.ERROR);
	        	alert.setTitle("Error");
	        	alert.setHeaderText("Could not load data from file:\n" + file.getPath());
	        	
	        	Label label = new Label("The exception stacktrace was:");
	        	
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
	    
		
	    
	    /**
	     * Guarda la dirección del archivo del proyecto en las preferencias del sistema.
	     * 
	     * @param file Archivo para guardar su ruta o null para eliminar la preferencia. 
	     */
	    public void setProjectFilePath(File file) {
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        if (file != null) {
	            prefs.put("filePath", file.getPath());
	            // Actualiza el título de la ventana.
	            primaryStage.setTitle("Perikymata - " + file.getName());
	        } else {
	            prefs.remove("filePath");
	            // Actualiza el título de la ventana.
	            primaryStage.setTitle("Perikymata - Proyecto sin guardar");
	        }
	    }
	    
	    /**
	     * Accede al último archivo abierto-guardado en las preferencias del sistema.
	     * 
	     * @return null si no existe proyecto, último proyecto abierto en caso contrario.
	     */
	    public File getProjectFilePath() {
	    	//Se busca la preferencia del proyecto actual que indica el último proyecto abierto.
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        String filePath = prefs.get("filePath", null);
	        if (filePath != null) {
	            return new File(filePath);
	        } else {
	            return null;
	        }
	    }
	    
	    /**
	     * Muestra la ventana de selección de imágenes.
	     */
	    public void showImageSelection() {
	        try {
	        	// Carga la clase FXML.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/ImageSelection.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();

	            // Muestra la ventana en el centro del layout.
	            rootLayout.setCenter(personOverview);

	            // Da al controlador acceso a la aplicación principal.
	            ImageSelectionController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Muestra la ventana para filtrar imágenes.
	     */
	    public void showImageFilters() {
	        try {
	        	// Carga la clase FXML.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/ImageFilters.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	          
	            // Muestra la ventana en el centro del layout.
	            rootLayout.setCenter(personOverview);
	            
	            // Da al controlador acceso a la aplicación principal.
	            ImageFiltersController controller = loader.getController();
	            controller.setMainApp(this);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Muestra la ventana para contar perikymata.
	     */
	    public void showPerikymataCount() {
	        try {
	            // Carga la clase FXML.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("vista/PerikymataCount.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	            
	            // Muestra la ventana en el centro del layout.
	            rootLayout.setCenter(personOverview);

	            // Da al controlador acceso a la aplicación principal.
	            PerikymataCountController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

		public Image getImagenCompleta() {
			return imagenCompleta;
		}

		public void setImagenCompleta(Image imagenCompleta) {
			this.imagenCompleta = imagenCompleta;
		}

		public Proyecto getProyecto() {
			return proyecto;
		}

		public void setProyecto(Proyecto proyecto) {
			this.proyecto = proyecto;
		}
}
