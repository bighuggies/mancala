package events;

import mancala.Mancala;
import mancala.Player;

public class CommandEvent implements GameEvent<CommandListener> {
	public final Player player;
	public final int houseNumber;

	public CommandEvent(Player player, int command) {
		this.player = player;
		this.houseNumber = command;
	}

	@Override
	public void notify(Object context, CommandListener listener) {
		Mancala gameContext = (Mancala) context;
		listener.onPlayerIssuedCommand(gameContext, this);
	}
}
