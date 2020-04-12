package oceanbox.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Veille extends BorderPane {

	private Label message = new Label();

	public Veille() {

		message.setText("Le boîtier est en veille.");
		message.setFont(new Font(30));
		message.setTextAlignment(TextAlignment.CENTER);
		message.setWrapText(true);
		message.setTextFill(Color.WHITE);

		this.setCenter(message);
		
		this.setStyle("-fx-background-color: #000000;");
	}
}
