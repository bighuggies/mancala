package mancala;

import java.util.Arrays;

import utility.MockIO;

/**
 * This class is the starting point for SOFTENG 701 Assignment 1.1 in 2013.
 */
public class Mancala {
	public static void main(String[] args) {
		new Mancala().play(new MockIO());
	}

	public void play(MockIO io) {
		int[] player1 = new int[7];
		int[] player2 = new int[7];
		
		Arrays.fill(player1, 4);
		Arrays.fill(player2, 4);
		
		player1[0] = 0;
		player2[0] = 0;
		
		int[][] players = {player1, player2};
		int[] currentPlayer = player1;
		int[] otherPlayer = player2;

		int turn = 0;
				
		while(true) {	
			if (checkWon(currentPlayer))
				break;
			
			int command = io.readInteger(">Player " + (turn + 1) + "'s turn - Specify house number or 'q' to quit:\n<", 1, 6, -1, "q");
			
			if (command == -1)
				break;
			
			int seeds = currentPlayer[command];
			currentPlayer[command] = 0;
			
			
			otherPlayer = currentPlayer;
			turn = turn == 0 ? 1 : 1;
			currentPlayer = players[turn];
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
