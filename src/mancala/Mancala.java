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
import events.GameEndEvent.Reason;
import events.GameEndListener;
import events.TurnEndEvent;
import events.TurnEndListener;

/**
 * This class is the starting point for SOFTENG 701 Assignment 1.1 in 2013.
 */
public class Mancala implements BadCommandListener, EndedInStoreListener,
		TurnEndListener, GameEndListener {
	public final Board board;
	public final int[] players;

	private MockIO io;
	private Events dispatcher;
	private MancalaFormatter printer;
	private boolean playing = true;
	private Reason gameEndReason;

	private int currentPlayer;
	private int nextPlayerOverride = -1;

	public static void main(String[] args) {
		new Mancala().play(new MockIO());
	}

	public Mancala() {
		dispatcher = new Events();

		// Listen for game quit events, turn end events
		dispatcher.listen(TurnEndEvent.class, this);
		dispatcher.listen(EndedInStoreEvent.class, this);
		dispatcher.listen(GameEndEvent.class, this);
		dispatcher.listen(BadCommandEvent.class, this);

		board = new Board(dispatcher);

		players = new int[Config.NUM_PLAYERS];
		currentPlayer = 0;
	}

	public void play(MockIO io) {
		printer = new TwoPlayerSingleStoreASCIIFormatter(io);
		this.io = io;
		this.printer.displayBoard(board);

		while (playing) {
			nextTurn();
		}

		io.println("Game over");
		printer.displayBoard(board);

		if (gameEndReason == Reason.FINISHED) {
			this.printer.displayScores(board.getScores());
		}
	}

	public void nextTurn() {
		String prompt = "Player " + (currentPlayer + 1)
				+ "'s turn - Specify house number or 'q' to quit: ";
		int command = io.readInteger(prompt, 1, 6, -1, "q");

		if (command == -1) {
			dispatcher.notify(new GameEndEvent(Reason.QUITTING));
			return;
		}

		dispatcher.notify(new CommandEvent(currentPlayer, command));
		dispatcher.notify(new TurnEndEvent(currentPlayer));
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public int getNextPlayer(int currentPlayer) {
		if (nextPlayerOverride != -1) {
			int nextPlayer = nextPlayerOverride;
			nextPlayerOverride = -1;
			return nextPlayer;
		}

		return (currentPlayer + 1) % this.players.length;
	}

	public void overrideNextPlayer(int playerNumber) {
		nextPlayerOverride = playerNumber;
	}

	@Override
	public void onEndedInStore(EndedInStoreEvent event) {
		overrideNextPlayer(event.playerNumber);
	}

	@Override
	public void onTurnEnd(TurnEndEvent event) {
		if (playing) {
			currentPlayer = getNextPlayer(currentPlayer);
			printer.displayBoard(board);
		}
	}

	@Override
	public void onGameEnd(GameEndEvent event) {
		this.playing = false;
		gameEndReason = event.reason;
	}

	@Override
	public void onBadCommand(BadCommandEvent badCommand) {
		io.println("House is empty. Move again.");
		overrideNextPlayer(badCommand.sourcePlayer);
	}
}
