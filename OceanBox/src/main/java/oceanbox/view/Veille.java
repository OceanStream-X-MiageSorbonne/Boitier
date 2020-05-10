package oceanbox.view;

import java.util.TimerTask;

import oceanbox.controler.AbstractControler;

public class Veille extends TimerTask {

	private AbstractControler controler;

	@Override
	public void run() {
		controler.stopDiffusion();
	}

	public Veille(AbstractControler controler) {

		this.controler = controler;
		// TODO
	}
}
