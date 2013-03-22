package display;

import mancala.Board;

public interface MancalaFormatter {
	public void displayBoard(Board board);

	public void displayScores(int[] scores);
}
