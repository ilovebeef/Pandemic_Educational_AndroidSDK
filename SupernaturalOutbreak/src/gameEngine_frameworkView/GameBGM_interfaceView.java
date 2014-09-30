/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkView;

public interface GameBGM_interfaceView {
	public void play();

    public void stop();

    public void pause();

    public void setLooping(boolean looping);

    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isStopped();

    public boolean isLooping();

    public void dispose();
}
