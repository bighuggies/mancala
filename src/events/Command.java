package events;

import mancala.Player;

public class Command implements GameEvent<CommandListener> {
	public final Player player;
	public final int command;

	public Command(Player player, int command) {
		this.player = player;
		this.command = command;
	}

	@Override
	public void notify(CommandListener listener) {
		listener.playerIssuedCommand(this);
	}
}
