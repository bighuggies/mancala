package display;

import java.util.HashMap;
import java.util.Map.Entry;

import mancala.Board;
import mancala.Player;
import utility.IO;

public class TwoPlayerSingleStoreASCIIFormatter implements MancalaFormatter {
	private IO io;

	public TwoPlayerSingleStoreASCIIFormatter(IO io) {
		this.io = io;
	}

	public void displayBoard(Board board) {
		printHorizontalBorder(board.HOUSES_PER_PLAYER);

		printPlayer2("P2", board.getPlayerHouses(1),
				board.getPlayerStores(0)[0]);
		printPlayerDivider();
		printPlayer1("P1", board.getPlayerHouses(0),
				board.getPlayerStores(1)[0]);

		printHorizontalBorder(board.HOUSES_PER_PLAYER);
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
	public void displayScores(HashMap<Player, Integer> scores) {
		Player winner = null;
		int bestScore = 0;

		for (Entry<Player, Integer> e : scores.entrySet()) {
			io.println("\tplayer " + e.getKey().name + ":" + e.getValue());

			// ugly
			if (e.getValue() > bestScore) {
				bestScore = e.getValue();
				winner = e.getKey();
			} else if (e.getValue() == bestScore) {
				winner = null;
			}
		}

		if (winner == null) {
			io.println("A tie!");
		} else {
			io.println("Player " + winner.name + " wins!");
		}
	}
}
