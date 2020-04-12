package oceanbox.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.util.Duration;
import oceanbox.model.Contenu;
import oceanbox.propreties.ClientPropreties;

public class ContenuTest {

	private static final int secondsForTest = 12 * 3600;
	private static Contenu contenu;

	@BeforeClass
	public static void setUpBeforeClass() {
		contenu = new Contenu(secondsForTest);
	}

	@AfterClass
	public static void tearDownAfterClass() {
		contenu = null;
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
	public final void testRepereForDiffusion() {
		int currently = (LocalDateTime.now().getHour() * 3600) + (LocalDateTime.now().getMinute() * 60)
				+ LocalDateTime.now().getSecond();

		// L'heure de réveil par défaut est 6h du matin d'où : 6 x 3600
		Duration result = Duration.seconds((currently - (6 * 3600)) % secondsForTest);

		assertEquals(contenu.repereForDiffusion(), result);
	}
}
