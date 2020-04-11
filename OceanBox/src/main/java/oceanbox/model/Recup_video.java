package oceanbox.model;

import java.io.File;
import java.io.FilenameFilter;

import javafx.scene.media.Media;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe permet de récupérer la vidéo qui sera diffusée à l'écran
 */
public class Recup_video {

	private Media video;

	public Recup_video(String regex) {

		File dir = new File(SystemPropreties.getPropertie("videoPath"));
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(regex);
			}
		};

		String[] children = dir.list(filter);

		this.video = new Media("file:" + dir.getAbsolutePath() + "/" + children[0]);
	}

	public Media getVideo() {
		return video;
	}
}