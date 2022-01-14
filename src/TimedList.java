import java.util.ArrayList;

public class TimedList<T> {

	private ArrayList<T> innerList;
	private ArrayList<Long> timeList;
	private long expiryTime;
	private long cleanInterval;
	private long lastClean;
	public TimedList(long expiryTime) {
		this.innerList = new ArrayList<T>();
		this.timeList = new ArrayList<Long>();
		this.expiryTime = expiryTime;
		this.cleanInterval = 15*60*1000;
	}
	public TimedList(long expiryTime, long cleanInterval) {
		this(expiryTime);
		this.cleanInterval = cleanInterval;
	}
	public void add(T value) {
		innerList.add(value);
		timeList.add(System.currentTimeMillis());
	}

	public synchronized void clear() {
		try {
			if (innerList.size() != 0  && System.currentTimeMillis()-lastClean > cleanInterval) {
				this.lastClean = System.currentTimeMillis();
				ArrayList<T> newInnerList = new ArrayList<T>(innerList);
				ArrayList<Long> newTimeList = new ArrayList<Long>(timeList);
				while(newTimeList.size() != 0 && System.currentTimeMillis()-newTimeList.get(0) <= expiryTime) {
					newInnerList.remove(0);
					newTimeList.remove(0);
				}
				this.innerList = newInnerList;
				this.timeList = newTimeList;
			}
		}
		catch(IndexOutOfBoundsException e) {
			this.innerList = new ArrayList<T>();
			this.timeList = new ArrayList<Long>();
			e.printStackTrace();
		}
	}

	public boolean contains(T e) {
		for (int i = innerList.size()-1; i >= 0; i--) {
			if (System.currentTimeMillis()-timeList.get(i) < expiryTime) {
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
	public int size() {
		int toReturn = 0;
		for (int i = innerList.size()-1; i >= 0; i--) {
			if (System.currentTimeMillis()-timeList.get(i) < expiryTime) {
				toReturn++;
			}
			else {
				clear();
				return toReturn;
			}
		}
		return toReturn;
	}

}
