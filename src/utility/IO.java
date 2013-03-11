package utility;

public interface IO {
	String readFromKeyboard(String prompt);
	int readInteger(String prompt, int lower, int upper, int cancelResult, String cancelStr);
	void print(String str);
	void println(String str);
}
