package oceanbox.view.info;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;

/**
 * Cette classe définit la conteneur du bandeau déroulant d'informations
 */
public class Barre_info extends HBox {

	private Bandeau_deroulant bandeauDeroulant;

	public Barre_info(Bandeau_deroulant bandeauDeroulant) {

		this.bandeauDeroulant = bandeauDeroulant;

		this.setMaxHeight(this.bandeauDeroulant.getInfo().getHeight());
		this.getChildren().add(this.bandeauDeroulant);
		this.setStyle("-fx-background-color: #5F9EA0;");
		this.setAlignment(Pos.CENTER);
		StackPane.setAlignment(this, Pos.BOTTOM_CENTER);
	}

	public Bandeau_deroulant getBandeauDeroulant() {
		return bandeauDeroulant;
	}
}
