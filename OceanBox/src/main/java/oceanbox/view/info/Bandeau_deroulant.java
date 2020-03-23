package oceanbox.view.info;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class Bandeau_deroulant extends ScrollPane {

	private Label info;

	public Bandeau_deroulant(Label info) {

		this.info = info;
		this.setContent(this.info);
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setStyle("-fx-background-color: transparent;");
		this.setFitToHeight(true);
	}

	public Label getInfo() {
		return info;
	}
}
