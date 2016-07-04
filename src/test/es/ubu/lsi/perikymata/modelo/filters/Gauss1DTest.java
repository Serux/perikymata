package test.es.ubu.lsi.perikymata.modelo.filters;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import es.ubu.lsi.perikymata.modelo.filters.Gauss1D;


public class Gauss1DTest {
	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	
	@Test
	public void test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			//TODO test more this.
			Method method = Gauss1D.class.getDeclaredMethod("calculateHannArray", int.class);
			method.setAccessible(true);
			float[] out = (float[]) method.invoke(new Gauss1D(), 10);
			System.out.println(Arrays.toString(out));
			
			List<Integer> profile = new ArrayList<>(Arrays.asList(1,1,50,1,1,1));
			Gauss1D g = new Gauss1D();
			List<Integer> res = g.convolve1D(profile, 10);
	
			System.out.println(res.toString());
			
			ArrayList<Integer> profile2 =  new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
			List<Integer> res2 = g.convolve1D(profile2, 10);
			System.out.println(res2.toString());
			
			/* Image image;
		        ImageView imageView;
		  
		        BorderPane pane;
		        Scene scene;
		        Stage stage;
			imageView = new ImageView(image);
			// Our image will sit in the middle of our popup.
	        pane = new BorderPane();
	        pane.setCenter(imageView);
	        scene = new Scene(pane);

	        // Create the actual window and display it.
	        stage = new Stage();
	        stage.setScene(scene);
	        stage.showAndWait();*/
	}
			

}
