package oceanbox.view.info;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class Barre_info extends HBox {

	public Barre_info(Bandeau_deroulant bDeroulant) {

		this.setMaxHeight(bDeroulant.getInfo().getHeight());
		this.getChildren().add(bDeroulant);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setAlignment(Pos.CENTER);
		StackPane.setAlignment(this, Pos.BOTTOM_CENTER);
	}
}
