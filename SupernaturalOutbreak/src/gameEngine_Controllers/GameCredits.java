/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import java.util.List;

import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameInput_interfaceView.TouchEvent;

public class GameCredits extends GameScreen_View {
	boolean killAll = true;  // close game entirely if you hit the back button;
	List<TouchEvent> touchEvents;
	int len = 0;
	TouchEvent event1;
	public GameCredits (Game_interfaceView game) {
		super (game);
		GameAssets.warriorDrums = game.getAudio().newMusic("Audio/warriordrums.mp3");
		GameAssets.warriorDrums.play();
		GameAssets.warriorDrums.setLooping(true);
	}
	@Override
    public void update(float deltaTime) {
		
		touchEvents = game.getInput().getTouchEvents();
        //game.getInput().getKeyEvents();
        
        len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            event1 = touchEvents.get(i);
            if(event1.type == TouchEvent.TOUCH_UP) {
            	if (inBounds (event1, 0, 320-50, 50, 50)) {
            		killAll = false;
                    game.setScreen(new GameMainMenu(game));
                    GameAssets.mainMenu_Click.play(1);
                }
            }
        }
    }
	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }
    @Override
    public void present(float deltaTime) {
        GameGraphic_interfaceView g = game.getGraphics();      
        g.drawPixmap(GameAssets.creditScreen, 0, 0);
        g.drawPixmap(GameAssets.backButton, 0, 320-50);
    }

    @Override
    public void pause() {
    	GameAssets.warriorDrums.pause();
    }

    @Override
    public void resume() {
    	GameAssets.warriorDrums.play();
    }

    @Override
    public void dispose() {
    	GameAssets.warriorDrums.dispose();
    	if (killAll == true) {
    		System.exit(0);
    	}
    }
}
