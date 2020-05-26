package oceanbox.videoplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Cette classe représente une vidéo en objet Java pour pouvoir appeler
 * certaines méthodes dessus
 */
public class Video {

	private String path;
	private int duration;
	private String name;
	private String extension;
	private int numero;
	private String date;

	public Video(String path) {
		this.path = path;
		initNameAttribute();
		initVideoDuration();
	}

	/**
	 * Cette méthode récupère la durée de la vidéo grâce au programme " mediainfo "
	 * et l'affecte à l'attribut de classe correspondant
	 */
	public void initVideoDuration() {

		// Pour l'application en .jar il ne faut pas mettre le chemin absolu
		// String cmd = "mediainfo --Inform=" + '\'' + "Video" + ";" + "%Duration%" + '\'' + " " + this.path;

		// Sinon ici il faut mettre le chemin absolu vers mediainfo
		String cmd = "/usr/local/bin/mediainfo --Inform=" + '\'' + "Video" + ";" + "%Duration%" + '\'' + " "
				+ this.path;

		ProcessBuilder processbuild = new ProcessBuilder("sh", "-c", cmd);
		long durationInMillisec = 0;
		try {
			Process p = processbuild.start();
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			durationInMillisec = Long.parseLong(reader.readLine());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		int durationInSec = (int) (durationInMillisec / 1000);
		this.duration = durationInSec;
	}

	/**
	 * Cette méthode partitionne le nom de fichier de la vidéo pour initialiser les
	 * attributs de classe correspondant
	 */
	private void initNameAttribute() {

		String[] splitOnSlash = path.split("\\\\|/");
		this.name = splitOnSlash[splitOnSlash.length - 1];
		String[] nameSplit = this.name.split("\\.");
		this.extension = nameSplit[1];
		String[] beginSplit = nameSplit[0].split("_");
		this.numero = Integer.parseInt(beginSplit[1]);
		this.date = beginSplit[0];
	}

	@Override
	public String toString() {
		return "Video [path=" + path + ", duration=" + duration + ", name=" + name + ", extension=" + extension
				+ ", numero=" + numero + ", date=" + date + "]";
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
