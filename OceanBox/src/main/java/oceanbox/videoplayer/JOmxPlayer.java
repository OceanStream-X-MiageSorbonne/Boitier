package oceanbox.videoplayer;

import java.io.IOException;

public class JOmxPlayer {

	private String cmd = "omxplayer";
	Process omxPlayerProcess;

	public JOmxPlayer() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				JOmxPlayer.this.stop();
			}
		});
	}

	public Process play(String VideoPath, String time) {
		cmd += " --advanced";
		cmd += " --pos " + time + " ";
		cmd += " --aspect-mode fill ";
		cmd += " " + VideoPath;
		ProcessBuilder playerBuilder = new ProcessBuilder("sh", "-c", cmd);
		try {
			omxPlayerProcess = playerBuilder.start();
		} catch (IOException  e) {
			e.printStackTrace();
		}
		return omxPlayerProcess;
	}

	public void stop() {
		if (omxPlayerProcess != null) {
			try {
				omxPlayerProcess.getOutputStream().write('q');
				omxPlayerProcess.getOutputStream().flush();
				omxPlayerProcess = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
