package struct;

/**
 * Pair of two instances of the same type.
 * Has getter and setter for each.
 */
public class Duet<T> {
	private T first;
	private T last;

	public Duet() {}

	public Duet(T first, T last) {
		this.first = first;
		this.last = last;
	}

	public T getFirst() {
		return first;
	}

	public T getLast() {
		return last;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public void setLast(T last) {
		this.last = last;
	}
}
