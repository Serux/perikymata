package es.ubu.lsi.perikymata.vista;

import java.util.List;

import com.sun.prism.image.Coords;

import es.ubu.lsi.perikymata.MainApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableListBase;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

/**
 * Controller for the layout that is used to count perikyma.
 * @author Sergio Chico Carrancio
 *
 */
public class PerikymataCountController {
	// Reference to the main application
    private MainApp mainApp;
    
    Double xStart = null;
    Double xEnd = null;
    Line lineStart;
    Line lineEnd;
    Path path;
    List<PathElement> coords = FXCollections.observableArrayList();
    
    double[] deciles = new double[9];
    Line[] decilesLines = new Line[9];
    
    private void drawLines(){
    	for(int i =0; i<9; i++){
    		decilesLines[i].setStartX(deciles[i]/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
    		decilesLines[i].setEndX(deciles[i]/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
    		decilesLines[i].setEndY(fullImage.getFitHeight());
    	}
    }
    
    @FXML
    private AnchorPane imageAnchorPane;
    @FXML
    private Button zoomMinusButton;

    @FXML 
    Label totalNumber;
    @FXML
    Label statusLabel;
    
    @FXML 
    Button countButton;
    
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
    	lineStart = new Line();
    	lineStart.setStroke(Color.CORNFLOWERBLUE);
    	((AnchorPane) fullImage.getParent()).getChildren().add(lineStart);
    	
    	lineEnd = new Line();
    	lineEnd.setStroke(Color.CORNFLOWERBLUE);
    	((AnchorPane) fullImage.getParent()).getChildren().add(lineEnd);
    	    	
    	for( int i =0; i<decilesLines.length; i++){
    		decilesLines[i] = new Line();
    		decilesLines[i].setStroke(Color.CORNFLOWERBLUE);
    		((AnchorPane) fullImage.getParent()).getChildren().add(decilesLines[i]);
    	}
    	path = new Path();
        path.setStrokeWidth(2);
        path.setStroke(Color.RED);
       // path.
        ((AnchorPane) fullImage.getParent()).getChildren().add(path);
    }
    /**
     * Handles the zooming in.
     */
    @FXML
    public void zoomPlus(){
    	fullImage.setFitHeight(fullImage.getFitHeight()*1.25);
    	fullImage.setFitWidth(fullImage.getFitWidth()*1.25);
    	if(xStart != null && xEnd != null) 	drawLines();
    	lineStart.setStartX(xStart/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineStart.setEndX(xStart/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineStart.setEndY(fullImage.getFitHeight());
		
		lineEnd.setStartX(xEnd/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineEnd.setEndX(xEnd/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineEnd.setEndY(fullImage.getFitHeight());
		
		//((MoveTo) path.getElements().get(0)).setX(value);
		
		if(!coords.isEmpty()){
		path.getElements().clear();
		path.getElements().add(
				new MoveTo(
						((MoveTo)coords.get(0)).getX()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()),
						((MoveTo)coords.get(0)).getY()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()))
				);
		for (int i = 1; i<coords.size(); i++){
			path.getElements().add(
					new LineTo(
							((LineTo)coords.get(i)).getX()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()),
							((LineTo)coords.get(i)).getY()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()))
					);
			
		}}
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
    	
    	if(xStart != null && xEnd != null) 	drawLines();
    	lineStart.setStartX(xStart/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineStart.setEndX(xStart/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineStart.setEndY(fullImage.getFitHeight());
		
		lineEnd.setStartX(xEnd/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineEnd.setEndX(xEnd/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
		lineEnd.setEndY(fullImage.getFitHeight());
		
		//TODO and refactor this
		if(!coords.isEmpty()){
		path.getElements().clear();
		path.getElements().add(
				new MoveTo(
						((MoveTo)coords.get(0)).getX()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()),
						((MoveTo)coords.get(0)).getY()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()))
				);
		for (int i = 1; i<coords.size(); i++){
			path.getElements().add(
					new LineTo(
							((LineTo)coords.get(i)).getX()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()),
							((LineTo)coords.get(i)).getY()/(fullImage.getImage().getWidth()/fullImage.getFitWidth()))
					);
			
		}
		}
    	if(fullImage.getFitHeight()<=50){
    		zoomMinusButton.setDisable(true);
    		
    	}
    }
    
    /**
     * Handles the selection of the bounds of the tooth.
     */
    @FXML
    private void selectStart(){
		 
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    		 
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	          if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
    	        	  xStart = modifyLine(mouseEvent,lineStart);
    	            fullImage.setOnMouseClicked(null);
    	            if(xStart != null && xEnd != null) 	calculateDeciles();
    	          } 
    	        }
    	      };
    	
    	fullImage.setPickOnBounds(true);
        fullImage.setOnMouseClicked(mouseHandler);
        
    }
    /**
     * Handles the selection of the bounds of the tooth.
     */
    @FXML
    private void selectEnd(){
		 
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    	
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	          if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
    	        	xEnd = modifyLine(mouseEvent,lineEnd);
    	            fullImage.setOnMouseClicked(null);
    	            if(xStart != null && xEnd != null) 	calculateDeciles();
    	          } 
    	        }
    	      };
    	
    	fullImage.setPickOnBounds(true);
        fullImage.setOnMouseClicked(mouseHandler);
        
    }

    private double modifyLine(MouseEvent me, Line line){
    	 
    	 ;            
         line.setStartX(new Double(me.getX()));
         line.setEndX(new Double(me.getX()));
         line.setStartY(0);
         line.setEndY(new Double(fullImage.getFitHeight()));
    	return Double.valueOf(me.getX() * (fullImage.getImage().getWidth()/fullImage.getFitWidth()));
    }
    
    
    
    /**
     * Handles the selection of the bounds of the tooth.
     */
    @FXML
    private void drawPath(){
		 
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	        	if (mouseEvent.getButton().compareTo(MouseButton.SECONDARY)==0){
    	        	     fullImage.setOnMouseClicked(null);
    	       	      fullImage.setOnMouseDragged(null);
    	       	      fullImage.setOnMousePressed(null);
    	        	}else if (path.getElements().isEmpty() && mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
    	            path.getElements()
    	                .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
    	            	coords.add(
    	            			new MoveTo(
    	            					mouseEvent.getX() * (fullImage.getImage().getWidth()/fullImage.getFitWidth()), 
    	            					mouseEvent.getY() * (fullImage.getImage().getWidth()/fullImage.getFitWidth())
    	            					)
    	            			);
    	          } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
    	            path.getElements()
    	                .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    	           	coords.add(
	            			new LineTo(
	            					mouseEvent.getX() * (fullImage.getImage().getWidth()/fullImage.getFitWidth()), 
	            					mouseEvent.getY() * (fullImage.getImage().getWidth()/fullImage.getFitWidth())
	            					)
	            			);
    	        
    	      };
    	        }};
    	    
    	      fullImage.setPickOnBounds(true);
    	      fullImage.setOnMouseClicked(mouseHandler);
    	      fullImage.setOnMouseDragged(mouseHandler);
    	      fullImage.setOnMousePressed(mouseHandler);
    	
        
    }
    
    private void calculateDeciles(){
    	double min = xStart,max = xEnd;
    	if (Double.compare(xStart, xEnd) > 0) {min = xEnd ; max=xStart;}
    	double sizeDecil = (max-min)/10;
    	
    	deciles[0] = min+sizeDecil;
    	for(int i=1;i<9;i++){
    		deciles[i]= deciles[i-1] + sizeDecil;
    	}
    	drawLines();
    }
    
    public void Bresenham(int x0, int y0, int x1, int y1) { 
    	  int x, y, dx, dy, p, incE, incNE, stepx, stepy;
    	  dx = (x1 - x0);
    	  dy = (y1 - y0);

    	 /* determinar que punto usar para empezar, cual para terminar */
    	  if (dy < 0) { 
    	    dy = -dy; 
    	    stepy = -1; 
    	  } 
    	  else {
    	    stepy = 1;
    	  }

    	  if (dx < 0) {  
    	    dx = -dx;  
    	    stepx = -1; 
    	  } 
    	  else {
    	    stepx = 1;
    	  }

    	  x = x0;
    	  y = y0;
    	 /* se cicla hasta llegar al extremo de la línea */
    	  if(dx>dy){
    	    p = 2*dy - dx;
    	    incE = 2*dy;
    	    incNE = 2*(dy-dx);
    	    while (x != x1){
    	      x = x + stepx;
    	      if (p < 0){
    	        p = p + incE;
    	      }
    	      else {
    	        y = y + stepy;
    	        p = p + incNE;
    	      }
    	      //TODO remove
    	      System.out.println("x="+x+";y="+y);
    	    }
    	  }
    	  else{
    	    p = 2*dx - dy;
    	    incE = 2*dx;
    	    incNE = 2*(dx-dy);
    	    while (y != y1){
    	      y = y + stepy;
    	      if (p < 0){
    	        p = p + incE;
    	      }
    	      else {
    	        x = x + stepx;
    	        p = p + incNE;
    	      }
    	      //TODO remove
    	      System.out.println("x="+x+";y="+y);
    	    }
    	  }
    	 }
}

   
    

   

