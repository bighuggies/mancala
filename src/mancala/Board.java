package mancala;

import java.util.Arrays;

import events.CommandEvent;
import events.CommandListener;
import events.Events;
import events.StealEvent;
import events.StealListener;

public class Board implements CommandListener, StealListener {
	public final int NUM_PLAYERS = 2;
	public final int HOUSES_PER_PLAYER = 6;
	public final int STORES_PER_PLAYER = 1;
	public final int PIECES_PER_PLAYER = HOUSES_PER_PLAYER + STORES_PER_PLAYER;
	public final int TOTAL_PIECES = NUM_PLAYERS * PIECES_PER_PLAYER;
	public final int SEEDS_PER_HOUSE = 4;
	public final int SEEDS_PER_STORE = 0;

	private Events _dispatcher;
	private int[] _pieces;

	public Board(Events dispatcher) {
		dispatcher.listen(CommandEvent.class, this);
		dispatcher.listen(StealEvent.class, this);

		_dispatcher = dispatcher;
		_pieces = new int[TOTAL_PIECES];

		Arrays.fill(_pieces, SEEDS_PER_HOUSE);

		for (int i = 1; i <= NUM_PLAYERS; i++) {
			_pieces[i * PIECES_PER_PLAYER - 1] = SEEDS_PER_STORE;
		}
	}

	private void distributeSeeds(int fromHouse, int fromPlayer) {
		int numSeeds = _pieces[fromHouse];
		int currentHouse = fromHouse;

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
		return isHouse(pieceIndex) && isPlayerPiece(pieceIndex, playerNumber);
	}

	private boolean isPlayerStore(int pieceIndex, int playerNumber) {
		return isStore(pieceIndex) && isPlayerPiece(pieceIndex, playerNumber);
	}

	private boolean isPlayerPiece(int pieceIndex, int playerNumber) {
		return (pieceIndex >= playerNumber * PIECES_PER_PLAYER && pieceIndex < playerNumber
				+ 1 * PIECES_PER_PLAYER);
	}

	private boolean isHouse(int pieceIndex) {
		return !isStore(pieceIndex);
	}

	private boolean isStore(int pieceIndex) {
		return pieceIndex % PIECES_PER_PLAYER >= HOUSES_PER_PLAYER;
	}

	@SuppressWarnings("unused")
	private int ownerOfPiece(int pieceIndex) {
		for (int i = 0; i < NUM_PLAYERS; i++) {
			if (i < (i + 1) * PIECES_PER_PLAYER) {
				return i;
			}
		}

		return -1;
	}

	private int getPlayerStore(int playerNumber, int storeNumber) {
		return ((playerNumber * PIECES_PER_PLAYER) + HOUSES_PER_PLAYER)
				+ storeNumber;
	}

	public int[] getPlayerStores(int playerNumber) {
		return Arrays.copyOfRange(_pieces, playerNumber * PIECES_PER_PLAYER,
				playerNumber + 1 * PIECES_PER_PLAYER);
	}

	public int[] getPlayerHouses(int playerNumber) {
		return new int[] { 0, 1 };
	}

	private int getOppositeHouse(int playerNumber, int houseIndex) {
		int offset = PIECES_PER_PLAYER - (houseIndex % PIECES_PER_PLAYER);

		return ((playerNumber + 1 % NUM_PLAYERS) * PIECES_PER_PLAYER) + offset;
	}

	private boolean verifyCommand(CommandEvent command) {
		int pieceIndex = (command.player.number * PIECES_PER_PLAYER)
				+ command.houseIndex;

		return isPlayerHouse(pieceIndex, command.player.number);
	}

	@Override
	public void onPlayerIssuedCommand(Mancala gameContext, CommandEvent command) {
		if (!verifyCommand(command))
			throw new IllegalArgumentException();

		int fromPlayer = command.player.number;

		distributeSeeds((fromPlayer * PIECES_PER_PLAYER) + command.houseIndex,
				fromPlayer);
	}

	@Override
	public void onStealMove(Board boardContext, StealEvent stealEvent) {
		int oppositeHouse = getOppositeHouse(stealEvent.stealingPlayerNumber,
				stealEvent.stealingHouseNumber);
		int seeds = _pieces[oppositeHouse]
				+ _pieces[stealEvent.stealingHouseNumber];

		_pieces[oppositeHouse] = 0;
		_pieces[stealEvent.stealingHouseNumber] = 0;
		_pieces[getPlayerStore(stealEvent.stealingPlayerNumber, 0)] += seeds;
	}
}
