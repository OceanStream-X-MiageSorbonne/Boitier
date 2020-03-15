package oceanbox.observer;

public interface Observable {
	public void addObserver(Observer obs);
	public void notifyObserver(String time);
	public void removeObserver();
}
