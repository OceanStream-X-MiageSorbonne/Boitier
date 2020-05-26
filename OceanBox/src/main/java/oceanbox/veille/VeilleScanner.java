package oceanbox.veille;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import oceanbox.propreties.ClientPropreties;
import oceanbox.system.Contenu;

/**
 * Cette classe est une implémentation de Veille pour une entrée clavier
 */
public class VeilleScanner implements Veille {

	private Contenu contenu;
	private Timer timeBeforeVeille;
	private Boolean sleepMode;

	public VeilleScanner(Contenu c) {
		this.contenu = c;
		this.sleepMode = false;
		Thread VeilleScannerThread = new Thread(() -> {
			if (ClientPropreties.getPropertie("activateStandby").equals("1"))
				initVeille();
			Scanner sc = new Scanner(System.in);
			String entry = "";
			while (!entry.equals("exit")) {
				entry = sc.next();
				if (entry.equals("sleep"))
					goInVeille();
				else
					update();
			}
			sc.close();
			System.exit(0);
		});

		VeilleScannerThread.start();
	}

	@Override
	public void goInVeille() {
		sleepMode = true;
		contenu.stopDiffusion();

	}

	@Override
	public void update() {
		if (!sleepMode && ClientPropreties.getPropertie("activateStandby").equals("1")) {
			pushVeille();
		} else {
			goOutVeille();
		}
	}

	@Override
	public void goOutVeille() {
		if (ClientPropreties.getPropertie("activateStandby").equals("1"))
			initVeille();
		sleepMode = false;
	}

	@Override
	public void pushVeille() {
		timeBeforeVeille.cancel();
		initVeille();
	}

	@Override
	public void initVeille() {
		timeBeforeVeille = new Timer();
		timeBeforeVeille.schedule(new VeilleTask(), initMiliSecondsBeforeClose());
	}

	private long initMiliSecondsBeforeClose() {
		String[] times = ClientPropreties.getPropertie("timeBeforeStandby").split(":");
		return 1000 * ((Integer.parseInt(times[0]) * 3600) + (Integer.parseInt(times[1]) * 60)
				+ Integer.parseInt(times[2]));
	}

	private class VeilleTask extends TimerTask {
		@Override
		public void run() {
			goInVeille();
		}
	}

	@Override
	public boolean isSleepMode() {
		return sleepMode;
	}

}
