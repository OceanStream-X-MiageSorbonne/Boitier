package oceanbox.veille;

/**
 * Cette interface définit les méthodes de gestion de la veille de l'application
 */
public interface Veille {

	/**
	 * Cette méthode met l'application en veille
	 */
	public void goInVeille();

	/**
	 * Cette méthode met à jour les propriétés de veille de l'application
	 */
	public void update();

	/**
	 * Cette méthode sort l'application de l'état de veille
	 */
	public void goOutVeille();

	/**
	 * Cette méthode repousse la prochaine veille automatique
	 */
	public void pushVeille();

	/**
	 * Cette méthode initialise les propriétés de veille automatique
	 */
	public void initVeille();

	/**
	 * Cette méthode permet de savoir l'application est en veille
	 * 
	 * @return : true si l'application est en veille, false sinon
	 */
	public boolean isSleepMode();
}
