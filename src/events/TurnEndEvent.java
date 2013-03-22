package events;

import mancala.Board;

public class TurnEndEvent implements GameEvent<TurnEndListener> {

	public final Board boardState;
	public final int playersTurn;

	public TurnEndEvent(Board board, int player) {
		this.boardState = board;
		this.playersTurn = player;
	}

	@Override
	public void notify(TurnEndListener listener) {
		listener.onTurnEnd(this);
	}
}
