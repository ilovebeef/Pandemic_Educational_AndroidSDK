/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkView;





public interface Game_interfaceView {
	public GameInput_interfaceView getInput();
	
	public GameGraphic_interfaceView getGraphics();

    public GameAudio_interfaceView getAudio();
    
    //public GameBGM_interfaceView getBGM();

    public void setScreen(GameScreen_View _screen);

    public GameScreen_View getCurrentScreen();

    public GameScreen_View getStartScreen();

}
