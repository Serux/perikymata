package test.es.ubu.lsi.perikymata.modelo.filters;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import es.ubu.lsi.perikymata.vista.PerikymataCountController;

public class BresenhamTest {

	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	@Test
	public void test() {
		//TODO test more this.
		new PerikymataCountController().Bresenham(7, 0, 0, 5);
	}

}
