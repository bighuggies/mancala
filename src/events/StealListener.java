package events;

import mancala.Board;

public interface StealListener {
	void onStealMove(Board boardContext, StealEvent stealEvent);
}
