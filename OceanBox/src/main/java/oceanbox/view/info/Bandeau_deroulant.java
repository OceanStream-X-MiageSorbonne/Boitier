package oceanbox.view.info;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.animation.PauseTransition;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.util.Duration;

/**
 * Cette classe définit le bandeau déroulant d'informations du bas de l'écran
 */
public class Bandeau_deroulant extends ScrollPane {

	private Label info;
	private Deque<PauseTransition> defilement;

	public Bandeau_deroulant(Label info) {

		this.info = info;

		this.setContent(this.info);
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setStyle("-fx-background-color: transparent;");
		this.setFitToHeight(true);

		this.defilement = new ArrayDeque<PauseTransition>();
		initDefilement();
	}

	/**
	 * Cette méthode permet d'obtenir le nombre de caractères dans le texte de
	 * l'information
	 * 
	 * @return le nombre de caractères
	 */
	public int nbCharInInfo() {
		return info.getText().length();
	}

	/**
	 * Cette méthode permet de paramétrer une vitesse de défilement relative au
	 * nombre de caractères qui composent le texte de l'information
	 * 
	 * @return la vitesse de défilement du bandeau d'informations
	 */
	public double vitesseDefilement() {

		if (nbCharInInfo() == 0)
			return 1.0;

		int longueurNbCharInInfo = Integer.toString(nbCharInInfo()).length();

		double ajustement = 1.0 * (longueurNbCharInInfo - 2) + (nbCharInInfo() / Math.pow(10, longueurNbCharInInfo));

		double vitesse = ((double) nbCharInInfo() / (nbCharInInfo() * nbCharInInfo())) / ajustement;

		if (vitesse <= 0.0)
			return 1.0;
		return vitesse;
	}

	/**
	 * Cette méthode initialise des timer secrets qui font défiler petit à petit le
	 * texte de l'information dans le bandeau
	 */
	public void initDefilement() {

		defilement.add(new PauseTransition(Duration.seconds(2)));

		defilement.getLast().setOnFinished(event -> {

			defilement.removeFirst();
			defilement.getFirst().play();
		});

		double max = 0.0;

		while (max < 1.0) {

			defilement.add(new PauseTransition(Duration.seconds(0.07)));

			max += vitesseDefilement();

			defilement.getLast().setOnFinished(event -> {

				this.setHvalue(this.getHvalue() + vitesseDefilement());
				defilement.removeFirst();
				defilement.getFirst().play();
			});
		}

		defilement.add(new PauseTransition(Duration.seconds(2)));

		defilement.getLast().setOnFinished(event -> {

			defilement.removeFirst();
			defilement.getFirst().play();
		});

		defilement.add(new PauseTransition(Duration.seconds(0.07)));

	}

	public Label getInfo() {
		return info;
	}

	public Deque<PauseTransition> getDefilement() {
		return defilement;
	}
}
