package oceanbox.model;

import oceanbox.observer.Observable;
import oceanbox.observer.Observer;
import java.util.ArrayList;

import javafx.scene.Node;

public abstract class AbstractModel implements Observable {

	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	public void notifyObserver(Node node, boolean add) {
		for (Observer obs : listObserver)
			obs.update(node, add);
	}

	public void removeObserver() {
		listObserver = new ArrayList<Observer>();
	}
}
