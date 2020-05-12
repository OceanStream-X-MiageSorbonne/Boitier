package oceanbox.videoplayer;

public interface VideoPlayer {
	
	public Process play(String videoPath, int time);
	public void stopPlayerProcess();
	
}
