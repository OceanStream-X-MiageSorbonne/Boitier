package oceanbox.videoplayer;

import java.io.IOException;

/**
 * Cette classe est une implémentation de VideoPlayer pour OmxPlayer
 */
public class JOmxPlayer implements VideoPlayer {

	private String cmd;
	Process omxPlayerProcess;

	public JOmxPlayer() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				stopPlayerProcess();
			}
		});
	}

	public Process play(String videoPath, int time) {

		cmd = "omxplayer";
		cmd += " --advanced";
		cmd += " --pos " + time + " ";
		cmd += " --aspect-mode fill ";
		cmd += " " + videoPath;

		ProcessBuilder playerBuilder = new ProcessBuilder("sh", "-c", cmd);

		try {
			omxPlayerProcess = playerBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return omxPlayerProcess;
	}

	public void stopPlayerProcess() {
		if (omxPlayerProcess != null) {
			try {
				omxPlayerProcess.getOutputStream().write('q');
				omxPlayerProcess.getOutputStream().flush();
				// omxPlayerProcess.destroy();
				omxPlayerProcess = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
