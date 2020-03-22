package oceanbox.view;

import oceanbox.controler.AbstractControler;
import oceanbox.observer.Observer;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Lecteur_video extends StackPane implements Observer {

	public Lecteur_video(Stage stage, AbstractControler controler, String fileName) {

		Contenu contenu = new Contenu(stage, fileName);

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE)
					stage.close();
			}
		});

		Horloge horloge = new Horloge(stage);

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
