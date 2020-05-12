package oceanbox;

import javafx.scene.media.Media;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe permet de récupérer la vidéo qui sera diffusée à l'écran
 */
public class Recup_video {

	private Media video;

	public Recup_video(String fileName) {
		this.video = new Media("file:" + SystemPropreties.getPropertie("videoPath") + fileName);
	}

	public Media getVideo() {
		return video;
	}
}