package oceanbox;

import javafx.scene.media.Media;

public class Recup_video {

	private Media video;

	public Recup_video(String fileName) {
		// ici il faut mettre votre propre chemin absolu jusqu'à la vidéo
		String specificPath = "file:/Users/abdelbenamara/Movies/OceanBox/";
		this.setVideo(new Media(specificPath + fileName));
	}

	public Media getVideo() {
		return video;
	}

	public void setVideo(Media video) {
		this.video = video;
	}
}