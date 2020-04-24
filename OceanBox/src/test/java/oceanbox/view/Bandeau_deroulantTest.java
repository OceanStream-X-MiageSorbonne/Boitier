package oceanbox.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.util.Duration;
import oceanbox.view.info.Bandeau_deroulant;

public class Bandeau_deroulantTest {

	private static final int textLength = 1500;
	private static Label info;
	private static Bandeau_deroulant bandeau;

	@BeforeClass
	public static void setUpBeforeClass() {
		new JFXPanel();
		StringBuilder sentence = new StringBuilder();
		for (int i = 0; i < textLength; i++)
			sentence.append("c");
		info = new Label(sentence.toString());
	}

	@AfterClass
	public static void tearDownAfterClass() {
		info = null;
	}

	@Before
	public void setUp() {
		bandeau = new Bandeau_deroulant(info);
	}

	@After
	public void tearDown() {
		bandeau = null;
	}

	@Test
	public final void testVitesseDefilement() {
		assertTrue(bandeau.vitesseDefilement() > 0.0);
		assertTrue(bandeau.vitesseDefilement() <= 1.0);
	}

	@Test
	public final void testInitDefilement() {
		assertEquals(bandeau.getDefilement().getFirst().getDuration(), Duration.seconds(2));
	}
}
