package events;

import mancala.Mancala;

public interface CommandListener {
	public void onPlayerIssuedCommand(CommandEvent command);
}
