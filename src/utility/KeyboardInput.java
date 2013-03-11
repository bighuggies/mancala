package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardInput {
	private static BufferedReader _in  = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Set the input to come from the specified file, instead of stdin. This is
	 * intended to do automatic testing, so the the test input can come from a
	 * file. This is done because file redirection doesn't seem to work reliably.
	 * @param filename The path (relative or absolute) to read the input from.
	 */
	public static void setInput(String filename) {
		try {
			_in = new BufferedReader(new FileReader(filename));
			System.out.println("Input is now from " + filename);
		} catch (FileNotFoundException e) {
			System.out.println("%%Message: cannot find " + filename);
		}
	}

	/**
	 * Display the specified prompt to stdout, and then read a string from the command line 
	 * (or rather, the current input {@see #setInput(String)}). What is typed at the keyboard
	 * will appear immediately after the prompt, so any formatting must be provided in the
	 * prompt string. 
	 * 
	 * @param prompt The prompt to print.
	 * @return A string, or null if there is any problem reading the input.
	 */
	public static String readFromKeyboard(String prompt) {
		try {
			System.out.print(prompt);
			String read = _in.readLine();
			System.err.println("Got [" + read + "]");
			return read;
		} catch (IOException e) {
			System.out.println("%%Message: problem getting input");
		}
		return null;
	}
	/**
	 * Read an integer from a fixed range of numbers from the command line. If given 
	 * invalid inputs (either not an integer or not in the specified range) this method
	 * will continue to ask until it either gets a valid input or is told to cancel the
	 * attempt. 
	 * @param prompt What message to display when requesting the integer input
	 * @param lower The lower bound of the range (inclusive)
	 * @param upper The upper bound of the range (inclusive)
	 * @param cancelResult What value to return if the attempt at input is cancelled
	 * @param cancelStr What string the user should enter to cancel the input
	 * @return Either cancelResult or an integer in the range [lower..upper] (inclusive).
	 */
	public static int readInteger(String prompt, int lower, int upper, int cancelResult, String cancelStr) {
		int result;
		while (true) {
			String input = KeyboardInput.readFromKeyboard(prompt);
			//System.err.println("Got {" + input + "}");
			if (input.equals(cancelStr)) {
				return cancelResult;
			}
			try {
				result = Integer.parseInt(input);
				// If we get here, we must have a valid int
				if (result < lower || result > upper) {
					System.out.println("%%Message: `" + input + "' must be between " + 
							lower + " and " + upper + ", or '" + cancelStr + "' to cancel");
				} else {
					return result;
				}
			} catch (NumberFormatException nfex) {
				System.out.println("%%Message: `" + input + "' is not a valid response.\n" +
						"Input must be an integer between '" + lower + "' and '" + upper + "'");
			}
		}
	}

}
