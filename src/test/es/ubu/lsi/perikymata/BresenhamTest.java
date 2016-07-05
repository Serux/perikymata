package test.es.ubu.lsi.perikymata;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import es.ubu.lsi.perikymata.modelo.filters.Prewitt;
import es.ubu.lsi.perikymata.util.ProfileUtil;
import es.ubu.lsi.perikymata.vista.PerikymataCountController;

public class BresenhamTest {

	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	@Test
	public void test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//TODO test more this.
		Method method = ProfileUtil.class.getDeclaredMethod("Bresenham", int.class, int.class, int.class, int.class);
		method.setAccessible(true);
		List<int[]> lista = (List<int[]>) method.invoke(new PerikymataCountController(), 0, 0, 3, 3);
		List<int[]> resultado = new ArrayList<>();
		resultado.add(new int[]{1,1});
		resultado.add(new int[]{2,2});
		resultado.add(new int[]{3,3});
		Iterator<int[]> itList = lista.iterator();
		Iterator<int[]> itResult = resultado.iterator();
		while(itList.hasNext()){
			assertArrayEquals(itList.next(), itResult.next());
		}
	}

}
