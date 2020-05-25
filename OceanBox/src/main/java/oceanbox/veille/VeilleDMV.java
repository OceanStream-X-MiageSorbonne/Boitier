package oceanbox.veille;

import java.util.Timer;
import java.util.TimerTask;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import oceanbox.propreties.ClientPropreties;
import oceanbox.system.Contenu;

public class VeilleDMV implements Veille {

	private Contenu contenu;
	private Timer timeBeforeVeille;
	private Boolean sleepMode;

	public VeilleDMV(Contenu c) {
		contenu = c;
		if (ClientPropreties.getPropertie("activateStandby").equals("1"))
			initMotionSensorListner();
	}

	private void initMotionSensorListner() {
		initVeille();
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalInput motionDetector = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		motionDetector.setShutdownOptions(true);
		motionDetector.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				if (event.getState().equals(PinState.HIGH)) {
					System.out.println(">>> Motion detected");
					update();
				}
			}
		});
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
