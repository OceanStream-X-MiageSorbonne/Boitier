package oceanbox.videoplayer;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import oceanbox.propreties.SystemPropreties;

public class VideosInfos {
	private File[] videos;
	private Map<Integer, Video> videosInfos;

	public VideosInfos() {
		grepVideos();
		setInfos();
	}

	private void setInfos() {
		videosInfos = new TreeMap<Integer, Video>();
		for (File videoFile : videos) {
			if (videoFile.getName().startsWith("."))
				continue;
			Video v = new Video(videoFile.getAbsolutePath());
			videosInfos.put(v.getNumero(), v);
		}
	}

	private void grepVideos() {
		videos = new File(SystemPropreties.getPropertie("videoPath")).listFiles();
	}

	public File[] getAllVideos() {
		return (videos);
	}

	public Map<Integer, Video> getVideosInfos() {
		return (videosInfos);
	}

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
		for(Video v : videosInfos.values()) {
			str.append(v.toString() + "\n");
		}
		return str.toString();
	}
	
	
}
