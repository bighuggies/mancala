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
	private Events _dispatcher;
	private int[] _pieces;

	public Board(Events dispatcher) {
		dispatcher.listen(CommandEvent.class, this);
		dispatcher.listen(StealEvent.class, this);

		_dispatcher = dispatcher;
		_pieces = new int[Config.TOTAL_PIECES];

		Arrays.fill(_pieces, Config.SEEDS_PER_HOUSE);

		for (int i = 1; i <= Config.NUM_PLAYERS; i++) {
			_pieces[i * Config.PIECES_PER_PLAYER - 1] = Config.SEEDS_PER_STORE;
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
			_dispatcher.notify(new EndedInStoreEvent(0, fromPlayer));
		}

		// Check for steals
		if (_pieces[pieceIndex] == 1 && isPlayerHouse(pieceIndex, fromPlayer)) {
			_dispatcher.notify(new StealEvent(pieceIndex
					% Config.PIECES_PER_PLAYER, fromPlayer));
		}
	}

	private boolean isPlayerHouse(int pieceIndex, int playerNumber) {
		return isHouse(pieceIndex) && isPlayerPiece(pieceIndex, playerNumber);
	}

	private boolean isPlayerStore(int pieceIndex, int playerNumber) {
		return isStore(pieceIndex) && isPlayerPiece(pieceIndex, playerNumber);
	}

	private boolean isPlayerPiece(int pieceIndex, int playerNumber) {
		return (pieceIndex >= playerNumber * Config.PIECES_PER_PLAYER && pieceIndex < (playerNumber + 1)
				* Config.PIECES_PER_PLAYER);
	}

	private boolean isHouse(int pieceIndex) {
		return !isStore(pieceIndex);
	}

	private boolean isStore(int pieceIndex) {
		return pieceIndex % Config.PIECES_PER_PLAYER >= Config.HOUSES_PER_PLAYER;
	}

	@SuppressWarnings("unused")
	private int ownerOfPiece(int pieceIndex) {
		for (int i = 0; i < Config.NUM_PLAYERS; i++) {
			if (i < (i + 1) * Config.PIECES_PER_PLAYER) {
				return i;
			}
		}

		return -1;
	}

	private int getPlayerStore(int playerNumber, int storeNumber) {
		return ((playerNumber * Config.PIECES_PER_PLAYER) + Config.HOUSES_PER_PLAYER)
				+ storeNumber;
	}

	public int[] getPlayerStores(int playerNumber) {
		int lastPiece = (playerNumber + 1) * Config.PIECES_PER_PLAYER;
		int firstStore = lastPiece - Config.STORES_PER_PLAYER;

		return Arrays.copyOfRange(_pieces, firstStore, lastPiece);
	}

	public int[] getPlayerHouses(int playerNumber) {
		return Arrays.copyOfRange(_pieces, playerNumber
				* Config.PIECES_PER_PLAYER,
				((playerNumber + 1) * Config.PIECES_PER_PLAYER)
						- Config.STORES_PER_PLAYER);
	}

	public int[] getScores() {
		int[] scores = new int[Config.NUM_PLAYERS];

		for (int i = 0; i < Config.NUM_PLAYERS; i++) {
			int score = 0;
			int firstPiece = i * Config.PIECES_PER_PLAYER;
			int lastPiece = firstPiece + Config.PIECES_PER_PLAYER;

			for (int j = firstPiece; j < lastPiece; j++) {
				score += _pieces[j];
			}

			scores[i] = score;
		}

		return scores;
	}

	private boolean playerHousesEmpty(int playerIndex) {
		int firstHouse = playerIndex * Config.PIECES_PER_PLAYER;

		for (int i = firstHouse; i < firstHouse + Config.HOUSES_PER_PLAYER; i++) {
			if (_pieces[i] > 0) {
				return false;
			}
		}

		return true;
	}

	private int getOppositeHouse(int playerNumber, int houseNumber) {
		int offset = Config.PIECES_PER_PLAYER - Config.STORES_PER_PLAYER
				- houseNumber;

		return (((playerNumber + 1) % Config.NUM_PLAYERS) * Config.PIECES_PER_PLAYER)
				+ offset - 1;
	}

	private boolean verifyCommand(CommandEvent command) {
		int pieceIndex = (command.playerNumber * Config.PIECES_PER_PLAYER)
				+ command.houseNumber;

		return _pieces[pieceIndex] > 0;
	}

	@Override
	public void onPlayerIssuedCommand(CommandEvent command) {
		if (!verifyCommand(command)) {
			_dispatcher.notify(new BadCommandEvent(command.playerNumber));
			return;
		}

		int fromPlayer = command.playerNumber;

		distributeSeeds((fromPlayer * Config.PIECES_PER_PLAYER)
				+ command.houseNumber, fromPlayer);

		if (playerHousesEmpty(command.playerNumber)) {
			_dispatcher.notify(new GameEndEvent(Reason.FINISHED));
		}
	}

	@Override
	public void onStealMove(StealEvent stealEvent) {
		int oppositeHouse = getOppositeHouse(stealEvent.stealingPlayerNumber,
				stealEvent.stealingHouseNumber);
		int stealingHouse = (stealEvent.stealingPlayerNumber * Config.PIECES_PER_PLAYER)
				+ stealEvent.stealingHouseNumber;

		int seeds = _pieces[oppositeHouse] + _pieces[stealingHouse];

		_pieces[oppositeHouse] = 0;
		_pieces[stealingHouse] = 0;
		_pieces[getPlayerStore(stealEvent.stealingPlayerNumber, 0)] += seeds;
	}
}
