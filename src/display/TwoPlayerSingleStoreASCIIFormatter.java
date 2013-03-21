package display;

import mancala.Board;

public class TwoPlayerSingleStoreASCIIFormatter implements MancalaFormatter {
	@Override
	public void display(Board board) {
		printHorizontalBorder(board.HOUSES_PER_PLAYER);

		printPlayer1("P2", board.getPlayerHouses(2),
				board.getPlayerStores(1)[0]);
		printPlayerDivider();
		printPlayer2("P1", board.getPlayerHouses(1),
				board.getPlayerStores(2)[0]);

		printHorizontalBorder(board.HOUSES_PER_PLAYER);
	}

	private void printPlayerDivider() {
		System.out
				.println("|    |-------+-------+-------+-------+-------+-------|    |");
	}

	private void printPlayer1(String playerName, int[] playerHouses,
			int playerStore) {
		System.out.print("| " + playerName + " | ");

		for (int i = playerHouses.length; i >= 0; i--) {
			System.out.print((i + 1) + "[ 1] | ");
		}

		System.out.print(playerStore + " |");
	}

	private void printPlayer2(String playerName, int[] playerHouses,
			int playerStore) {
		System.out.print("| " + playerStore + " | ");

		for (int i = playerHouses.length; i >= 0; i--) {
			System.out.print(String.format("%d[%2d] |", (i + 1),
					playerHouses[i]));
		}

		System.out.print(playerName + " |");
	}

	public void printHorizontalBorder(int numPits) {
		System.out.print("+----+");

		for (int i = 0; i < numPits; i++) {
			System.out.print("-------+");
		}

		System.out.print("----+");
	}
}
