package display;

import mancala.Board;
import mancala.Config;
import utility.IO;

public class TwoPlayerSingleStoreASCIIFormatter implements MancalaFormatter {
	private IO io;

	public TwoPlayerSingleStoreASCIIFormatter(IO io) {
		this.io = io;
	}

	public void displayBoard(Board board) {
		printHorizontalBorder(Config.HOUSES_PER_PLAYER);

		printPlayer2("P2", board.getPlayerHouses(1),
				board.getPlayerStores(0)[0]);
		printPlayerDivider();
		printPlayer1("P1", board.getPlayerHouses(0),
				board.getPlayerStores(1)[0]);

		printHorizontalBorder(Config.HOUSES_PER_PLAYER);
	}

	private void printPlayerDivider() {
		io.println("|    |-------+-------+-------+-------+-------+-------|    |");
	}

	private void printPlayer2(String playerName, int[] playerHouses,
			int playerStore) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("| %s |", playerName));

		for (int i = playerHouses.length - 1; i >= 0; i--) {
			sb.append(String.format(" %d[%2d] |", (i + 1), playerHouses[i]));
		}

		sb.append(String.format(" %2d |", playerStore));
		io.println(sb.toString());
	}

	private void printPlayer1(String playerName, int[] playerHouses,
			int playerStore) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("| %2d |", playerStore));

		for (int i = 0; i < playerHouses.length; i++) {
			sb.append(String.format(" %d[%2d] |", (i + 1), playerHouses[i]));
		}

		sb.append(String.format(" %s |", playerName));
		io.println(sb.toString());
	}

	public void printHorizontalBorder(int numPits) {
		StringBuilder sb = new StringBuilder();

		sb.append("+----+");

		for (int i = 0; i < numPits; i++) {
			sb.append("-------+");
		}

		sb.append("----+");
		io.println(sb.toString());
	}

	@Override
	public void displayScores(int[] scores) {
		int winner = -1;
		int bestScore = 0;

		for (int i = 0; i < scores.length; i++) {
			io.println("\tplayer " + (i + 1) + ":" + scores[i]);

			if (scores[i] > bestScore) {
				bestScore = scores[i];
				winner = i;
			} else if (scores[i] == bestScore) {
				winner = -1;
			}
		}

		if (winner == -1) {
			io.println("A tie!");
		} else {
			io.println("Player " + (winner + 1) + " wins!");
		}
	}
}
