package events;

import mancala.Mancala;

public interface TurnEndListener {
	public void onTurnEnd(TurnEndEvent event);
}
