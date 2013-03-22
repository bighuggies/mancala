package events;

import mancala.Mancala;
import mancala.Player;

public class CommandEvent implements GameEvent<CommandListener> {
	public final Player player;
	public final int houseIndex;

	public CommandEvent(Player player, int command) {
		this.player = player;
		this.houseIndex = command - 1;
	}

	@Override
	public void notify(CommandListener listener) {
		listener.onPlayerIssuedCommand(this);
	}
}
