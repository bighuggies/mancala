package mancala;

import events.Command;
import events.CommandListener;

public class Board implements CommandListener {
	public Board(Player[] players) {
		
	}

	@Override
	public void playerIssuedCommand(Command command) {
		System.out.println(command.command);
		
	}
}
