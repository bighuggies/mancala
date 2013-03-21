package events;

import mancala.Board;

public interface EndedInStoreListener {
	public void onEndedInStore(Board boardContext, EndedInStoreEvent event);
}
