package uk.ac.le.cs.CO3090.cw1.test;

import org.junit.jupiter.api.*;
import uk.ac.le.cs.CO3090.cw1.InputOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This class is  testing InputOutput class
 */

@DisplayName ("Testing Unit InputOutput: ")
public class InputOutputTest {
	static InputOutput inOut;
	@BeforeAll
	public static void setUp() {
		inOut = new InputOutput();
	}
	@Test
	@DisplayName ("Test Input: ")
	public void testReadInput() {
		String sample = "https://blackboard.le.ac.uk,a,b,";
		byte [] expectedInput = sample.getBytes();
		
		
		InputStream input = new ByteArrayInputStream(expectedInput);
		inOut.read(input);
		assertEquals("https://blackboard.le.ac.uk", inOut.getTokens().get(0));
		assertEquals("a", inOut.getTokens().get(1));
		assertEquals("b", inOut.getTokens().get(2));
	}

}
