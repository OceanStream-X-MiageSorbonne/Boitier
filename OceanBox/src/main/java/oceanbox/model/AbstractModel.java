package oceanbox.model;

import oceanbox.observer.Observable;
import oceanbox.observer.Observer;
import java.util.ArrayList;

public abstract class AbstractModel implements Observable {
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	public void notifyObserver(String time) {
		for (Observer obs : listObserver)
			obs.update(time);
	}

	public void removeObserver() {
		listObserver = new ArrayList<Observer>();
	}
}
