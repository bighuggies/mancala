package events;

public class TurnEndEvent implements GameEvent<TurnEndListener> {

	public final int playersTurn;

	public TurnEndEvent(int player) {
		this.playersTurn = player;
	}

	@Override
	public void notify(TurnEndListener listener) {
		listener.onTurnEnd(this);
	}
}
