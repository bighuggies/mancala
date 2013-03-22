package events;

import mancala.Board;

public interface StealListener {
	void onStealMove(StealEvent stealEvent);
}
