package events;

import mancala.Board;

public class GameStartEvent implements GameEvent<GameStartListener> {

	public final Board boardState;

	public GameStartEvent(Board board) {
		this.boardState = board;
	}

	@Override
	public void notify(GameStartListener listener) {
		listener.onGameStart(this);
	}
}
