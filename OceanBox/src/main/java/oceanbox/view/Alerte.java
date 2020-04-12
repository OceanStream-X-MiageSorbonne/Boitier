package oceanbox.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Cette classe définit l'alerte qui apparaît à l'écran quand aucun mouvement
 * n'est détecté au bout d'un certain temps
 */
public class Alerte extends Label {

	public Alerte() {

		this.setText("Attention : si vous ne bougez pas le boîtier va se mettre en veille dans 5 sec");
		this.setFont(new Font(30));
		this.setTextAlignment(TextAlignment.CENTER);
		this.setWrapText(true);
		this.setTextFill(Color.WHITE);
		this.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, new CornerRadii(10), Insets.EMPTY)));
		this.setPadding(new Insets(20));
	}
}
