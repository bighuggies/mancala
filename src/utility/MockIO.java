package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Supports automated testing for Mancala by providing the ability to
 * specify the inputs to be provided and the expected outputs that should result. 
 * As a side-effect, it supports a useful means to get input from the Keyboard,
 * and to record what input is provided, meaning it can be used as a replacement
 * for standard I/O for some applications. It can also be set up to take its
 * input from a file (separate from the testing functionality).
 */
public class MockIO implements IO {
	/**
	 * Set this to true to print output showing various states of the IO process,
	 * particularly useful for figuring out why tests have failed.
	 */
	private static final boolean DEBUG = false;
	
	/**
	 * Used to keep a record of the inputs provided
	 */
	private List<String> _inputs;
	/**
	 * Indicates whether or not the inputs provided are being recorded.
	 */
	private boolean _recording;
	
	/**
	 * The file containing the specification for the test (including input)
	 */
	private BufferedReader _expectedIO  = null;
	
	/**
	 * The line number of the specification file that has been last read. This
	 * helps provide more useful reports when things go wrong.
	 */
	private int _lineCount = 0;
	
	/**
	 * The next line of expected output. If null, then the line
	 * has been matched (or things have just started)
	 */
	private String _currentOutput = null;
	
	/**
	 * What has been seen so far on the current line.
	 */
	private String _seenOutput = null;
	
	/**
	 * Create a new MockIO object that does not record the input.
	 */
	public MockIO() {
		_recording = false;
	}

	/**
	 * Identify the file containing the specification of what the expect I/O behaviour is
	 * Format of the file is:
	 * <{input}
	 * >{ouput}
	 * #{comment}
	 * @param filename The path (relative or absolute) to 
	 */
	public void setExpected(String filename) {
		try {
			_expectedIO = new BufferedReader(new FileReader(filename));
			if (DEBUG) {
				System.err.println("Expected IO from " + filename);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("%%Message: cannot find " + filename);
		}
	}
	
	/**
	 * Identify the file from which to take input. Everytime input is requested,
	 * a line from this file will be used. Output will go to the usual place.
	 * @param filename The file containing the input.
	 */
	public void setInput(String filename) {
		KeyboardInput.setInput(filename);
	}
	
	/**
	 * Ask for input from the current input location, displaying the specified prompt
	 * to the current output location. If the MockIO object is testing against expected
	 * input, and the supplied value does not match, then an exception will be thrown.
	 * @param prompt A string prompting for the input.
	 * @return A string as supplied from the current input location.
	 * @throws TextIOAssertionException If the expected I/O does not occur.
	 */
	public String readFromKeyboard(String prompt) {
		print(prompt);
		String input = null;
		if (_expectedIO != null) {
			// Take all input from the _expectedIO string.
			try {
				// At this point, there should be no expected output. Since we may not have
				// a NL at this point, we have to check for both empty string and null
				if (_currentOutput != null && !_currentOutput.equals("")) {
					throw new TextIOAssertionException("Asking for input but still expected output from line " + _lineCount + 
							":-\nExpected:" + 
							_seenOutput + "{" + _currentOutput + "}\nInput is for : {" + prompt + "}");
				}
				_currentOutput = null;
				String read = _expectedIO.readLine();

				// Expected next line from specification but there isn't one. 
				if (read == null) {
					throw new TextIOAssertionException("No expected input provided after line " + _lineCount);
				}
				_lineCount++;
				
				if (DEBUG) {
					System.err.println("ExpectedIO - got [" + read + "]");
				}
				if (!read.startsWith("<")) {
					throw new TextIOAssertionException("Trying to provide non-input for input on line " + _lineCount + 
							":-\nGot input: {" + read + "}");  
				}
				read = read.substring(1);
				System.out.println(read);
				return read;
			} catch (IOException e) {
				System.err.println("%%Message: problem getting input");
			}
		} else {
			// Take input from the keyboard.
			input = KeyboardInput.readFromKeyboard(prompt);
			// No point recording input from expected io
			if (_recording) {
				_inputs.add(input);
			}
		}
		return input;
	}
	/**
	 * Ask for an integer input from the current input location, displaying the specified prompt
	 * plus the cancelStr to the current output location, and requiring that the input be from 
	 * the range [lower..upper]. If the supplied input matches the cancelStr, then the value
	 * cancelResult will be returned. If the supplied input is not in the required range, the
	 * request for input will be repeated.
	 * @param prompt
	 * @param lower
	 * @param upper
	 * @param cancelResult
	 * @param cancelStr
	 * @return 
	 * @throws TextIOAssertionException If the expected I/O does not occur.
	 */
	public int readInteger(String prompt, int lower, int upper, int cancelResult, String cancelStr) {
		int result;
		if (_expectedIO == null) {
			result = KeyboardInput.readInteger(prompt, lower, upper, cancelResult, cancelStr);
			if (_recording) {
				_inputs.add(""+result);
			}
			return result;
		}
		while (true) {
			String input = readFromKeyboard(prompt);
			if (DEBUG) {
				System.err.println("Got input {" + input + "}");
			}
			if (input.equals(cancelStr)) {
				result = cancelResult;
				break;
			}
			try {
				result = Integer.parseInt(input);
				// If we get here, we must have a valid int
				if (result < lower || result > upper) {
					System.out.println("%%Message: `" + input + "' must be between " + 
							lower + " and " + upper + ", or '" + cancelStr + "' to cancel");
				} else {
					break;
				}
			} catch (NumberFormatException nfex) {
				System.out.println("%%Message: `" + input + "' is not a valid response.\n" +
						"Input must be an integer between '" + lower + "' and '" + upper + "'");
			}
		}
		
		if (_recording) {
			_inputs.add(""+result);
		}
		return result;
	}
	
	/**
	 * Print to standard out without a newline. This replaces System.out.print so that
	 * checking for expected output can be done.
	 */
	public void print(String str) {
		assertOutput(str, false);
		System.out.print(str);
	}
	/**
	 * Print to standard out with a newline. This replaces System.out.println so that
	 * checking for expected output can be done.
	 */
	public void println(String str) {
		assertOutput(str, true);
		System.out.println(str);
	}
	/**
	 * Start recording inputs.
	 */
	public void record() {
		_inputs = new Vector<String>();
		_recording = true;
	}
	/**
	 * Print a record of what inputs were given.
	 */
	public void printRecord() {
		if (!_recording) {
			if (DEBUG) {
				System.err.println("No record made");
			}
			return;
		}
		for (String input: _inputs) {
			System.out.println(input);
		}
	}

	/**
	 * Compare the specified actual output matches what is expected. If eol is
	 * set, then the actual output ends the line (and so the expected output
	 * should also end the line).
	 * @param actual The output that was seen
	 * @param eol If true, then the seen output ends the line.
	 */
	private void assertOutput(String actual, boolean eol) {
			if (_expectedIO == null) {
				// No expected input.
				return;
			}
			if (_currentOutput == null) {
				getNextOutput();
			}
			if (_currentOutput == null) {
				throw new TextIOAssertionException("No more expected output after line " + _lineCount + ":-\nExpected:" + 
						_seenOutput + "<end of input>\nGot     :" + _seenOutput + "{" + actual + "}");
			}
			if (!_currentOutput.startsWith(actual)) {
				throw new TextIOAssertionException("Expected output does not match on line " + _lineCount + ":-\nExpected:" + 
						_seenOutput + "{" + _currentOutput + "}\nGot     :" + _seenOutput + "{" + actual + "}");
			}
			if (DEBUG) {
				System.err.println("Seen actual [" + actual + "]");
			}
			_seenOutput += actual;
			_currentOutput = _currentOutput.substring(actual.length());
			if (eol) {
				if (!_currentOutput.equals("")) {
					throw new TextIOAssertionException("Actual has EOL but still expecting output on line " + _lineCount + ":-\nExpected:" + 
							_seenOutput + "{" + _currentOutput + "}\nGot     :" + _seenOutput + "{\\n}");
				}
				if (DEBUG) {
					System.err.println("Matched expected newline");
				}
				_currentOutput = null;
			}
		}

	private void getNextOutput() {
		if (_expectedIO == null) {
			return;
		}
		if (_currentOutput != null) {
			throw new RuntimeException("Getting next output with {" + _currentOutput + "} remaining");
		}
		while (true) {
			try {
				String read = _expectedIO.readLine();
				if (read == null) {
					_seenOutput = "";
					return; // no more expected output
				}
				_lineCount++;
				if (DEBUG) {
					System.err.println("ExpectedIO (getNextOutput) - got [" + read + "]");
				}
				if (read.startsWith("#")) {
					continue;
				}
				if (!read.startsWith(">")) {
					throw new RuntimeException("Trying to provide {" + read + "} as output");
				}
				_currentOutput = read.substring(1); // strip off the ">" indicating output
				_seenOutput = "";
			} catch (IOException e) {
				throw new TextIOAssertionException("Failed to get next line of specification - last line was line " + _lineCount);
			}
			break;
		}
	}

	/**
	 * Indicate that all IO should be finished, so expect no more output or input.
	 */
	public void finished() {
		// Anything left on the current line?
		if (_currentOutput != null && !_currentOutput.equals("")) {
			throw new TextIOAssertionException("Finished, but still expecting more output on line " + _lineCount + ":-\nExpected:" + 
					_seenOutput + "{" + _currentOutput + "}\nGot     :" + _seenOutput + "<end of input>");
		}
		try {
			// Any more input? If yes, then this next bit will complain because it wants output
			getNextOutput();
		} catch (TextIOAssertionException tioae) {
			// This will happen if no more input, so correct behaviour
		}
		// Check to see if got more expected output
		if (_currentOutput != null && !_currentOutput.equals("")) {
			throw new TextIOAssertionException("Finished, but still expecting output from line " + _lineCount + ":-\nExpected:" + 
					_seenOutput + "{" + _currentOutput + "}\nGot     :" + _seenOutput + "<end of input>");
		}
	}
}
