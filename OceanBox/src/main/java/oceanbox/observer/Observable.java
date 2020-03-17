package oceanbox.observer;

public interface Observable {
	public void addObserver(Observer obs);
	public void notifyObserver();
	public void removeObserver();
}
