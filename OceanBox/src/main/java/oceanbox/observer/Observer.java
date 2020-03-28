package oceanbox.observer;

import javafx.scene.Node;

/**
 * Cette interface contient la méthode qui modifie les composants de
 * l'application
 */
public interface Observer {

	public void update(Node node, boolean add);
}
