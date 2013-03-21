package display;

import mancala.Board;
import utility.IO;

public class TwoPlayerSingleStoreASCIIFormatter implements MancalaFormatter {
	private IO io;

	public TwoPlayerSingleStoreASCIIFormatter(IO io) {
		this.io = io;
	}

	public void display(Board board) {
		printHorizontalBorder(board.HOUSES_PER_PLAYER);

		printPlayer2("P2", board.getPlayerHouses(1),
				board.getPlayerStores(0)[0]);
		printPlayerDivider();
		printPlayer1("P1", board.getPlayerHouses(0),
				board.getPlayerStores(1)[0]);

		printHorizontalBorder(board.HOUSES_PER_PLAYER);
	}

	private void printPlayerDivider() {
		io.println(">|    |-------+-------+-------+-------+-------+-------|    |");
	}

	private void printPlayer2(String playerName, int[] playerHouses,
			int playerStore) {
		io.print(String.format(">| %s |", playerName));
		
			
		for (int i = playerHouses.length - 1; i >= 0; i--) {
			io.print(String.format(" %d[%2d] |", (i + 1), playerHouses[i]));
		}

		io.print(String.format(" %2d |\n", playerStore));
	}
	
	private void printPlayer1(String playerName, int[] playerHouses,
			int playerStore) {
		io.print(String.format(">| %2d |", playerStore));
		
		for (int i = 0; i < playerHouses.length; i++) {
			io.print(String.format(" %d[%2d] |", (i + 1), playerHouses[i]));
		}
		
		io.print(String.format(" %s |\n", playerName));
	}

	public void printHorizontalBorder(int numPits) {
		io.print(">+----+");

		for (int i = 0; i < numPits; i++) {
			io.print("-------+");
		}

		io.print("----+\n");
	}
}
