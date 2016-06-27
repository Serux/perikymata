package es.ubu.lsi.perikymata.vista;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.stream.events.StartDocument;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.util.ProfileUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

/**
 * Controller for the layout that is used to count perikyma.
 * 
 * @author Sergio Chico Carrancio
 *
 */
public class PerikymataCountController {
	/**
	 *  Reference to the main application.
	 */
	private MainApp mainApp;

	/**
	 * Start and end X coordinate of the deciles on the original image.
	 */
	private Double xDecileStart = null;
	private Double xDecileEnd = null;
	
	/**
	 * X coordinates of the deciles between the DecileStart and DecileEnd.
	 */
	private double[] decilesBetween = new double[9];

	/**
	 * Start and end Line objects drawn over the image.
	 */
	private Line lineDecileStart;
	private Line lineDecileEnd;
	
	/**
	 * Line objects of the deciles between DecileStart and DecileEnd
	 */
	private Line[] decilesLinesBetween = new Line[9];

	/**
	 * Free-Draw line drawn over the image.
	 */
	private Path freeDrawPath;
	
	/**
	 * List of points used by path to draw over the image. These List of coordinates
	 * are calculated over the original image, and are used to calculate the line
	 * when it is zoomed. The first element is always a MoveTo and the rest are LineTo.
	 */
	// TODO maybe not observable needed
	private List<PathElement> freeDrawPathList = FXCollections.observableArrayList();
	
	/**
	 * Coordinates of the detected perikymata.
	 */
	private List<int[]> peaksCoords = new ArrayList<>();
	
	/**
	 * Circles drawn to show where perikymata are found.
	 */
	//TODO observable maybe not needed.
	private List<Circle> circles = FXCollections.observableArrayList();

	//TODO maybe can be used to refactor the adding of graphic components.
	@FXML
	private AnchorPane imageAnchorPane;
	
	private double[] startMeasure;
	private double[] endMeasure;
	private Line measureLine;
	

	/**
	 * Imageview of the image used to calculate the perikymata.
	 */
	@FXML
	private ImageView fullImage;

	/**
	 * Label that shows the current action status.
	 */
	@FXML
	private Label statusLabel;

	/**
	 * Initializes the Javafx components.
	 */
	@FXML
	private void initialize() {
		
		measureLine = new Line();
		measureLine.setStrokeWidth(2);
		measureLine.setStroke(Color.RED);
		((AnchorPane) fullImage.getParent()).getChildren().add(measureLine);
		
		lineDecileStart = new Line();
		lineDecileStart.setStroke(Color.CORNFLOWERBLUE);
		((AnchorPane) fullImage.getParent()).getChildren().add(lineDecileStart);

		lineDecileEnd = new Line();
		lineDecileEnd.setStroke(Color.CORNFLOWERBLUE);
		((AnchorPane) fullImage.getParent()).getChildren().add(lineDecileEnd);

		for (int i = 0; i < decilesLinesBetween.length; i++) {
			decilesLinesBetween[i] = new Line();
			decilesLinesBetween[i].setStroke(Color.CORNFLOWERBLUE);
			((AnchorPane) fullImage.getParent()).getChildren().add(decilesLinesBetween[i]);
		}
		
		freeDrawPath = new Path();
		freeDrawPath.setStrokeWidth(2);
		freeDrawPath.setStroke(Color.RED);

		((AnchorPane) fullImage.getParent()).getChildren().add(freeDrawPath);
	}

	/**
	 * Handles the zooming in, redraws the image elements.
	 */
	@FXML
	private  void zoomPlus() {
		fullImage.setFitHeight(fullImage.getFitHeight() * 1.25);
		fullImage.setFitWidth(fullImage.getFitWidth() * 1.25);

		reDrawElements();


	}

	/**
	 * Handles the zooming out, redraws the image elements.
	 */
	@FXML
	private  void zoomMinus() {
		fullImage.setFitHeight(fullImage.getFitHeight() * 0.75);
		fullImage.setFitWidth(fullImage.getFitWidth() * 0.75);

		reDrawElements();

	}

	/**
	 * Handles the selection of the bounds of the tooth.
	 */
	@FXML
	private void selectStart() {
		clearImageViewHandlers();
		
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
					xDecileStart = modifyLine(mouseEvent, lineDecileStart);
					fullImage.setOnMouseClicked(null);
					statusLabel.setText("Start point selected.");
					if (xDecileStart != null && xDecileEnd != null){
						calculateDeciles();
					}
				}
			}
		};
		statusLabel.setText("Selecting Start point.");
		fullImage.setPickOnBounds(true);
		fullImage.setOnMouseClicked(mouseHandler);

	}

	/**
	 * Handles the selection of the bounds of the tooth.
	 */
	@FXML
	private void selectEnd() {
		clearImageViewHandlers();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
					xDecileEnd = modifyLine(mouseEvent, lineDecileEnd);
					fullImage.setOnMouseClicked(null);
					statusLabel.setText("End point selected.");
					if (xDecileStart != null && xDecileEnd != null){
						calculateDeciles();
					}
				}
			}
		};
		statusLabel.setText("Selecting End point.");
		fullImage.setPickOnBounds(true);
		fullImage.setOnMouseClicked(mouseHandler);

	}

	/**
	 * Modifies the line to a new position defined by the coordinates of the mouseEvent and
	 * returns the X coordinate on the real image.
	 * @param me Mouse event that shows the new position of the line.
	 * @param line Line to draw or redraw.
	 * @return x position relative to the real image.
	 */
	private double modifyLine(MouseEvent me, Line line) {
		line.setStartX(new Double(me.getX()));
		line.setEndX(new Double(me.getX()));
		line.setStartY(0);
		line.setEndY(new Double(fullImage.getFitHeight()));
		double ratio = (fullImage.getImage().getWidth() / fullImage.getFitWidth());
		return Double.valueOf(me.getX() * ratio);
	}

	/**
	 * Clears the line that has been drawn to detect perikymata and clears the mouse
	 * handlers.
	 */
	@FXML
	private void clearLine() {
		clearImageViewHandlers();
		statusLabel.setText("Line cleared.");
		this.freeDrawPathList.clear();
		freeDrawPath.getElements().clear();
	}

	/**
	 * Handles the selection of the bounds of the tooth.
	 */
	@FXML
	private void drawPath() {
		
		clearImageViewHandlers();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				double ratio = (fullImage.getImage().getWidth() / fullImage.getFitWidth());
				if (mouseEvent.getButton().compareTo(MouseButton.SECONDARY) == 0) {
					statusLabel.setText("Finished drawing line.");
					clearImageViewHandlers();
				} else if (freeDrawPath.getElements().isEmpty() 
						&& (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED
						|| mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED)) {
					freeDrawPath.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
					freeDrawPathList.add(new MoveTo(mouseEvent.getX() * ratio,mouseEvent.getY() * ratio));
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED
						|| mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
					freeDrawPath.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
					freeDrawPathList.add(new LineTo(mouseEvent.getX() * ratio,mouseEvent.getY() * ratio));

				}
				;
			}
		};
		statusLabel.setText("Drawing line, draw or click between two points. Right click to end.");
		fullImage.setPickOnBounds(true);
		fullImage.setOnMouseClicked(mouseHandler);
		fullImage.setOnMouseDragged(mouseHandler);
		fullImage.setOnMousePressed(mouseHandler);

	}

	/**
	 * Draws circles where perikymata are detected.
	 */
	private void drawPeaks() {
		((AnchorPane) fullImage.getParent()).getChildren().removeAll(circles);
		circles.clear();
		double ratio = (fullImage.getImage().getWidth() / fullImage.getFitWidth());
		for (int[] peak : peaksCoords) {
			Circle c = new Circle(peak[0] / ratio,peak[1] / ratio, 2);
			c.setStroke(Color.TEAL);
			c.setFill(Color.TEAL);
			((AnchorPane) fullImage.getParent()).getChildren().add(c);
			circles.add(c);
		}
	
	}

	/**
	 * Redraws the graphic interface objects, usually used when zooming.
	 */
	private void reDrawElements() {
		double ratio = (fullImage.getImage().getWidth() / fullImage.getFitWidth());
		//Draws the perikymata circles.
		if (!circles.isEmpty()){
			drawPeaks();
		}
		
		if(startMeasure!= null && endMeasure != null){
			measureLine.setStartX(startMeasure[0]/ratio);
			measureLine.setStartY(startMeasure[1]/ratio);
			measureLine.setEndX(endMeasure[0]/ratio);
			measureLine.setEndY(endMeasure[1]/ratio);
			
		}

		//Draws the deciles.
		if (xDecileStart != null) {
			lineDecileStart.setStartX(xDecileStart / ratio);
			lineDecileStart.setEndX(xDecileStart / ratio);
			lineDecileStart.setEndY(fullImage.getFitHeight());
		}
		if (xDecileEnd != null) {
			lineDecileEnd.setStartX(xDecileEnd / ratio);
			lineDecileEnd.setEndX(xDecileEnd / ratio);
			lineDecileEnd.setEndY(fullImage.getFitHeight());
		}

		if (xDecileStart != null && xDecileEnd != null) {
			for (int i = 0; i < 9; i++) {
				decilesLinesBetween[i].setStartX(decilesBetween[i] / ratio);
				decilesLinesBetween[i].setEndX(decilesBetween[i] / ratio);
				decilesLinesBetween[i].setEndY(fullImage.getFitHeight());
			}
		}

		//Redraws the free-Draw line.
		if (!freeDrawPathList.isEmpty()) {
			freeDrawPath.getElements().clear();
			freeDrawPath.getElements().add(new MoveTo(
					((MoveTo) freeDrawPathList.get(0)).getX() / ratio,
					((MoveTo) freeDrawPathList.get(0)).getY() / ratio));
			for (int i = 1; i < freeDrawPathList.size(); i++) {
				freeDrawPath.getElements().add(new LineTo(
								((LineTo) freeDrawPathList.get(i)).getX()/ ratio,
								((LineTo) freeDrawPathList.get(i)).getY()/ ratio));

			}
		}
	}
	
	/**
	 * Given a start and an end, divides the interval into 10 equal slices (deciles) 
	 * and draws those slices over the image.
	 */
	private void calculateDeciles() {
		double min = xDecileStart, max = xDecileEnd;
		if (Double.compare(xDecileStart, xDecileEnd) > 0) {
			min = xDecileEnd;
			max = xDecileStart;
		}
		double sizeDecil = (max - min) / 10;

		decilesBetween[0] = min + sizeDecil;
		for (int i = 1; i < 9; i++) {
			decilesBetween[i] = decilesBetween[i - 1] + sizeDecil;
		}
		reDrawElements();
	}

	

	/**
	 * Handler that calculates the localization of the perikymata over the drawn
	 * line and draws circles there.
	 */
	@FXML
	private void calculatePerikymata() {
		if (!freeDrawPathList.isEmpty()) {
			//TODO maybe all this can be done in the util.
			List<int[]> profile = ProfileUtil.getProfilePixels(this.freeDrawPathList);
			List<Integer> peaksIndexes = ProfileUtil.findLocalPeaks(ProfileUtil.getIntensityProfile(profile,mainApp));
			for (Integer i : peaksIndexes) {
				peaksCoords.add(profile.get(i));
	
			}
			drawPeaks();
		} else {
			statusLabel.setText("Line has not been drawn");
		}
	}
	
	/**
	 * Clears the handlers of the imageview.
	 */
	private void clearImageViewHandlers() {
		fullImage.setOnMouseClicked(null);
		fullImage.setOnMouseDragged(null);
		fullImage.setOnMousePressed(null);
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * Also, sets the full Image. This is done here because when the method
	 * initialize is called, there is no reference to the mainapp.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		if (mainApp.getFullImage() != null) {
			fullImage.setImage(mainApp.getFilteredImage());
			fullImage.setFitHeight(fullImage.getImage().getHeight());
			fullImage.setFitWidth(fullImage.getImage().getWidth());
			fullImage.setPreserveRatio(true);
		}
	}
	
	@FXML
	private void measureStartHandler(){
		clearImageViewHandlers();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
					if(startMeasure == null){
						startMeasure = new double[2];
					}
					startMeasure[0] = mouseEvent.getX() * getImageToImageViewRatio();
					startMeasure[1] = mouseEvent.getY() * getImageToImageViewRatio();
					fullImage.setOnMouseClicked(null);
					statusLabel.setText("Start measure point selected.");
					if (startMeasure != null && endMeasure != null){
						measure();
					}
				}
			}
		};
		statusLabel.setText("Selecting start point for the measure.");
		fullImage.setPickOnBounds(true);
		fullImage.setOnMouseClicked(mouseHandler);
	}
	
	@FXML
	private void measureEndHandler(){
		clearImageViewHandlers();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
					if(endMeasure == null){
						endMeasure = new double[2];
					}
					endMeasure[0] = mouseEvent.getX() * getImageToImageViewRatio();
					endMeasure[1] = mouseEvent.getY() * getImageToImageViewRatio();
					fullImage.setOnMouseClicked(null);
					statusLabel.setText("End measure point selected.");
					if (startMeasure != null && endMeasure != null){
						measure();
					}
				}
			}
		};
		statusLabel.setText("Selecting End point for the measure.");
		fullImage.setPickOnBounds(true);
		fullImage.setOnMouseClicked(mouseHandler);
	}
	
	@FXML
	private void measure(){
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Input the image measure unit and measure value.");
		dialog.setHeaderText("Input the image measure unit and measure value.");

		// Set the button types.
		ButtonType doneButtonType = new ButtonType("Done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField measureUnit = new TextField();
		measureUnit.setPromptText("Measure unit");
		TextField measureValue = new TextField();
		measureValue.setPromptText("Measure value");
		measureValue.setTextFormatter(new TextFormatter<String>(
				change->{
					if (change.getText().matches("[0-9]*")) {
						return change;
					}
					return null;
		}));
		
			

		grid.add(new Label("Measure unit:"), 0, 0);
		grid.add(measureUnit, 1, 0);
		grid.add(new Label("Measure value:"), 0, 1);
		grid.add(measureValue, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node acceptButton = dialog.getDialogPane().lookupButton(doneButtonType);
		acceptButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		measureValue.textProperty().addListener((observable, oldValue, newValue) -> {
		    acceptButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> measureUnit.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == doneButtonType) {
		        return new Pair<>(measureUnit.getText(), measureValue.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(measureValues -> {
		    System.out.println("Unit=" + measureValues.getKey() + ", Value=" + measureValues.getValue());
		});
	}
	
	private double getImageToImageViewRatio(){
		return fullImage.getImage().getWidth() / fullImage.getFitWidth();
	}
	
	@FXML
	private void handleDrawPerikymata(){
		clearImageViewHandlers();
		EventHandler<Event> h = evt->{
			if(((MouseEvent)evt).getButton().compareTo(MouseButton.SECONDARY)==0){
				clearImageViewHandlers();
			} else {
			peaksCoords.add(new int[]{(int) (((MouseEvent)evt).getX()*this.getImageToImageViewRatio()),(int) (((MouseEvent)evt).getY()*this.getImageToImageViewRatio())});
			drawPeaks();
			}
		};
		fullImage.setOnMousePressed(h);
	}
	
	@FXML 
	private void handleErasePerikymata(){
		clearImageViewHandlers();
		EventHandler<Event> h = evt->{
			Circle c = (Circle)evt.getSource();
			peaksCoords.remove(circles.indexOf(c));
			drawPeaks();
			handleErasePerikymata();
		};
		
		for(Circle c : circles){
			c.addEventHandler(MouseEvent.MOUSE_CLICKED, h);
		}
		
	}
	

}
