package oceanbox.videoplayer;

import java.io.IOException;

public class JVlcPlayer implements VideoPlayer {

	private String cmd = "omxplayer";
	Process vlcPlayerProcess;

	public JVlcPlayer() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				stopPlayerProcess();
			}
		});
	}

	public Process play(String videoPath, int time) {
		cmd = "/Applications/VLC.app/Contents/MacOS/VLC";
		//cmd = "vlc";
		cmd += " --start-time=" + time + " ";
		cmd += videoPath;
		ProcessBuilder playerBuilder = new ProcessBuilder("sh", "-c", cmd);
		//ProcessBuilder playerBuilder = new ProcessBuilder("CMD", "-c", cmd);
		try {
			vlcPlayerProcess = playerBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vlcPlayerProcess;
	}

	public void stopPlayerProcess() {
		if (vlcPlayerProcess != null) {
			vlcPlayerProcess.destroy();
			vlcPlayerProcess=null;
		}
	}


}
