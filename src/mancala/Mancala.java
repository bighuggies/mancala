package mancala;

import java.util.ArrayList;
import java.util.List;

import utility.MockIO;
import display.TwoPlayerSingleStoreASCIIFormatter;
import display.MancalaFormatter;
import events.CommandEvent;
import events.CommandListener;
import events.EndedInStoreEvent;
import events.EndedInStoreListener;
import events.Events;
import events.TurnEndEvent;
import events.TurnEndListener;

/**
 * This class is the starting point for SOFTENG 701 Assignment 1.1 in 2013.
 */
public class Mancala implements CommandListener, EndedInStoreListener,
		TurnEndListener {
	public final List<Player> players;
	public final Board board;

	private MockIO io;
	private Events dispatcher;
	private MancalaFormatter printer;

	private Player currentPlayer;
	private Player nextPlayerOverride;

	public static void main(String[] args) {
		new Mancala().play(new MockIO());
	}

	public Mancala() {
		dispatcher = new Events();

		// Listen for game quit events, turn end events
		dispatcher.listen(CommandEvent.class, this);
		dispatcher.listen(TurnEndEvent.class, this);

		players = new ArrayList<Player>();
		board = new Board(dispatcher);

		printer = new TwoPlayerSingleStoreASCIIFormatter();

		players.add(new Player("1", 0));
		players.add(new Player("2", 1));

		currentPlayer = players.get(0);
	}

	public void play(MockIO io) {
		this.io = io;
		nextTurn();
	}

	public void nextTurn() {
		int command = io.readInteger(">Player " + currentPlayer.name
				+ "'s turn - Specify house number or 'q' to quit:\n<", 1, 6,
				-1, "q");

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
			nextPlayerOverride = null;
			return nextPlayerOverride;
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
	public void onPlayerIssuedCommand(Mancala gameContext, CommandEvent command) {
		if (command.houseIndex == -1) {
			System.exit(0);
		}
	}

	@Override
	public void onEndedInStore(Board boardContext, EndedInStoreEvent event) {
		overrideNextPlayer(event.playerNumber);
	}

	@Override
	public void onTurnEnd(Mancala gameContext, TurnEndEvent event) {
		currentPlayer = getNextPlayer(currentPlayer);
		printer.display(board);
		nextTurn();
	}
}
