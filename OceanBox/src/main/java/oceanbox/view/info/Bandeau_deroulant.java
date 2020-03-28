package oceanbox.view.info;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

/**
 * Cette classe définit le bandeau déroulant d'informations du bas de l'écran
 */
public class Bandeau_deroulant extends ScrollPane {

	private Label info;
	private List<PauseTransition> defilement;

	public Bandeau_deroulant(Label info) {

		this.info = info;

		this.setContent(this.info);
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setStyle("-fx-background-color: transparent;");
		this.setFitToHeight(true);

		this.defilement = new ArrayList<PauseTransition>();
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

		int longueurNbCharInInfo = Integer.toString(nbCharInInfo()).length();

		double ajustement = 1.0 * (longueurNbCharInInfo - 2) + (nbCharInInfo() / Math.pow(10, longueurNbCharInInfo));

		double vitesse = ((double) nbCharInInfo() / (nbCharInInfo() * nbCharInInfo())) / ajustement;

		return vitesse;
	}

	/**
	 * Cette méthode initialise des timer secrets qui font défiler petit à petit le
	 * texte de l'information dans le bandeau
	 */
	public void initDefilement() {

		defilement.add(new PauseTransition(Duration.seconds(2)));

		defilement.get(0).setOnFinished(event -> {

			defilement.get(1).play();
		});

		double max = 0.0;
		int i = 1;

		while (max < 1.0) {

			defilement.add(new PauseTransition(Duration.seconds(0.07)));

			int pos[] = { i };
			max += vitesseDefilement();
			defilement.get(i).setOnFinished(event -> {

				this.setHvalue(this.getHvalue() + vitesseDefilement());
				defilement.get(pos[0] + 1).play();
			});

			i++;
		}

		defilement.add(new PauseTransition(Duration.seconds(2)));

		int pos[] = { i };
		defilement.get(i).setOnFinished(event -> {

			defilement.get(pos[0] + 1).play();
		});

		defilement.add(new PauseTransition(Duration.seconds(0.07)));

	}

	public Label getInfo() {
		return info;
	}

	public List<PauseTransition> getDefilement() {
		return defilement;
	}
}
