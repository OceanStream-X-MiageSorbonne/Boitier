package oceanbox.observer;

import javafx.scene.Node;

public interface Observer {
	public void update(Node node, boolean add);
}
