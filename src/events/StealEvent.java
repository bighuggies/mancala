package events;

public class StealEvent implements GameEvent<StealListener> {
	public final int stealingHouseNumber;
	public final int stealingPlayerNumber;

	public StealEvent(int houseNumber, int playerNumber) {
		this.stealingHouseNumber = houseNumber;
		this.stealingPlayerNumber = playerNumber;
	}

	@Override
	public void notify(StealListener listener) {
		listener.onStealMove(this);
	}
}
