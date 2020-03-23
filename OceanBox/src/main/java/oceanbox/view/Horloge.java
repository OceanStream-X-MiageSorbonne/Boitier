package oceanbox.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import oceanbox.controler.AbstractControler;

public class Horloge extends Label {

	private SimpleDateFormat affichage = new SimpleDateFormat("dd-MM-yyyy" + "\n" + "HH:mm:ss");

	public Horloge(Stage stage, AbstractControler controler) {

		this.setFont(new Font(40));
		this.setTextAlignment(TextAlignment.RIGHT);
		this.setTextFill(Color.WHITE);
		this.setPadding(new Insets(30));
		StackPane.setAlignment(this, Pos.BOTTOM_RIGHT);

		KeyFrame update = new KeyFrame(Duration.seconds(1), event -> {
			this.setText(affichage.format(new Date()));
		});
		Timeline horloge = new Timeline(update);
		horloge.setCycleCount(7);
		horloge.setOnFinished(event -> {
			controler.getModel().notifyObserver(this, false);
		});
		horloge.play();
	}
}
