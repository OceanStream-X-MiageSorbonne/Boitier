package oceanbox.view.info;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class BasicInfo extends Label {

	public BasicInfo(String texte) {
		
		this.setText(texte);
		this.setFont(new Font(40));
		this.setTextAlignment(TextAlignment.CENTER);
		this.setWrapText(true);
		this.setTextFill(Color.WHITE);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);
		HBox.setHgrow(this, Priority.ALWAYS);
	}
}
