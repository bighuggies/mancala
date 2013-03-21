package mancala;

import java.util.Arrays;

import events.BadCommandEvent;
import events.CommandEvent;
import events.CommandListener;
import events.EndedInStoreEvent;
import events.Events;
import events.GameEndEvent;
import events.GameEndEvent.Reason;
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
		_pieces[fromHouse] = 0;
		int pieceIndex = fromHouse;

		while (numSeeds > 0) {
			pieceIndex++;

			if (pieceIndex == _pieces.length) {
				pieceIndex = 0;
			}

			if (isHouse(pieceIndex) || isPlayerStore(pieceIndex, fromPlayer)) {
				_pieces[pieceIndex]++;
				numSeeds--;
			}
		}

		// Check for turn continuations
		if (isPlayerStore(pieceIndex, fromPlayer)) {
			_dispatcher.notify(this, new EndedInStoreEvent(0, fromPlayer));
		}

		// Check for steals
		if (_pieces[pieceIndex] == 1 && isPlayerHouse(pieceIndex, fromPlayer)) {
			_dispatcher.notify(this, new StealEvent(pieceIndex
					% PIECES_PER_PLAYER, fromPlayer));
		}
	}

	private boolean isPlayerHouse(int pieceIndex, int playerNumber) {
		return isHouse(pieceIndex) && isPlayerPiece(pieceIndex, playerNumber);
	}

	private boolean isPlayerStore(int pieceIndex, int playerNumber) {
		return isStore(pieceIndex) && isPlayerPiece(pieceIndex, playerNumber);
	}

	private boolean isPlayerPiece(int pieceIndex, int playerNumber) {
		return (pieceIndex >= playerNumber * PIECES_PER_PLAYER && pieceIndex < (playerNumber + 1)
				* PIECES_PER_PLAYER);
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
		int lastPiece = (playerNumber + 1) * PIECES_PER_PLAYER;
		int firstStore = lastPiece - STORES_PER_PLAYER;

		return Arrays.copyOfRange(_pieces, firstStore, lastPiece);
	}

	public int[] getPlayerHouses(int playerNumber) {
		return Arrays.copyOfRange(_pieces, playerNumber * PIECES_PER_PLAYER,
				((playerNumber + 1) * PIECES_PER_PLAYER) - STORES_PER_PLAYER);
	}

	public int[] getScores() {
		int[] scores = new int[NUM_PLAYERS];

		for (int i = 0; i < NUM_PLAYERS; i++) {
			int score = 0;
			int firstPiece = i * PIECES_PER_PLAYER;
			int lastPiece = firstPiece + PIECES_PER_PLAYER;

			for (int j = firstPiece; j < lastPiece; j++) {
				score += _pieces[j];
			}

			scores[i] = score;
		}

		return scores;
	}

	private int getOppositeHouse(int playerNumber, int houseNumber) {
		int offset = PIECES_PER_PLAYER - STORES_PER_PLAYER - houseNumber;

		return (((playerNumber + 1) % NUM_PLAYERS) * PIECES_PER_PLAYER)
				+ offset - 1;
	}

	private boolean verifyCommand(CommandEvent command) {
		int pieceIndex = (command.player.number * PIECES_PER_PLAYER)
				+ command.houseIndex;

		return _pieces[pieceIndex] > 0;
	}

	@Override
	public void onPlayerIssuedCommand(Mancala gameContext, CommandEvent command) {
		if (!verifyCommand(command)) {
			_dispatcher.notify(this, new BadCommandEvent(command.player));
			return;
		}

		int fromPlayer = command.player.number;

		distributeSeeds((fromPlayer * PIECES_PER_PLAYER) + command.houseIndex,
				fromPlayer);

		if (playerHousesEmpty(command.player.number)) {
			_dispatcher.notify(this, new GameEndEvent(Reason.FINISHED));
		}
	}

	private boolean playerHousesEmpty(int playerIndex) {
		int firstHouse = playerIndex * PIECES_PER_PLAYER;

		for (int i = firstHouse; i < firstHouse + HOUSES_PER_PLAYER; i++) {
			if (_pieces[i] > 0) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void onStealMove(Board boardContext, StealEvent stealEvent) {
		int oppositeHouse = getOppositeHouse(stealEvent.stealingPlayerNumber,
				stealEvent.stealingHouseNumber);
		int stealingHouse = (stealEvent.stealingPlayerNumber * PIECES_PER_PLAYER)
				+ stealEvent.stealingHouseNumber;

		// System.out.println("House opposite to "
		// + (stealEvent.stealingHouseNumber + 1) + " is " + ((oppositeHouse %
		// PIECES_PER_PLAYER) + 1));

		int seeds = _pieces[oppositeHouse] + _pieces[stealingHouse];

		_pieces[oppositeHouse] = 0;
		_pieces[stealingHouse] = 0;
		_pieces[getPlayerStore(stealEvent.stealingPlayerNumber, 0)] += seeds;
	}
}
