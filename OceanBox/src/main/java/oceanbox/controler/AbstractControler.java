package oceanbox.controler;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Date;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.PauseTransition;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;

import oceanbox.model.AbstractModel;
import oceanbox.model.Contenu;

import oceanbox.propreties.ClientPropreties;

import oceanbox.view.Alerte;
import oceanbox.view.Horloge;
import oceanbox.view.Veille;
import oceanbox.view.info.Bandeau_deroulant;
import oceanbox.view.info.Barre_info;
import oceanbox.view.info.BasicInfo;

/**
 * Cette classe contient des attributs et des méthodes qui jouent sur ce qui
 * s'affiche à l'écran
 */
public abstract class AbstractControler {

	protected Stage stage;
	protected AbstractModel model;
	protected int secondsBeforeClose;
	protected PauseTransition pauseBeforeVeille;
	protected PauseTransition pauseBeforeVeilleAlert;
	protected PauseTransition pauseBeforeShowUpInfo;
	protected Label veilleInfoControler;
	protected Barre_info infoControler;
	protected Deque<PauseTransition> defilementInfo;
	protected Horloge horloge;
	protected boolean sleep;
	protected Veille veille;
	protected Contenu contenu;
	protected boolean download;

	/**
	 * Cet événement ferme l'application
	 */
	protected EventHandler<ActionEvent> veilleApp = event -> {
		goInVeille();
	};

	/**
	 * Cet événement alerte l'utilisateur que l'application va se fermer si le
	 * capteur du boîtier ne détecte pas de mouvement
	 */
	protected EventHandler<ActionEvent> veilleAlert = event -> {
		veilleInfoControler = new Alerte();
		model.notifyObserver(veilleInfoControler, true);
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

	public AbstractControler() {
		// Ceci est un constructeur qui n'est utile que pour les tests unitaires
	}

	public AbstractControler(Stage stage, AbstractModel model) {

		this.model = model;
		this.stage = stage;
		this.horloge = new Horloge(this);
		this.contenu = new Contenu(this);
		this.veille = new Veille();
		this.sleep = false;
		this.download = false;

		initStageProperties();

		this.secondsBeforeClose = initSecondsBeforeClose();

		control();
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
			if (standby) {
				ClientPropreties.setPropertie("onStandby", "true");
				setSleep(true);
			} else {
				ClientPropreties.setPropertie("onStandby", "false");
				setSleep(false);
			}

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
				stage.hide();

			} else if (!isSleep()) {
				if (event.getCode() == KeyCode.SPACE) {
					goInVeille();
				} else {
					if (veilleInfoControler != null)
						model.notifyObserver(veilleInfoControler, false);
					control();
				}
			} else {
				goOutOfVeille();
			}
		});
	}

	private void goInVeille() {
		sleepMode(true);
		model.notifyObserver(veille, true);
		if (veilleInfoControler != null)
			model.notifyObserver(veilleInfoControler, false);

		if (horloge.getMontre() != null) {
			horloge.getMontre().stop();
			horloge.setMontre(null);
			model.notifyObserver(horloge, false);
		}

		if (infoControler != null) {
			model.notifyObserver(infoControler, false);
			defilementInfo.getFirst().stop();
		}

		contenu.stopDiffusion();
		controlVeille();
	}

	private void goOutOfVeille() {

		horloge.setMontre(horloge.timelineForHorloge(this));
		horloge.getMontre().play();
		contenu.initVideos();
		control();
	}

	/**
	 * Cette méthode initialise au lancement de l'application les téléchargements
	 * réguliers de vidéos
	 */
	public void initDowloadVideos() {

		LocalTime currentTime = LocalTime.now();

		LocalTime downloadTime = LocalTime.parse(ClientPropreties.getPropertie("downloadHour"));

		Timer dowloadTimer = new Timer();
		LocalDateTime firstTimeDownload = null;

		if (currentTime.compareTo(downloadTime) < 0) {
			firstTimeDownload = LocalDateTime.now().withHour(downloadTime.getHour())
					.withMinute(downloadTime.getMinute()).withSecond(downloadTime.getSecond());
		} else {
			firstTimeDownload = LocalDateTime.now().plus(1, ChronoUnit.DAYS).withHour(downloadTime.getHour())
					.withMinute(downloadTime.getMinute()).withSecond(downloadTime.getSecond());
		}

		dowloadTimer.schedule(new DownloadTask(),
				Date.from(firstTimeDownload.atZone(ZoneId.systemDefault()).toInstant()),
				contenu.getTotalDurationOfVideo() * 1000);
	}

	private class DownloadTask extends TimerTask {

		@Override
		public void run() {
			setDownload(!isDownload());
			System.out.println(isDownload());
		}
	}

	public AbstractModel getModel() {
		return model;
	}

	public void setInfoControler(Barre_info infoControler) {
		this.infoControler = infoControler;
	}

	public boolean isSleep() {
		return sleep;
	}

	public void setSleep(boolean sleep) {
		this.sleep = sleep;
	}

	public Veille getVeille() {
		return veille;
	}

	public Horloge getHorloge() {
		return horloge;
	}

	public Contenu getContenu() {
		return contenu;
	}

	public void setContenu(Contenu contenu) {
		this.contenu = contenu;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
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

	/**
	 * Cette méthode met la valeur null à tous les compteurs de l'application
	 */
	public abstract void controlVeille();
}
