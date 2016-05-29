package es.ubu.lsi.perikymata.vista;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.modelo.filters.Filter;
import es.ubu.lsi.perikymata.modelo.filters.Gauss;
import es.ubu.lsi.perikymata.modelo.filters.Prewitt;
import ij.plugin.filter.GaussianBlur;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Controller for the layout that filters images.
 * 
 * @author Sergio Chico Carrancio
 */
public class ImageFiltersController {
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	/**
	 * Reference to the main application
	 */
	private MainApp mainApp;

	/**
	 * Loading gif.
	 */
    @FXML
	private ImageView loading;
    
	/**
	 * List of operations to be executed.
	 */
    @FXML
	private TableView<Filter> filtersTable;
    
    /**
     * Column of the table that shows the name of the filter.
     */
    @FXML
    private TableColumn<Filter, String> filtersColumn;
    
    /**
     * Column of the table that shows the arguments the filter.
     */
    @FXML
    private TableColumn<Filter, String> argumentsColumn;
    

	/**
	 * Reference to the filtered image.
	 */
	@FXML
	private ImageView filteredImage;
	
	/**
	 * Image used to apply the filters.
	 */
	private BufferedImage auxImage;
	
	/**
	 * Reference to the original image.
	 */
	@FXML
	private ImageView originalImage;

	
	
	/**
	 * True if a thread is working, false otherwise.
	 */
	private AtomicBoolean working = new AtomicBoolean(false);

	/**
	 * Current status, tells to the user if a Thread is running.
	 */
	@FXML
	private Label status;

	/**
	 * Reference to the first Scrollpane.
	 */
	@FXML
	private ScrollPane scrollPane1;
	
	/**
	 * Reference to the second Scrollpane.
	 */
	@FXML
	private ScrollPane scrollPane2;
	
	/**
	 * Button to delete a selected filter.
	 */
	@FXML
	private Button deleteFilterButton;

	////////////////////////Prewitt Filter///////////////////////
	/**
	 * Button to ad a new prewitt filter.
	 */
	@FXML
	private Button addPrewittButton;
	
	/**
	 * Slider for controlling the size of the prewitt mask.
	 */
	@FXML
	private Slider prewittSizeSlider;
	
	/**
	 * Label to show the exact size of the force on a prewitt filter.
	 */
	@FXML
	private Label prewittForceLevel;

	/**
	 * Slider for controlling the force of the prewitt mask.
	 */
	@FXML
	private Slider prewittForceSlider;
	
	/**
	 * Label to show the exact size of the mask on a prewitt filter.
	 */
	@FXML
	private Label prewittSizeLevel;

	//////////////////////Gaussian Filter/////////////////////////

	/**
	 * Button to ad a new gauss filter.
	 */
	@FXML
	private Button addGaussButton;
	
	/**
	 * Slider for controlling the sigma of the gauss filter.
	 */
	@FXML
	private Slider gaussSlider;

	/**
	 * Label to show the exact size of sigma on a gaussian filter.
	 */
	@FXML
	private Label gaussLevel;

	/////////////////////////////////////////////////////////////

	/**
	 * Initializes the components when the controller is loaded.
	 */
	@FXML
	private void initialize() {
		// binds scroll properties together to make both scrolls move at the
		// same time.
		scrollPane1.vvalueProperty().bindBidirectional(scrollPane2.vvalueProperty());
		scrollPane1.hvalueProperty().bindBidirectional(scrollPane2.hvalueProperty());

		// Handler that changes a labels to show the value of the sliders.
		gaussLevel.textProperty().bindBidirectional(gaussSlider.valueProperty(), new DecimalFormat("##.##"));
		prewittForceLevel.textProperty().bindBidirectional(prewittForceSlider.valueProperty(), new DecimalFormat("#.##"));
		prewittSizeLevel.textProperty().bindBidirectional(prewittSizeSlider.valueProperty(), new DecimalFormat("#"));
		status.setText("Idle");
		
		// Loads loading gif.
		loading.setImage(new Image(new File("rsc/482.gif").toURI().toString()));
		loading.setVisible(false);
		
		// Initialize the person table with the two columns.
        filtersColumn.setCellValueFactory(cellData -> cellData.getValue().getFiltername());
        argumentsColumn.setCellValueFactory(cellData -> cellData.getValue().getFilterArgs());
       
        //http://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows
        filtersTable.setRowFactory(tv -> {
            TableRow<Filter> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Filter draggedPerson = filtersTable.getItems().remove(draggedIndex);

                    int dropIndex ; 

                    if (row.isEmpty()) {
                        dropIndex = filtersTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    filtersTable.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    filtersTable.getSelectionModel().select(dropIndex);
                    event.consume();
                } });
            return row ;
        });
        
	}

	/**
	 * Handler that adds a prewitt filter to the list of filters.
	 */
	@FXML
	public void addPrewittFilter() {
		mainApp.getAppliedFilters().add(new Prewitt((int)prewittSizeSlider.getValue(), prewittForceSlider.getValue()));

	}
	
	/**
	 * Handler that adds a gaussian filter to the list of filters.
	 */
	@FXML
	public void addGaussianFilter() {
		mainApp.getAppliedFilters().add(new Gauss(gaussSlider.getValue()));
	}
	
	/**
	 * Handler that removes a filter from the list.
	 */
	@FXML
	public void handleRemoveFilter() {
	       int selectedIndex = filtersTable.getSelectionModel().getSelectedIndex();
	        if (selectedIndex >= 0) {
	        	filtersTable.getItems().remove(selectedIndex);
	        } else {
	            // Nothing selected.
	        	Alert alert = new Alert(AlertType.WARNING);
	        	alert.setTitle("No Selection");
	        	alert.setHeaderText("No Filter Selected");
	        	alert.setContentText("Please select a filter in the table to delete it");
	        	alert.showAndWait();
	        }
	}
	
	/**
	 * Handler that removes a filter from the list.
	 */
	@FXML
	public void handleRunFilters() {
		if (working.compareAndSet(false, true)) {
			// Saves a copy of the original image to apply filters
			auxImage = SwingFXUtils.fromFXImage(this.originalImage.getImage(), null);
			disableComponents();
			changeStatus("Applying filters, please wait");
			loading.setVisible(true);
			new Thread(() -> {
				ObservableList<Filter> f = this.filtersTable.getItems();
				for(int i =0; i< f.size(); i++ ){
					changeStatus("Aplying " + f.get(i).getFiltername().getValue() + ". " + (i+1) + "\\" + f.size());
					this.auxImage = f.get(i).run(auxImage);
				}
				
				filteredImage.setImage(SwingFXUtils.toFXImage(auxImage,null));
				changeStatus("Filters apply completed! Idle.");
				working.set(false);
				loading.setVisible(false);
				this.enableComponents();
			}).start();

		} else {
			changeStatus("Already applying filters, please wait.");
		}
	}
	
	
	private void disableComponents(){
		filtersTable.setDisable(true);
		addGaussButton.setDisable(true);
		addPrewittButton.setDisable(true);
		deleteFilterButton.setDisable(true);
	}
	private void enableComponents(){
		filtersTable.setDisable(false);
		addGaussButton.setDisable(false);
		addPrewittButton.setDisable(false);
		deleteFilterButton.setDisable(false);
	}
	
	/**
	 * Handler that applies a convolveFilter to the original image and stores it
	 * as a filtered image.
	 */
	@FXML
	public void applyConvolveFilter() {
		//TODO remove and generic run
		/*if (working.compareAndSet(false, true)) {
			changeStatus("Applying filters, please wait");
			loading.setVisible(true);
			new Thread(() -> {
				
				Filter f = new Prewitt(SwingFXUtils.fromFXImage(originalImage.getImage(), null), 3, 10);
				
				filteredImage.setImage(SwingFXUtils.toFXImage(f.run(),null));
				changeStatus("Filters apply completed! Idle.");
				working.set(false);
				loading.setVisible(false);
			}).start();

		} else {
			changeStatus("Already applying filters, please wait.");
		}*/

	}

	/**
	 * Handler that applies a convolveFilter to the original image and stores it
	 * as a filtered image.
	 */
	@FXML
	public void applyGaussFilter() {
		//TODO remove and generic run
		if (working.compareAndSet(false, true)) {
			changeStatus("Applying filters, please wait");
			//TODO loading image only in one function
			loading.setVisible(true);
			new Thread(() -> {
				GaussianBlur gauss = new GaussianBlur();
				ImageProcessor proc = new ColorProcessor(SwingFXUtils.fromFXImage(filteredImage.getImage(), null));
				gauss.blurGaussian(proc, gaussSlider.getValue());
				mainApp.setFilteredImage(SwingFXUtils.toFXImage((BufferedImage) proc.createImage(), null));
				filteredImage.setImage(mainApp.getFilteredImage());
				changeStatus("Filters apply completed! Idle.");
				working.set(false);
				loading.setVisible(false);
			}).start();

		} else {
			changeStatus("Already applying filters, please wait.");
		}
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * Also, sets the Images. This is done here because when the method
	 * initialize is called, there is no reference to the mainapp.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		if (mainApp.getFullImage() != null) {
			originalImage.setImage(mainApp.getFullImage());
			filteredImage.setImage(mainApp.getFilteredImage());
	        
	        // Add observable list data to the table
	        filtersTable.setItems(mainApp.getAppliedFilters());
	        
	     // Saves a copy of the original image to apply filters
	        auxImage = SwingFXUtils.fromFXImage(this.originalImage.getImage(), null);
		}
	}


	/**
	 * Handler that changes to the perikymata counting stage when called.
	 */
	@FXML
	public void nextScreen() {
		mainApp.showPerikymataCount();
	}
	
	/**
	 * Changes the text of the status label from the Platform because label can't
	 * be changed directly from a thread.
	 * @param text
	 */
	private synchronized void changeStatus(String text){
		Platform.runLater(()-> status.setText(text));
	}
}
