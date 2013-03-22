package mancala;

import utility.MockIO;
import display.MancalaFormatter;
import display.TwoPlayerSingleStoreASCIIFormatter;
import events.BadCommandEvent;
import events.BadCommandListener;
import events.CommandEvent;
import events.EndedInStoreEvent;
import events.EndedInStoreListener;
import events.Events;
import events.GameEndEvent;
import events.GameStartEvent;
import events.GameEndEvent.Reason;
import events.GameEndListener;
import events.TurnEndEvent;
import events.TurnEndListener;

/**
 * This class is the starting point for SOFTENG 701 Assignment 1.1 in 2013.
 */
public class Mancala implements BadCommandListener, EndedInStoreListener,
		TurnEndListener, GameEndListener {
	private Board _board;
	private int[] _players;
	private Events _events;

	private MockIO _io;
	private MancalaFormatter _printer;

	private boolean _playing = true;
	private int _currentPlayer;
	private int _nextPlayerOverride = -1;

	public static void main(String[] args) {
		new Mancala().play(new MockIO());
	}

	public void play(MockIO io) {
		_events = new Events();
		_io = io;

		// Listen for game quit events, turn end events
		_events.listen(TurnEndEvent.class, this);
		_events.listen(EndedInStoreEvent.class, this);
		_events.listen(GameEndEvent.class, this);
		_events.listen(BadCommandEvent.class, this);

		createGameObjects();

		_players = new int[Config.NUM_PLAYERS];
		_currentPlayer = 0;

		_events.notify(new GameStartEvent(_board));

		// Play the game
		while (_playing) {
			nextTurn();
		}
	}
	
	private void createGameObjects() {
		_board = new Board();
		_printer = new TwoPlayerSingleStoreASCIIFormatter(_io);
		
		addGameObject(_board);
		addGameObject(_printer);
	}
	
	public void addGameObject(GameObject object) {
		object.setEvents(_events);
	}

	private void nextTurn() {
		String prompt = "Player " + (_currentPlayer + 1)
				+ "'s turn - Specify house number or 'q' to quit: ";
		int command = _io.readInteger(prompt, 1, 6, -1, "q");

		if (command == -1) {
			_events.notify(new GameEndEvent(Reason.QUITTING, _board));
		} else {
			_events.notify(new CommandEvent(_currentPlayer, command));			
		}

		if (this._playing) {
			_events.notify(new TurnEndEvent(_board, _currentPlayer));
		}
	}

	private int getNextPlayer(int currentPlayer) {
		if (_nextPlayerOverride != -1) {
			int nextPlayer = _nextPlayerOverride;
			_nextPlayerOverride = -1;
			return nextPlayer;
		}

		return (currentPlayer + 1) % this._players.length;
	}

	private void overrideNextPlayer(int playerNumber) {
		_nextPlayerOverride = playerNumber;
	}
	
	@Override
	public void onEndedInStore(EndedInStoreEvent event) {
		overrideNextPlayer(event.playerNumber);
	}

	@Override
	public void onTurnEnd(TurnEndEvent event) {
		if (_playing) {
			_currentPlayer = getNextPlayer(_currentPlayer);
		}
	}

	@Override
	public void onGameEnd(GameEndEvent event) {
		this._playing = false;
		_io.println("Game over");
	}

	@Override
	public void onBadCommand(BadCommandEvent badCommand) {
		_io.println("House is empty. Move again.");
		overrideNextPlayer(badCommand.sourcePlayer);
	}
}
