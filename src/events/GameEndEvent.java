package events;

public class GameEndEvent implements GameEvent<GameEndListener> {
	public enum Reason {
		QUITTING, FINISHED
	}

	public final Reason reason;

	public GameEndEvent(Reason reason) {
		this.reason = reason;
	}

	@Override
	public void notify(Object context, GameEndListener listener) {
		listener.onGameEnd(this);
	}
}
