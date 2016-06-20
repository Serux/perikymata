package es.ubu.lsi.perikymata.vista;

import com.sun.prism.image.Coords;

import es.ubu.lsi.perikymata.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Controller for the layout that is used to count perikyma.
 * @author Sergio Chico Carrancio
 *
 */
public class PerikymataCountController {
	// Reference to the main application
    private MainApp mainApp;
    
    
    @FXML
    private AnchorPane imageAnchorPane;
    @FXML
    private Button zoomMinusButton;
    
    double x1 =0;
    Line line = new Line();

    
	@FXML
	private ImageView fullImage;
	 /**
     * Is called by the main application to give a reference back to itself.
     * Also, sets the full Image. This is done here because when the method
     * initialize is called, there is no reference to the mainapp.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if(mainApp.getFullImage()!= null){
        	fullImage.setImage(mainApp.getFilteredImage());
        	fullImage.setFitHeight(fullImage.getImage().getHeight());
        	fullImage.setFitWidth(fullImage.getImage().getWidth());
        	fullImage.setPreserveRatio(true);
        	

        }
    }
 
    @FXML
    private void initialize(){
    	((AnchorPane) fullImage.getParent()).getChildren().add(line);
    	/*
    	path = new Path();
        path.setStrokeWidth(2);
        path.setStroke(Color.RED);

        fullImage.setPickOnBounds(true);
        fullImage.setOnMouseClicked(mouseHandler);
        fullImage.setOnMouseDragged(mouseHandler);
        fullImage.setOnMouseEntered(mouseHandler);
        fullImage.setOnMouseExited(mouseHandler);
        fullImage.setOnMouseMoved(mouseHandler);
        fullImage.setOnMousePressed(mouseHandler);
        fullImage.setOnMouseReleased(mouseHandler);
        
        ((AnchorPane) fullImage.getParent()).getChildren().add(path);
        */
    }
    /**
     * Handles the zooming in.
     */
    @FXML
    public void zoomPlus(){
    	fullImage.setFitHeight(fullImage.getFitHeight()*1.25);
    	fullImage.setFitWidth(fullImage.getFitWidth()*1.25);
    	line.setStartX(x1/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		line.setEndX(x1/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		line.setEndY(fullImage.getFitHeight());
    	if(fullImage.getFitHeight()>=50){
    		zoomMinusButton.setDisable(false);
    	}
    }
    
    /**
     * Handles the zooming out.
     */
    @FXML
    public void zoomMinus(){
    	fullImage.setFitHeight(fullImage.getFitHeight()*0.75);
    	fullImage.setFitWidth(fullImage.getFitWidth()*0.75);
    	//TODO refactor this
    	line.setStartX(x1/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		line.setEndX(x1/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		line.setEndY(fullImage.getFitHeight());
    	if(fullImage.getFitHeight()<=50){
    		zoomMinusButton.setDisable(true);
    		
    	}
    }
    
    /**
     * Handles the selection of the bounds of the tooth.
     */
    @FXML
    private void selectBounds(){
		 
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    		 
    		 double x2 = fullImage.getImage().getWidth();
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	        	System.out.println("event");
    	          if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
    	            x1 = mouseEvent.getX() * (fullImage.getImage().getWidth()/fullImage.getFitWidth());            
    	            line.setStartX(mouseEvent.getX());
    	            line.setEndX(mouseEvent.getX());
    	            line.setStartY(0);
    	            line.setEndY(fullImage.getFitHeight());
    	            
    	          } 
    	        }
    	      };
    	
    	fullImage.setPickOnBounds(true);
        fullImage.setOnMouseClicked(mouseHandler);
        
    }
    
    /*
    Path path;
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            path.getElements().clear();
            path.getElements()
                .add(new MoveTo(fullImage.getmouseEvent.getX(), mouseEvent.getY()));
          } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            path.getElements()
                .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            System.out.println(path.toString());
          }
          

        }

      };
*/
    }

