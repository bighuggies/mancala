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

		printPlayer1("P2", board.getPlayerHouses(2),
				board.getPlayerStores(0)[0]);
		printPlayerDivider();
		printPlayer2("P1", board.getPlayerHouses(1),
				board.getPlayerStores(1)[0]);

		printHorizontalBorder(board.HOUSES_PER_PLAYER);
	}

	private void printPlayerDivider() {
		io.println("|    |-------+-------+-------+-------+-------+-------|    |");
	}

	private void printPlayer2(String playerName, int[] playerHouses,
			int playerStore) {
		io.print(String.format("| %2d | ", playerStore));
	
		for (int i = playerHouses.length; i >= 0; i--) {
			io.print(String.format("%d[%2d] |", (i + 1), playerHouses[i]));
		}

		io.print(playerName + " |\n");
	}
	
	private void printPlayer1(String playerName, int[] playerHouses,
			int playerStore) {
		io.print("| " + playerName + " | ");

		for (int i = playerHouses.length; i >= 0; i--) {
			io.print((i + 1) + "[ 1] | ");
		}

		io.print(String.format("%2d |\n", playerStore));
	}

	public void printHorizontalBorder(int numPits) {
		io.print("+----+");

		for (int i = 0; i < numPits; i++) {
			io.print("-------+");
		}

		io.print("----+\n");
	}
}
