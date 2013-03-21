package events;

import mancala.Mancala;

public class TurnEndEvent implements GameEvent<TurnEndListener> {

	public final int playersTurn;

	public TurnEndEvent(int playerNumber) {
		this.playersTurn = playerNumber;
	}

	@Override
	public void notify(Object context, TurnEndListener listener) {
		Mancala gameContext = (Mancala) context;
		listener.onTurnEnd(gameContext, this);
	}
}
