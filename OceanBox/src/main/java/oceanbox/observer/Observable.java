package oceanbox.observer;

import javafx.scene.Node;

public interface Observable {
	public void addObserver(Observer obs);
	public void notifyObserver(Node node, boolean add);
	public void removeObserver();
}
