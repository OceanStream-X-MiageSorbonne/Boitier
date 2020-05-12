package oceanbox.veille;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import oceanbox.model.Contenu;
import oceanbox.propreties.ClientPropreties;

public class VeilleScanner implements Veille {

	private Contenu contenu;
	private Timer timeBeforeVeille;
	private Boolean sleepMode;

	public VeilleScanner(Contenu c) {
		this.contenu = c;
		this.sleepMode = false;
		Thread VeilleScannerThread = new Thread(() -> {
			Scanner sc = new Scanner(System.in);
			String entry = "x";
			while (!entry.equals("exit")) {
				entry = sc.next();
				update();
			}
			sc.close();
		});

		VeilleScannerThread.start();
		System.out.println("test");
	}

	@Override
	public void goInVeille() {
		System.out.println("--goInVeille");
		sleepMode=true;
		contenu.stopDiffusion();

	}

	@Override
	public void update() {
		System.out.println("--Update");
		if (!sleepMode) {
			pushVeille();
		} else {
			goOutVeille();
		}
	}

	@Override
	public void goOutVeille() {
		System.out.println("--goOutVeille");
		initVeille();
		sleepMode=false;
	}

	@Override
	public void pushVeille() {
		System.out.println("--PushVeille");
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
		return 1000*((Integer.parseInt(times[0]) * 3600) + (Integer.parseInt(times[1]) * 60) + Integer.parseInt(times[2]));
	}

	private class VeilleTask extends TimerTask {
		@Override
		public void run() {
			sleepMode=true;
			contenu.stopDiffusion();
		}
	}
	
	@Override
	public boolean isSleepMode() {
		return sleepMode;
	}

}
