package oceanbox;

import javafx.scene.media.Media;

public class Recup_video {

	private Media video;

	public Recup_video(String fileName) {
		this.setVideo(new Media(this.getClass().getResource("content/" + fileName).toExternalForm()));
	}

	public Media getVideo() {
		return video;
	}

	public void setVideo(Media video) {
		this.video = video;
	}
}