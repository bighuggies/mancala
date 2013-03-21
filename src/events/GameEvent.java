package events;

public interface GameEvent<L> {
	public void notify(final Object context, final L listener);
}
