package oceanbox.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Alerte extends Label {

	public Alerte() {

		this.setText("Attention : si vous ne bougez pas le boîtier va s'éteindre dans 5 sec");
		this.setFont(new Font(40));
		this.setTextAlignment(TextAlignment.CENTER);
		this.setWrapText(true);
		this.setTextFill(Color.WHITE);
		this.setPadding(new Insets(30));
	}
}
