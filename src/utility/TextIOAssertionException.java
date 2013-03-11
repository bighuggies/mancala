package utility;

/**
 * Represents the situation where the actual output does not match
 * the expected output.
 */
@SuppressWarnings("serial")
public class TextIOAssertionException extends RuntimeException {
	public TextIOAssertionException(String message) {
		super(message);
	}
}
