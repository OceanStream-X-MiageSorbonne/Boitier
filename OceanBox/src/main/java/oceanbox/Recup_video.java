package oceanbox;

import javafx.scene.media.Media;
import oceanbox.propreties.SystemPropreties;

public class Recup_video {

	private Media video;

	public Recup_video(String fileName) {
		// ici il faut mettre votre propre chemin absolu jusqu'à la vidéo
		String specificPath = "file:"+SystemPropreties.getPropertie("videoPath");
		this.setVideo(new Media(specificPath + fileName));
	}

	public Media getVideo() {
		return video;
	}

	public void setVideo(Media video) {
		this.video = video;
	}
}