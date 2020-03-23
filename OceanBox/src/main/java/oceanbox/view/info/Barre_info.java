package oceanbox.view.info;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Barre_info extends VBox {

	public Barre_info(Label info) {
		this.setMaxHeight(info.getHeight());
		this.getChildren().add(info);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setAlignment(Pos.CENTER);
		StackPane.setAlignment(this, Pos.BOTTOM_CENTER);
	}
}
