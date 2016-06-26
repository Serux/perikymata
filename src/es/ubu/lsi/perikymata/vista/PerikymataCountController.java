package es.ubu.lsi.perikymata.vista;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.sun.media.jfxmedia.logging.Logger;

import es.ubu.lsi.perikymata.MainApp;
import es.ubu.lsi.perikymata.util.OrthogonalUtil;
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
    	
    	drawLines();
		
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
    	
    	 drawLines();
  
		
    	if(fullImage.getFitHeight()<=50){
    		zoomMinusButton.setDisable(true);
    		
    	}
    }
    
    /**
     * Handles the selection of the bounds of the tooth.
     */
    @FXML
    private void selectStart(){
    	cancelMouseHandlers();
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    		 
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	          if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
    	        	  xStart = modifyLine(mouseEvent,lineStart);
    	            fullImage.setOnMouseClicked(null);
    	            statusLabel.setText("Start point selected.");
    	            if(xStart != null && xEnd != null) 	calculateDeciles();
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
    private void selectEnd(){
    	cancelMouseHandlers();
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    	
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	          if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
    	        	xEnd = modifyLine(mouseEvent,lineEnd);
    	            fullImage.setOnMouseClicked(null);
    	            statusLabel.setText("End point selected.");
    	            if(xStart != null && xEnd != null) 	calculateDeciles();
    	          } 
    	        }
    	      };
    	statusLabel.setText("Selecting End point.");
    	fullImage.setPickOnBounds(true);
        fullImage.setOnMouseClicked(mouseHandler);
        
    }

    private double modifyLine(MouseEvent me, Line line){
    	         
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
    	 cancelMouseHandlers();
    	 EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    	        @Override
    	        public void handle(MouseEvent mouseEvent) {
    	        	if (mouseEvent.getButton().compareTo(MouseButton.SECONDARY)==0){
    	        		statusLabel.setText("Finished drawing line.");
    	        	    cancelMouseHandlers();
    	        	}else if (path.getElements().isEmpty() && (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED || mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED)) {
    	            path.getElements()
    	                .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
    	            	coords.add(
    	            			new MoveTo(
    	            					mouseEvent.getX() * (fullImage.getImage().getWidth()/fullImage.getFitWidth()), 
    	            					mouseEvent.getY() * (fullImage.getImage().getWidth()/fullImage.getFitWidth())
    	            					)
    	            			);
    	          } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED || mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
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
    	      statusLabel.setText("Drawing line, draw or click between two points. Right click to end.");
    	      fullImage.setPickOnBounds(true);
    	      fullImage.setOnMouseClicked(mouseHandler);
    	      fullImage.setOnMouseDragged(mouseHandler);
    	      fullImage.setOnMousePressed(mouseHandler);
    	
        
    }
    
    private void drawLines(){
		
		if(xStart != null){
			lineStart.setStartX(xStart/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
			lineStart.setEndX(xStart/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
			lineStart.setEndY(fullImage.getFitHeight());
		}
		if(xEnd != null){
			lineEnd.setStartX(xEnd/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
			lineEnd.setEndX(xEnd/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
			lineEnd.setEndY(fullImage.getFitHeight());
		}
		
		if(xStart != null && xEnd != null){
		for(int i =0; i<9; i++){
			decilesLines[i].setStartX(deciles[i]/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
			decilesLines[i].setEndX(deciles[i]/(fullImage.getImage().getWidth()/fullImage.getFitWidth()));
			decilesLines[i].setEndY(fullImage.getFitHeight());
		}
		}
		
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
    
    public List<int[]> Bresenham(int x0, int y0, int x1, int y1) { 
    	  int x, y, dx, dy, p, incE, incNE, stepx, stepy;
    	  dx = (x1 - x0);
    	  dy = (y1 - y0);
    	  List<int[]> llist = new LinkedList<>();

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
    	      llist.add(new int[]{x,y});
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
    	      llist.add(new int[]{x,y});
    	    }
    	  }
    	  return llist;
    	 }
    
    private List<int[]> getProfilePixels(){
    	LinkedList<int[]> profile = new LinkedList<>();
    	int x0 = (int)((MoveTo)(coords.get(0))).getX();
    	int y0 = (int)((MoveTo)(coords.get(0))).getY();
    	profile.add(new int[]{x0,y0});
    	int x1;
    	int y1;
    	for(int i =1; i<coords.size(); i++){
    		x1 = (int)((LineTo)(coords.get(i))).getX();
    		y1 = (int)((LineTo)(coords.get(i))).getY();
    		profile.addAll(this.Bresenham(x0, y0, x1, y1));
    		x0 = x1;
    		y0 = y1;
    	}
    	return profile;
    }
    
    @FXML
    private void calculatePerikymata(){
    	for(int el : getIntensity())
    		System.out.println(el);
    }
    
    @FXML 
    private void clearLine(){
    	cancelMouseHandlers();
    	statusLabel.setText("Line cleared.");
    	this.coords.clear();
    	path.getElements().clear();
    }
    
    private List<Integer> getIntensity(){
    	BufferedImage img = SwingFXUtils.fromFXImage(mainApp.getFullImage(), null);
    	List<Integer> l = OrthogonalUtil.getOrthogonalProfile(img, getProfilePixels(), 2);
    	return l;
    }
    
    private void cancelMouseHandlers(){
    	fullImage.setOnMouseClicked(null);
 	      fullImage.setOnMouseDragged(null);
 	      fullImage.setOnMousePressed(null);
    }
    
    
}

   
    

   

