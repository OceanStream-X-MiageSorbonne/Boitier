package oceanbox.veille;

import java.util.Timer;
import java.util.TimerTask;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import oceanbox.propreties.ClientPropreties;
import oceanbox.system.Contenu;

/**
 * Cette classe est une implémentation de Veille pour un détecteur de mouvement
 */
public class VeilleDMV implements Veille {

	private Contenu contenu;
	private Timer timeBeforeVeille;
	private Boolean sleepMode;

	public VeilleDMV(Contenu c) {
		contenu = c;
		this.sleepMode = false;
		if (ClientPropreties.getPropretie("activateStandby").equals("true")) {
			initMotionSensorListner();
		}
		
			
	}

	private void initMotionSensorListner() {
		initVeille();
		
		System.out.println("Starting Pi4J Motion Sensor Example");					
		
		// create gpio controller			
		final GpioController gpioSensor = GpioFactory.getInstance(); 
		final GpioPinDigitalInput sensor = gpioSensor.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);			
		System.out.println(sensor.getPin());
		// create and register gpio pin listener			
		sensor.addListener(new GpioPinListenerDigital() {			
		    @Override		
		    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {		 		
		    	if(event.getState().isHigh()){	
		            System.out.println(">>> Motion Detected!");
		            update();
		        }					
		    }		
		});			
	}

	@Override
	public void goInVeille() {
		System.out.println(">>> Into veille");
		sleepMode = true;
		contenu.stopDiffusion();
	}

	@Override
	public void update() {
		if (!sleepMode && ClientPropreties.getPropretie("activateStandby").equals("true")) {
			pushVeille();
		} else {
			goOutVeille();
		}
	}

	@Override
	public void goOutVeille() {
		System.out.println(">>> get out");
		if (ClientPropreties.getPropretie("activateStandby").equals("true"))
			initVeille();
		sleepMode = false;
	}

	@Override
	public void pushVeille() {
		System.out.println(">>> push");
		timeBeforeVeille.cancel();
		initVeille();
	}

	@Override
	public void initVeille() {
		System.out.println(">>> Init veille");
		timeBeforeVeille = new Timer();
		timeBeforeVeille.schedule(new VeilleTask(), initMiliSecondsBeforeClose());
	}

	private long initMiliSecondsBeforeClose() {
		String[] times = ClientPropreties.getPropretie("timeBeforeStandby").split(":");
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
