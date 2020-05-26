package oceanbox.videoplayer;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe permet d'accéder et d'intéragir avec n'importe quelle vidéo du
 * répertoire local
 */
public class VideosInfos {

	private File[] videos;
	private Map<Integer, Video> videosInfos;

	public VideosInfos() {
		initLocalVideos();
		initVideosInfos();
	}

	/**
	 * Cette méthode instancie un tableau de fichier des vidéos du répertoire local
	 */
	private void initLocalVideos() {
		videos = new File(SystemPropreties.getPropertie("videoPath")).listFiles();
	}

	/**
	 * Cette méthode initialise la Map de vidéos ayant pour clé leur numéro de vidéo
	 */
	private void initVideosInfos() {
		videosInfos = new TreeMap<Integer, Video>();
		for (File videoFile : videos) {
			if (videoFile.getName().startsWith("."))
				continue;
			Video v = new Video(videoFile.getAbsolutePath());
			videosInfos.put(v.getNumero(), v);
		}
	}

	/**
	 * Cette méthode permet d'avoir la durée totale cumulée de toutes les vidéos
	 * 
	 * @return : le temps cumulé en secondes
	 */
	public int getTotalDurationOfVideos() {
		int total = 0;

		for (int i : videosInfos.keySet()) {
			total += videosInfos.get(i).getDuration();
		}

		return total;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Video v : videosInfos.values()) {
			str.append(v.toString() + "\n");
		}
		return str.toString();
	}

	public File[] getAllVideos() {
		return (videos);
	}

	public Map<Integer, Video> getVideosInfos() {
		return (videosInfos);
	}
}
