package test.es.ubu.lsi.perikymata;

import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;

import es.ubu.lsi.perikymata.MainApp;

/**
 * Test that the logger is working.
 * @author Sergio Chico Carrancio
 *
 */
public class LoggerTest {
	@Rule 
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	
	@Test
	public void loggerTest() {
		//Must make a file on the running directory with this text. Hard to
		// make an assert of it because filename is current timestamp.
		MainApp main = new MainApp();
		main.configureLogger();
		main.getLogger().log(Level.SEVERE, "Testing logger.", new RuntimeException());
	}
	
	

}
