import java.util.ArrayList;

public class TimedList<T> {

	private ArrayList<T> innerList;
	private ArrayList<Long> time;
	private long expiryTime;

	public TimedList(long expiryTime) {
		this.innerList = new ArrayList<T>();
		this.time = new ArrayList<Long>();
		this.expiryTime = expiryTime;
	}

	public void add(T value) {
		innerList.add(value);
		time.add(System.currentTimeMillis());
	}

	public void clear() {
		innerList.clear();
		time.clear();
	}

	public boolean contains(T e) {
		for (int i = innerList.size()-1; i >= 0; i--) {
			if (System.currentTimeMillis()-time.get(i) < expiryTime) {
				if (innerList.get(i).equals(e)) {
					return true;
				}
			}
			else {
				clear();
				return false;
			}
		}
		return false;
	}
}
