package test.es.ubu.lsi.perikymata;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import es.ubu.lsi.perikymata.util.CSVUtil;
import es.ubu.lsi.perikymata.vista.PerikymataCountController;

public class CSVUtilTest {

	@Test
	public void test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//It is a private method, so we make it accessible to be able to test it.
		Method method = CSVUtil.class.getDeclaredMethod("orderPeaksByxCoord",List.class);
		method.setAccessible(true);
		
		List<int[]> l = new ArrayList<>();
		l.add(new int[]{10,0});
		l.add(new int[]{7,5});
		l.add(new int[]{3,0});
		l.add(new int[]{4,2});
		l.add(new int[]{1,2});
		
		//ordering
		method.invoke(new PerikymataCountController(), l);
		
		List<int[]> lr = new ArrayList<>();
		lr.add(new int[]{1,2});
		lr.add(new int[]{3,0});
		lr.add(new int[]{4,2});
		lr.add(new int[]{7,5});
		lr.add(new int[]{10,0});
		
		Iterator<int[]> il = l.iterator();
		Iterator<int[]> ilr = lr.iterator();
		while(il.hasNext() && ilr.hasNext()){
			assertArrayEquals(il.next(), ilr.next());
		}
	}

}
