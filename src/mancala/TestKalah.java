package mancala;

import junit.framework.TestCase;
import utility.MockIO;

public class TestKalah extends TestCase {
	
	private MockIO _mockIO;
	public void setUp() {
		_mockIO = new MockIO();
	}
	public void tearDown() {
		_mockIO.finished();
	}

	public void testSimpleStart() {
		playGame("test/simple_start.txt");
	}
	public void testP1Continue() {
		playGame("test/p1_continue.txt");
	}
	public void testSimpleTwoMoves() {
		playGame("test/simple_two_moves.txt");
	}
	public void testSingleWrap() {
		playGame("test/single_wrap.txt");
	}
	public void testContinueWrap() {
		playGame("test/continue_wrap.txt");
	}


	/**
	 * Player 1 wins
	 */
	public void testFullGame1() {
		playGame("test/full_game1.txt");
	}

	/**
	 * Player 2 wins
	 */
	public void testFullGame2() {
		playGame("test/full_game2.txt");
	}

	/**
	 * Player 2 wins by moving the last seed into its store
	 */
	public void testFullGameStore() {
		playGame("test/full_game_store.txt");
	}

	/**
	 * Player 2 wins by moving the last seed into its store
	 */
	public void testFullGameEmpty() {
		playGame("test/full_game_empty.txt");
	}

	/**
	 * Tie
	 */
	public void testFullGameTie() {
		playGame("test/full_game_tie.txt");
	}
	
	/*
	 * Various non-standard or rare situations
	 */
	
	public void testUseEmpty() {
		playGame("test/useempty.txt");
	}

	public void testWrapped() {
		playGame("test/wrapped.txt");
	}

	private void playGame(String spec) {
		_mockIO.setExpected(spec);
		new Mancala().play(_mockIO);
	}
}
