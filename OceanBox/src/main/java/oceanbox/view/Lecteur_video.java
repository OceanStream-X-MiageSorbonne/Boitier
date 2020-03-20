package oceanbox.view;

import oceanbox.controler.AbstractControler;
import oceanbox.observer.Observer;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Lecteur_video extends StackPane implements Observer {

	private SimpleDateFormat affichage = new SimpleDateFormat("dd-MM-yyyy" + "\n" + "HH:mm:ss");
	private Label timeInfo = new Label();
	private Label closeInfo = new Label("Attention : si vous ne bougez pas le boîtier va s'éteindre");

	
	public Lecteur_video(Stage stage, AbstractControler controler, String fileName) {

		Contenu contenu = new Contenu(stage, fileName);

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE)
					stage.close();
			}
		});

		this.getChildren().add(contenu.getContainer());

		this.timeInfo.setFont(new Font(40));
		this.timeInfo.setTextAlignment(TextAlignment.RIGHT);
		this.timeInfo.setTextFill(Color.WHITE);
		this.timeInfo.setPadding(new Insets(30));
		StackPane.setAlignment(this.timeInfo, Pos.BOTTOM_RIGHT);
		
		KeyFrame update = new KeyFrame(Duration.seconds(0.5), event -> {
			this.timeInfo.setText(affichage.format(new Date()));
		});
		Timeline horloge = new Timeline(update);
		horloge.setCycleCount(12);
		horloge.setOnFinished(event -> {
			this.timeInfo.setManaged(false);
			this.timeInfo.setVisible(false);
		});
		horloge.play();
		
		this.getChildren().add(this.timeInfo);
		
		this.closeInfo.setFont(new Font(40));
		this.closeInfo.setTextAlignment(TextAlignment.CENTER);
		this.closeInfo.setTextFill(Color.WHITE);
		this.closeInfo.setPadding(new Insets(30));
		
		controler.setCloseInfoControler(this.closeInfo);
	}

	@Override
	public void update(Node node, boolean add) {
		if (add)
			this.getChildren().add(node);
		else
			this.getChildren().remove(node);
	}
}
