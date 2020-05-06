package oceanbox.model;

import oceanbox.observer.Observable;
import oceanbox.observer.Observer;

import java.util.ArrayList;

/**
 * Cette classe permet de mettre à jour des éléments visibles et non visibles de
 * l'application
 */
public abstract class AbstractModel implements Observable {

	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	public void notifyObserver(Object object, boolean add) {
		for (Observer obs : listObserver)
			obs.update(object, add);
	}

	public void removeObserver() {
		listObserver = new ArrayList<Observer>();
	}
}
