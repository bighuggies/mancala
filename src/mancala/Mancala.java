package mancala;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
	public final List<Player> players;
	public final Board board;

	private MockIO io;
	private Events dispatcher;
	private MancalaFormatter printer;
	private boolean playing = true;
	private Reason gameEndReason;

	private Player currentPlayer;
	private Player nextPlayerOverride;

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

		players = new ArrayList<Player>();
		board = new Board(dispatcher);

		players.add(new Player("1", 0));
		players.add(new Player("2", 1));

		currentPlayer = players.get(0);
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
			HashMap<Player, Integer> scores = new LinkedHashMap<Player, Integer>();
			int[] points = board.getScores();

			for (int i = 0; i < points.length; i++) {
				Player player = players.get(i);
				int score = points[i];

				scores.put(player, score);
			}

			this.printer.displayScores(scores);
		}
	}

	public void nextTurn() {
		String prompt = "Player " + currentPlayer.name
				+ "'s turn - Specify house number or 'q' to quit: ";
		int command = io.readInteger(prompt, 1, 6, -1, "q");

		if (command == -1) {
			dispatcher.notify(this, new GameEndEvent(Reason.QUITTING));
			return;
		}

		dispatcher.notify(this, new CommandEvent(currentPlayer, command));
		dispatcher.notify(this, new TurnEndEvent(currentPlayer));
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Player getNextPlayer(Player currentPlayer) {
		return getNextPlayer(this.players.indexOf(currentPlayer));
	}

	public Player getNextPlayer(int currentPlayer) {
		if (nextPlayerOverride != null) {
			Player nextPlayer = nextPlayerOverride;
			nextPlayerOverride = null;
			return nextPlayer;
		}

		int next = (currentPlayer + 1) % this.players.size();

		return this.players.get(next);
	}

	public void overrideNextPlayer(int player) {
		nextPlayerOverride = this.players.get(player);
	}

	public void overrideNextPlayer(Player player) {
		overrideNextPlayer(player.number);
	}

	@Override
	public void onEndedInStore(Board boardContext, EndedInStoreEvent event) {
		overrideNextPlayer(event.playerNumber);
	}

	@Override
	public void onTurnEnd(Mancala gameContext, TurnEndEvent event) {
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
