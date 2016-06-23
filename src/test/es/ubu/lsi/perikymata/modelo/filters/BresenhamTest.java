package test.es.ubu.lsi.perikymata.modelo.filters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import es.ubu.lsi.perikymata.vista.PerikymataCountController;

public class BresenhamTest {

	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	@Test
	public void test() {
		//TODO test more this.
		List<int[]> lista = new PerikymataCountController().Bresenham(0, 0, 3, 3);
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
