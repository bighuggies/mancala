package events;

public class CommandEvent implements GameEvent<CommandListener> {
	public final int playerNumber;
	public final int houseNumber;

	public CommandEvent(int playerNumber, int command) {
		this.playerNumber = playerNumber;
		this.houseNumber = command - 1;
	}

	@Override
	public void notify(CommandListener listener) {
		listener.onPlayerIssuedCommand(this);
	}
}
