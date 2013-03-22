package events;

public interface CommandListener {
	public void onPlayerIssuedCommand(CommandEvent command);
}
