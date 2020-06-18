package oceanbox.videoplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import oceanbox.propreties.SystemPropreties;

public class MediaInfo {
	
	private String mediaInfoCmd;

	public MediaInfo() {
		this.mediaInfoCmd = SystemPropreties.getPropretie("MediaInfoCMD");
	}

	public int getDuration(String path) {

		String cmd = mediaInfoCmd + " --Inform=" + '\'' + "Video" + ";" + "%Duration%" + '\'' + " " + path;

		ProcessBuilder processbuild = new ProcessBuilder("sh", "-c", cmd);
		long durationInMillisec = 0;
		try {
			Process p = processbuild.start();
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			durationInMillisec = Long.parseLong(reader.readLine());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		int durationInSec = (int) (durationInMillisec / 1000);
		return durationInSec;
	}

}
