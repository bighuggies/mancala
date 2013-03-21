package events;

import mancala.Board;

public class StealEvent implements GameEvent<StealListener> {
	public final int stealingHouseNumber;
	public final int stealingPlayerNumber;

	public StealEvent(int houseNumber, int playerNumber) {
		this.stealingHouseNumber = houseNumber;
		this.stealingPlayerNumber = playerNumber;
	}

	@Override
	public void notify(Object context, StealListener listener) {
		Board boardContext = (Board) context;
		listener.onStealMove(boardContext, this);
	}
}
