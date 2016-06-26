package es.ubu.lsi.perikymata.vista;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.util.ProfileUtil;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

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
	private List<int[]> peaksCoords;
	
	/**
	 * Circles drawn to show where perikymata are found.
	 */
	//TODO observable maybe not needed.
	private List<Circle> circles = FXCollections.observableArrayList();

	//TODO maybe can be used to refactor the adding of graphic components.
	@FXML
	private AnchorPane imageAnchorPane;
	


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
		cancelMouseHandlers();
		
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
		cancelMouseHandlers();
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
		//TODO refactor redimension
		return Double.valueOf(me.getX() * (fullImage.getImage().getWidth() / fullImage.getFitWidth()));
	}

	/**
	 * Clears the line that has been drawn to detect perikymata and clears the mouse
	 * handlers.
	 */
	@FXML
	private void clearLine() {
		cancelMouseHandlers();
		statusLabel.setText("Line cleared.");
		this.freeDrawPathList.clear();
		freeDrawPath.getElements().clear();
	}

	/**
	 * Handles the selection of the bounds of the tooth.
	 */
	@FXML
	private void drawPath() {
		
		cancelMouseHandlers();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				double ratio = (fullImage.getImage().getWidth() / fullImage.getFitWidth());
				if (mouseEvent.getButton().compareTo(MouseButton.SECONDARY) == 0) {
					statusLabel.setText("Finished drawing line.");
					cancelMouseHandlers();
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
			peaksCoords = new ArrayList<>();
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
	private void cancelMouseHandlers() {
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

}
