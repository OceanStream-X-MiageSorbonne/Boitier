package oceanbox.videoplayer;

import java.io.IOException;

import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe est une implémentation de VideoPlayer pour VLC
 */
public class JVlcPlayer implements VideoPlayer {

	private String cmd;
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

		// Ici il faut mettre le chemin absolu vers VLC
		//cmd = "/Applications/VLC.app/Contents/MacOS/VLC";
		cmd = SystemPropreties.getPropretie("VlcCmd");	
		cmd += " --start-time=" + time + " ";
		cmd += videoPath + " -f";

		// Pour Windows il faut mettre "CMD", sinon il faut mettre "sh"
		// ProcessBuilder playerBuilder = new ProcessBuilder("CMD", "-c", cmd);
		ProcessBuilder playerBuilder = new ProcessBuilder("sh", "-c", cmd);

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
			vlcPlayerProcess = null;
		}
	}
}
