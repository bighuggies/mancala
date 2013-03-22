package events;

import mancala.Player;

public class BadCommandEvent implements GameEvent<BadCommandListener> {
	public final Player sourcePlayer;
	
	public BadCommandEvent(Player player) {
		sourcePlayer = player;
	}
	
	@Override
	public void notify(BadCommandListener listener) {
		listener.onBadCommand(this);
	}
}
