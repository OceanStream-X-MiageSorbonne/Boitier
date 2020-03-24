package oceanbox.view.info;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;

public class Barre_info extends HBox {

	private Bandeau_deroulant bDeroulant;

	public Barre_info(Bandeau_deroulant bDeroulant) {

		this.bDeroulant = bDeroulant;
		this.setMaxHeight(this.bDeroulant.getInfo().getHeight());
		this.getChildren().add(this.bDeroulant);
		this.setStyle("-fx-background-color: black;");
		this.setAlignment(Pos.CENTER);
		StackPane.setAlignment(this, Pos.BOTTOM_CENTER);
	}

	public Bandeau_deroulant getbDeroulant() {
		return bDeroulant;
	}
}
