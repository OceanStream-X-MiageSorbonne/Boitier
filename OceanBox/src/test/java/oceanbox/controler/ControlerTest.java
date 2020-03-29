package oceanbox.controler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import oceanbox.propreties.ClientPropreties;

public class ControlerTest {

	private static Controler controler;

	@BeforeClass
	public static void setUpBeforeClass() {
		controler = new Controler();
	}

	@AfterClass
	public static void tearDownAfterClass() {
		controler = null;
	}

	@Before
	public void setUp() throws FileNotFoundException, IOException {
		ClientPropreties.initProperties();
	}

	@After
	public void tearDown() {
		ClientPropreties.deletePropertiesFile();
	}

	@Test
	public final void testInitSecondsBeforeClose() throws FileNotFoundException, IOException {
		ClientPropreties.setPropertie("timeBeforeStandby", "03:27:35");

		assertEquals(controler.initSecondsBeforeClose(), 12455);
	}

	@Test
	public final void testSleepMode() {
		controler.sleepMode(false);
		assertEquals(ClientPropreties.getPropertie("onStandby"), "false");
		controler.sleepMode(true);
		assertEquals(ClientPropreties.getPropertie("onStandby"), "true");
	}
}
