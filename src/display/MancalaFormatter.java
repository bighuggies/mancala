package display;

import mancala.Board;
import mancala.GameObject;
import events.Events;
import events.GameEndEvent;
import events.GameEndListener;
import events.GameStartEvent;
import events.GameStartListener;
import events.TurnEndEvent;
import events.TurnEndListener;
import events.GameEndEvent.Reason;

public abstract class MancalaFormatter implements GameObject, TurnEndListener,
		GameStartListener, GameEndListener {
	private Events _events;

	public void displayBoard(Board board) {
		// override
	}

	public void displayScores(int[] scores) {
		// override
	}

	public void onTurnEnd(TurnEndEvent event) {
		displayBoard(event.boardState);
	}

	public void onGameStart(GameStartEvent event) {
		displayBoard(event.boardState);
	}

	public void onGameEnd(GameEndEvent event) {
		displayBoard(event.boardState);

		if (event.reason == Reason.FINISHED) {
			displayScores(event.boardState.getScores());
		}
	}
	
	public void setEvents(Events events) {
		this._events = events;

		_events.listen(GameStartEvent.class, this);
		_events.listen(GameEndEvent.class, this);
		_events.listen(TurnEndEvent.class, this);
	}
}
