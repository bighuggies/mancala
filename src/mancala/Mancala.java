package mancala;

import utility.MockIO;

/**
 * This class is the starting point for SOFTENG 701 Assignment 1.1 in 2013.
 */
public class Mancala {
	public static void main(String[] args) {
		new Mancala().play(new MockIO());
	}

	public void play(MockIO io) {
		Player[] players = { new Player("1", 7), new Player("2", 7) };
		int currentPlayer = 0;

		while (true) {
			Player player = players[currentPlayer];

			int command = io.readInteger(">Player " + player.getName()
					+ "'s turn - Specify house number or 'q' to quit:\n<", 1,
					6, -1, "q");

			if (command == -1)
				break;

			currentPlayer = currentPlayer == 0 ? 1 : 1;
		}
	}

	private boolean checkWon(int[] board) {
		for (int i = 1; i < 7; i++) {
			if (board[i] != 0)
				return false;
		}

		return true;
	}
}
