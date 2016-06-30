package test.es.ubu.lsi.perikymata.modelo.filters;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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
			
			int[] profile = new int[]{1,1,50,1,1,1};
			Gauss1D g = new Gauss1D();
			int[] res = g.convolve1D(profile, 10);
	
			System.out.println(Arrays.toString(res));
			
			int[] profile2 = new int[]{0,1,2,3,4,5,6,7,8,9};
			int[] res2 = g.convolve1D(profile2, 10);
			System.out.println(Arrays.toString(res2));
			
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
