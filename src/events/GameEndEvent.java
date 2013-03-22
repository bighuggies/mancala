package events;

import mancala.Board;

public class GameEndEvent implements GameEvent<GameEndListener> {
	public enum Reason {
		QUITTING, FINISHED
	}

	public final Reason reason;
	public final Board boardState;

	public GameEndEvent(Reason reason, Board board) {
		this.reason = reason;
		this.boardState = board;
	}

	@Override
	public void notify(GameEndListener listener) {
		listener.onGameEnd(this);
	}
}
