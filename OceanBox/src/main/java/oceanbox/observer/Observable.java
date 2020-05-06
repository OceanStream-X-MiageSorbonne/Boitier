package oceanbox.observer;

/**
 * Cette interface contient les méthodes qui permettent de mettre à jour ce qui
 * s'affiche à l'écran
 */
public interface Observable {

	public void addObserver(Observer obs);

	public void notifyObserver(Object object, boolean add);

	public void removeObserver();
}
