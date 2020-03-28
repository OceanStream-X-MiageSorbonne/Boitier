package oceanbox.controler;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import oceanbox.model.AbstractModel;
import oceanbox.propreties.ClientPropreties;
import oceanbox.view.Alerte;
import oceanbox.view.info.Bandeau_deroulant;
import oceanbox.view.info.Barre_info;
import oceanbox.view.info.BasicInfo;

/**
 * Cette classe contient des attributs et des méthodes qui jouent sur ce qui
 * s'affiche à l'écran
 */
public abstract class AbstractControler {

	protected AbstractModel model;
	protected Stage stage;
	protected int secondsBeforeClose;
	protected PauseTransition pauseBeforeClose = new PauseTransition();
	protected PauseTransition pauseBeforeCloseAlert = new PauseTransition();
	protected PauseTransition pauseBeforeShowUpInfo = new PauseTransition();
	protected Label closeInfoControler;
	protected Barre_info infoControler;

	/**
	 * Cet événement ferme l'application
	 */
	protected EventHandler<ActionEvent> closeApp = event -> {
		sleepMode(true);
		stage.close();
	};

	/**
	 * Cet événement alerte l'utilisateur que l'application va se fermer si le
	 * capteur du boîtier ne détecte pas de mouvement
	 */
	protected EventHandler<ActionEvent> closeAlert = event -> {
		closeInfoControler = new Alerte();

		model.notifyObserver(closeInfoControler, true);
	};

	/**
	 * Cet événement fait apparaître un bandeau déroulant d'information en bas de
	 * l'écran
	 */
	protected EventHandler<ActionEvent> basicInfoShowUp = event -> {
		// Ci-dessous un information test pour aider au paramétrage du bandeau déroulant
		String phraseTest = "Ceci est une très longue information qui apparaît à l'écran. Elle est composée d'environ 2 000 caractères au total. Je ne pense pas que vous ayez le courage de tout lire après ceci alors j'ai simplement copié / collé des citations au hasard trouvées sur internet. Tout cela n'a pour but que de tester l'intégration d'un bandeau d'informations de 2 000 caractères de long environ. Je sortis sur l'esplanade, où l'on avait mis quantité de chaises longues : toutes vides, mais leur toile pendant mollement et portant l'empreinte des corps qu'elles avaient accueillis. Ce n'est pas que j'ai des pensées contradictoires mais les choses le sont. Les nuits sont longues mais je trouve malgré tout que le temps passe vite. Le monde est dans les ténèbres, mais l'homme est plus élevé que son séjour; il porte ses regards plus haut, et il déploie les ailes de son ame. Lorsque les soixante minutes que nous appelons soixante ans ont sonné, il prend son essor et s'enflamme dans l'espace; les cendres de son enveloppe retombent sur la terre, et son ame, délivrée de sa prison fragile, s'élève seule, pure comme un son, vers les régions éthérées.... Mais ici-bas, du sein de cette vie obscure, il découvre les sommités du monde qui l'attend, éclairées par les rayons d'un soleil qui ne se lève point sur ce monde: ainsi l'habitant des régions australes, dans les longues nuits que le soleil n'interrompt point, voit cependant à midi une aurore boréale rougir les cimes des plus hautes montagnes, et il songe au long été où le soleil ne le délaissera plus. Les vieux péchés ont de longues ombres. Les vieillards et les comètes ont été vénérés pour la même raison : leurs longues barbes et leurs prétentions à prédire les événements. À ceux qui demandaient une raison à mes brusques départs, je décrivais l'huma­nisme : cet élan sentimental qui nous porte vers nos semblables, comme présidant à tout élan vagabond. J'ajoutais que c'était pour étancher ma soif de l' Autre que je me lançais dans de longues échappées. Mes interlocu­teurs se montraient ravis de ces réponses : la référence à l'humanisme est le meilleur moyen d'endormir une conversation.";
		infoControler = new Barre_info(new Bandeau_deroulant(new BasicInfo(phraseTest)));
		model.notifyObserver(infoControler, true);

		controlInfo();
	};

	public AbstractControler(Stage stage, AbstractModel model) {

		this.model = model;
		this.stage = stage;

		this.secondsBeforeClose = initSecondsBeforeClose();

		initStageProperties();

		control();
	}

	public AbstractModel getModel() {
		return model;
	}

	/**
	 * Cette méthode récupère les préférences du client concernant le temps que le
	 * boîtier doit rester allumé si son capteur ne détecte aucun mouvement
	 * 
	 * @return le nombre de secondes restantes avant la fermeture de l'application
	 */
	public int initSecondsBeforeClose() {

		String[] times = ClientPropreties.getPropertie("timeBeforeStandby").split(":");

		return (Integer.parseInt(times[0]) * 3600) + (Integer.parseInt(times[1]) * 60) + Integer.parseInt(times[2]);
	}

	/**
	 * Cette méthode met à jour le paramètre onStandy dans les propriétés du client
	 * 
	 * @param standby : qui est true si l'application est fermée et false sinon
	 */
	public void sleepMode(boolean standby) {

		try {
			if (standby)
				ClientPropreties.setPropertie("onStandby", "true");
			else
				ClientPropreties.setPropertie("onStandby", "false");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode initialise au lancement de l'application les différents
	 * événements qui ont un effet remarquable à l'écran
	 */
	public void initStageProperties() {

		stage.setOnShowing(event -> {
			sleepMode(false);
		});

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				sleepMode(true);
				stage.close();
			}
		});

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			model.notifyObserver(closeInfoControler, false);
			control();
		});
	}

	/**
	 * Cette méthode remet les différents comptes à rebours de l'application à zéro
	 * et initialise les événements qui se produisent à leur échéance
	 */
	public abstract void control();

	/**
	 * Cette méthode initialise les paramètres relatifs au bandeau d'informations
	 */
	public abstract void controlInfo();
}
