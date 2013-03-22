package events;

public interface GameEvent<L> {
	public void notify(final L listener);
}
