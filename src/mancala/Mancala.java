package mancala;

import java.util.ArrayList;
import java.util.List;

import utility.MockIO;
import events.CommandEvent;
import events.CommandListener;
import events.Events;

/**
 * This class is the starting point for SOFTENG 701 Assignment 1.1 in 2013.
 */
public class Mancala implements CommandListener {
	public final List<Player> players;
	public final Board board;
	
	private Events dispatcher;
	private Player currentPlayer;

	public static void main(String[] args) {
		new Mancala().play(new MockIO());
	}

	public Mancala() {
		dispatcher = new Events();
		
		// Listen for game quit events
		dispatcher.listen(CommandEvent.class, this);

		players = new ArrayList<Player>();
		board = new Board(dispatcher);

		players.add(new Player("1", 0));
		players.add(new Player("2", 1));

		currentPlayer = players.get(0);
	}

	public void play(MockIO io) {
		while (true) {
			int command = io.readInteger(">Player " + currentPlayer.name
					+ "'s turn - Specify house number or 'q' to quit:\n<", 1,
					6, -1, "q");

			dispatcher.notify(this, new CommandEvent(currentPlayer, command));

			currentPlayer = getNextPlayer(currentPlayer);
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Player getNextPlayer(Player currentPlayer) {
		int cur = this.players.indexOf(currentPlayer);
		int next = (cur + 1) % this.players.size();		
		
		return this.players.get(next);
	}

	@Override
	public void onPlayerIssuedCommand(Mancala gameContext, CommandEvent command) {
		if (command.command == -1) {
			System.exit(0);
		}
	}
}
