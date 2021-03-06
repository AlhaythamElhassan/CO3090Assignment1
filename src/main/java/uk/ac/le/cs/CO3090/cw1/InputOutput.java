package uk.ac.le.cs.CO3090.cw1;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
/**
 * This class is responsible from in/out from main input/output streams
 * @author alhaytham
 *
 */
public class InputOutput {

	public static void main(String[] args) {
		InputOutput inout = new InputOutput();
		System.out.println("please enter a website and keywords saparated and ended with , e.g");
		System.out.println("https://www.bbc.co.uk, news, sport, brexit,");
		inout.read(System.in);
		inout.getTokens().forEach(token -> {
			System.out.println(token);
		});

	}

	private List<String> tokens = new ArrayList<String>();;

	public void read(InputStream input) {
		StringBuilder builder = new StringBuilder();
		int dataIn;
		try {
			dataIn = input.read();
			while(dataIn != 10) {
				if((char)dataIn != ',') {
					builder.append(((char)dataIn));
				}
				else {
					this.tokens.add(builder.toString().trim());
					builder.delete(0, builder.length()); // clear the builder
				}
				dataIn = input.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
/**
 * 
 * @return an array of strings that are the user inputs
 */
	public List<String> getTokens() {
		return tokens;
	}

}
