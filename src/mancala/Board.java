package mancala;

import java.util.Arrays;

import events.CommandEvent;
import events.CommandListener;
import events.Events;
import events.StealEvent;
import events.StealListener;

public class Board implements CommandListener, StealListener {
	private static final int NUM_PLAYERS = 2;
	private static final int HOUSES_PER_PLAYER = 6;
	private static final int STORES_PER_PLAYER = 1;
	private static final int SEEDS_PER_HOUSE = 4;
	private static final int SEEDS_PER_STORE = 0;

	private Events _dispatcher;
	private int[] _pieces;

	public Board(Events dispatcher) {
		dispatcher.listen(CommandEvent.class, this);

		int piecesPerPlayer = HOUSES_PER_PLAYER + STORES_PER_PLAYER;
		int totalPieces = (NUM_PLAYERS * HOUSES_PER_PLAYER)
				+ (NUM_PLAYERS * STORES_PER_PLAYER);

		_dispatcher = dispatcher;
		_pieces = new int[totalPieces];

		Arrays.fill(_pieces, SEEDS_PER_HOUSE);

		for (int i = 1; i <= NUM_PLAYERS; i++) {
			_pieces[i * piecesPerPlayer - 1] = SEEDS_PER_STORE;
		}
	}

	private void distributeSeeds(int fromHouse, int fromPlayer) {
		int numSeeds = _pieces[fromHouse];
		int currentHouse = fromHouse + 1;

		while (numSeeds > 0) {
			currentHouse++;

			if (currentHouse > _pieces.length - 1) {
				currentHouse = 0;
			}

			if (isHouse(currentHouse)
					|| isPlayerStore(currentHouse, fromPlayer)) {
				_pieces[currentHouse]++;
				numSeeds--;
			}
		}

		if (_pieces[currentHouse] == 1
				&& isPlayerHouse(currentHouse, fromPlayer)) {
			_dispatcher.notify(this, new StealEvent(currentHouse, fromPlayer));
		}
	}

	private boolean isPlayerHouse(int pieceIndex, int playerNumber) {
		return (isHouse(pieceIndex) && isPlayersPiece(pieceIndex, playerNumber));
	}

	private boolean isPlayerStore(int pieceIndex, int playerNumber) {
		return (playerNumber + 1 * 7 - 1) == pieceIndex;
	}

	private boolean isPlayersPiece(int pieceIndex, int playerNumber) {
		return (pieceIndex >= playerNumber * 7 && pieceIndex < playerNumber + 1 * 7)
	}

	private boolean isHouse(int pieceIndex) {
		return !isStore(pieceIndex);
	}

	private boolean isStore(int pieceIndex) {
		return pieceIndex % 7 == 6;
	}

	private int getOppositeHouse(int houseIndex) {

	}

	private boolean verifyCommand(CommandEvent command) {
		return true;
	}

	@Override
	public void onPlayerIssuedCommand(Mancala gameContext, CommandEvent command) {
		if (!verifyCommand(command))
			throw new IllegalArgumentException();

		int fromPlayer = gameContext.getCurrentPlayer().number;
		int startIndex = fromPlayer * 7;

		distributeSeeds(command.command + startIndex, fromPlayer);
	}

	@Override
	public void onStealMove(Board boardContext, StealEvent stealEvent) {

	}
}
