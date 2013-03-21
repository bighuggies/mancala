package events;

import mancala.Mancala;

public interface CommandListener {
	public void onPlayerIssuedCommand(Mancala gameContext, CommandEvent command);
}
