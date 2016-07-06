package test.es.ubu.lsi.perikymata;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import es.ubu.lsi.perikymata.modelo.filters.Prewitt;

/**
 * Test the prewitt filter class.
 * 
 * @author Sergio Chico Carrancio
 */
public class PrewittTest {
	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	
	Prewitt filter = null;
	@Test
	public void testMakeMatrix() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		//Workaround private methods
		Method method = Prewitt.class.getDeclaredMethod("calculatePrewittMatrix", new Class[]{});
		method.setAccessible(true);
		
		//size 3, strength 1, so each row is 1,0,-1
		filter = new Prewitt(3,1);
		float[] matrix = (float[]) method.invoke(filter);
		Assert.assertArrayEquals(new float[]{1,0,-1,
											 1,0,-1,
											 1,0,-1}, matrix,0);
		
		//same as the previous test, but each number is multiplied by the strength.
		filter = new Prewitt(3,2);
		float[] matrix2 = (float[]) method.invoke(filter);
		Assert.assertArrayEquals(new float[]{2,0,-2,
											 2,0,-2,
											 2,0,-2}, matrix2,0);
		
		//A bigger prewitt custom matrix. In each row the first number is the
		//higher. Center number is always zero. Last number is the lower.
		filter = new Prewitt(5,1);
		float[] matrix3 = (float[]) method.invoke(filter);
		Assert.assertArrayEquals(new float[]{2,1,0,-1,-2,
											 2,1,0,-1,-2,
											 2,1,0,-1,-2,
											 2,1,0,-1,-2,
											 2,1,0,-1,-2}, matrix3,0);
	}
	
	@Test
	public void testRun() {
		//Can test that exceptions aren't thrown, but can't find an easy way
		//to check that we got the expected result.
	}

}
