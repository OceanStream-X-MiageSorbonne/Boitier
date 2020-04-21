package oceanbox.view;

import oceanbox.controler.AbstractControler;

import oceanbox.observer.Observer;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class Lecteur_video extends StackPane implements Observer {

	public Lecteur_video(AbstractControler controler) throws FileNotFoundException, IOException {

		Horloge horloge = new Horloge(controler);

		this.getChildren().add(controler.getContenu());

		this.getChildren().add(horloge);

		this.setStyle("-fx-background-color: #000000;");
	}

	@Override
	public void update(Node node, boolean add) {

		if (node.getClass() == Veille.class) {
			if (add) {
				this.getChildren().removeAll(this.getChildren());
				this.getChildren().add(node);
			} else {
				this.getChildren().removeAll(this.getChildren());
			}
		} else {
			if (add)
				this.getChildren().add(node);
			else
				this.getChildren().remove(node);
		}
	}
}
