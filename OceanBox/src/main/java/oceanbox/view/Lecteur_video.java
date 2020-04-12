package oceanbox.view;

import oceanbox.controler.AbstractControler;
import oceanbox.model.Contenu;
import oceanbox.observer.Observer;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Lecteur_video extends StackPane implements Observer {

	public Lecteur_video(Stage stage, AbstractControler controler) throws FileNotFoundException, IOException {

		Contenu contenu = new Contenu(stage);

		Horloge horloge = new Horloge(stage, controler);

		this.getChildren().add(contenu);

		this.getChildren().add(horloge);
	}

	@Override
	public void update(Node node, boolean add) {

		if (add)
			this.getChildren().add(node);
		else
			this.getChildren().remove(node);
	}
}
