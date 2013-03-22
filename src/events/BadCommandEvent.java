package events;

public class BadCommandEvent implements GameEvent<BadCommandListener> {
	public final int sourcePlayer;

	public BadCommandEvent(int playerNumber) {
		sourcePlayer = playerNumber;
	}

	@Override
	public void notify(BadCommandListener listener) {
		listener.onBadCommand(this);
	}
}
