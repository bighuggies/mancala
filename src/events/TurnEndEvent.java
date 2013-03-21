package events;

import mancala.Mancala;
import mancala.Player;

public class TurnEndEvent implements GameEvent<TurnEndListener> {

	public final Player playersTurn;

	public TurnEndEvent(Player player) {
		this.playersTurn = player;
	}

	@Override
	public void notify(Object context, TurnEndListener listener) {
		Mancala gameContext = (Mancala) context;
		listener.onTurnEnd(gameContext, this);
	}
}
