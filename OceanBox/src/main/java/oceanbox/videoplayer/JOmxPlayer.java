package oceanbox.videoplayer;

import java.io.IOException;

public class JOmxPlayer implements VideoPlayer{

	private String cmd = "omxplayer";
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

//	public Process playVLC(String videoPath, int time) {
//		cmd = "/Applications/VLC.app/Contents/MacOS/VLC";
//		cmd += " --start-time=" + time + " ";
//		cmd += videoPath;
//		ProcessBuilder playerBuilder = new ProcessBuilder("sh", "-c", cmd);
//		try {
//			omxPlayerProcess = playerBuilder.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return omxPlayerProcess;
//	}

	public void stopPlayerProcess() {
		if (omxPlayerProcess != null) {
			omxPlayerProcess.destroy();
			omxPlayerProcess=null;
		}
	}

}
