package test.es.ubu.lsi.perikymata;

import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;

import es.ubu.lsi.perikymata.MainApp;
import test.es.ubu.lsi.perikymata.modelo.filters.JavaFXThreadingRule;

public class MainAppTest {
	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	
	@Test
	public void loggerTest() {
		MainApp main = new MainApp();
		main.configureLogger();
		main.getLogger().log(Level.SEVERE, "Testing logger.", new RuntimeException());
	}
	
	

}
