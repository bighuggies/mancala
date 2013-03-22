package mancala;

public class Config {
	public static final int NUM_PLAYERS = 2;
	public static final int HOUSES_PER_PLAYER = 6;
	public static final int STORES_PER_PLAYER = 1;
	public static final int PIECES_PER_PLAYER = HOUSES_PER_PLAYER
			+ STORES_PER_PLAYER;
	public static final int TOTAL_PIECES = NUM_PLAYERS * PIECES_PER_PLAYER;
	public static final int SEEDS_PER_HOUSE = 4;
	public static final int SEEDS_PER_STORE = 0;
}
