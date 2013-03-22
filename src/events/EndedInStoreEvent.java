package events;

public class EndedInStoreEvent implements GameEvent<EndedInStoreListener> {

	public final int storeNumber;
	public final int playerNumber;

	public EndedInStoreEvent(int storeNumber, int playerNumber) {
		this.storeNumber = storeNumber;
		this.playerNumber = playerNumber;
	}

	@Override
	public void notify(EndedInStoreListener listener) {
		listener.onEndedInStore(this);
	}
}
