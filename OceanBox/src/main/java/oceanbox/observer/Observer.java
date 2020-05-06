package oceanbox.observer;

/**
 * Cette interface contient la méthode qui modifie les composants de
 * l'application
 */
public interface Observer {

	public void update(Object object, boolean add);
}
