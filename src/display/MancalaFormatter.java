package display;

import java.util.HashMap;

import mancala.Board;
import mancala.Player;

public interface MancalaFormatter {
	public void displayBoard(Board board);

	public void displayScores(HashMap<Player, Integer> scores);
}
