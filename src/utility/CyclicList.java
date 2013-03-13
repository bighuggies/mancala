package utility;

import java.util.AbstractList;

public class CyclicList<T> extends AbstractList<T> {

	private int cursor = 0;

	@Override
	public T get(int index) {
		return (T) this.toArray()[index];
	}

	@Override
	public int size() {
		return this.toArray().length;
	}

	public T next() {
		int get = getCursor();
		setCursor(getCursor() + 1);

		if (getCursor() >= this.size())
			setCursor(0);

		return this.get(get);
	}

	private int getCursor() {
		return cursor;
	}

	private void setCursor(int cursor) {
		this.cursor = cursor;
	}

}
