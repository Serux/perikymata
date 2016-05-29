package test.es.ubu.lsi.perikymata.modelo.filters;


import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import es.ubu.lsi.perikymata.modelo.filters.Prewitt;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;


public class PrewittTest {
	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	
	Prewitt filtro = null;
	@Test
	public void testPrewitt() {
		Image image = new Image(new File("./rsc/perikymata.jpg").toURI().toString());
		filtro = new Prewitt(3,2);
	}

	@Test
	public void testMakeMatrix() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		//Workaround private methods
		Method method = Prewitt.class.getDeclaredMethod("calculatePrewittMatrix", new Class[]{});
		method.setAccessible(true);
		Image image = new Image(new File("./rsc/perikymata.jpg").toURI().toString());
		
		filtro = new Prewitt(3,1);
		float[] matrix = (float[]) method.invoke(filtro);
		Assert.assertArrayEquals(new float[]{1,0,-1,
											 1,0,-1,
											 1,0,-1}, matrix,0);
		
		filtro = new Prewitt(3,2);
		float[] matrix2 = (float[]) method.invoke(filtro);
		Assert.assertArrayEquals(new float[]{2,0,-2,
											 2,0,-2,
											 2,0,-2}, matrix2,0);
		
		filtro = new Prewitt(5,1);
		float[] matrix3 = (float[]) method.invoke(filtro);
		Assert.assertArrayEquals(new float[]{2,1,0,-1,-2,
											 2,1,0,-1,-2,
											 2,1,0,-1,-2,
											 2,1,0,-1,-2,
											 2,1,0,-1,-2}, matrix3,0);
	}
	
	@Test
	public void testRun() {
		Image image = new Image(new File("./rsc/perikymata.jpg").toURI().toString());
		filtro = new Prewitt(3,2);
		BufferedImage image2 = filtro.run(SwingFXUtils.fromFXImage(image,null));
	}

}
