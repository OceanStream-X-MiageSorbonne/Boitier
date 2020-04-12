package oceanbox.view.info;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Cette classe définit un objet type d'information textuelle destinée à
 * apparaître dans le bandeau d'informations
 */
public class BasicInfo extends Label {

	public BasicInfo(String texte) {

		this.setText(texte);
		this.setFont(new Font(30));
		this.setTextAlignment(TextAlignment.CENTER);
		this.setWrapText(true);
		this.setTextFill(Color.BLACK);
		this.setPadding(new Insets(10, 10, 20, 10));
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #5F9EA0;");
		this.setHeight(this.getFont().getSize() + this.getPadding().getTop() + this.getPadding().getBottom());
		HBox.setHgrow(this, Priority.ALWAYS);
	}
}
