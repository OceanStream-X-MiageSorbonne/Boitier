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
import javafx.util.Duration;

import oceanbox.controler.AbstractControler;

/**
 * Cette classe définit l'horloge qui apparaît en bas à droite de l'écran durant
 * les 5 premières secondes après le lancement de l'application
 */
public class Horloge extends Label {

	private SimpleDateFormat affichage = new SimpleDateFormat("dd-MM-yyyy" + "\n" + "HH:mm:ss");

	public Horloge(AbstractControler controler) {

		this.setFont(new Font(40));
		this.setTextAlignment(TextAlignment.RIGHT);
		this.setTextFill(Color.WHITE);
		this.setPadding(new Insets(30));
		StackPane.setAlignment(this, Pos.BOTTOM_RIGHT);

		Timeline horloge = timelineForHorloge(controler);

		horloge.play();
	}

	/**
	 * Cette méthode permet de spécifier les paramètres de la timeline dans laquelle
	 * est affichée et mise à jour l'horloge
	 * 
	 * @param controler : qui permet de mettre à jour l'horloge à l'écran
	 * @return la timeline qui affiche l'horloge
	 */
	public Timeline timelineForHorloge(AbstractControler controler) {

		KeyFrame update = new KeyFrame(Duration.seconds(1), event -> {
			this.setText(affichage.format(new Date()));
		});

		Timeline horloge = new Timeline(update);
		horloge.setCycleCount(7);
		horloge.setOnFinished(event -> {
			controler.getModel().notifyObserver(this, false);
		});

		return horloge;
	}
}
