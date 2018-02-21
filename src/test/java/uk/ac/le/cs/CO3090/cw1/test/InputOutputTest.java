package uk.ac.le.cs.CO3090.cw1.test;

import org.junit.jupiter.api.*;

import uk.ac.le.cs.CO3090.cw1.InputOutput;

@DisplayName ("Testing Unit InputOutput ... ")
public class InputOutputTest {
	static InputOutput inOut;
	@BeforeAll
	public static void setUp() {
		inOut = new InputOutput();
	}
	@Test
	public void testReadInput() {
	}
}
