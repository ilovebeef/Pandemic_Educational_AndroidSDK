/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import java.util.List;
import android.util.Log;
import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameInput_interfaceView.TouchEvent;

public class GameMainMenu extends GameScreen_View {
	boolean killAll = true;  // If you press back, we want to kill the game entirely
	int len = 0;
	GameGraphic_interfaceView g;
	List<TouchEvent> touchEvents;
	TouchEvent event1;
	
	public GameMainMenu (Game_interfaceView game) {
		super(game);
		Log.d ("GameMainMenu", "in Extend Screen Class");
		//a way to by-pass a bug of which the intent needs to extend activity
		GameAssets.mainMenu_BGM = game.getAudio().newMusic("Audio/mainMenu_BGM.mp3");
		GameAssets.mainMenu_BGM.play();
		GameAssets.mainMenu_BGM.setLooping(true);
		this.g = game.getGraphics();
		System.gc();
		System.gc();
	}
	public void update(float deltaTime) {
        touchEvents = game.getInput().getTouchEvents();
        //game.getInput().getKeyEvents();       
        
        len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            event1 = touchEvents.get(i);
            
            if(event1.type == TouchEvent.TOUCH_UP) {
            	if (inBounds (event1, (480/2)-25, (320/2)-30, 50, 50)) {
            		killAll = false;
            		game.setScreen(new GameSettingScreen(game));
            		GameAssets.mainMenu_Click.play(1);
            		System.gc();
            	}
            	if (inBounds (event1, (480/2)-25, (320/2)+30, 50, 50)) {
            		killAll = false;
            		game.setScreen(new GameHelp(game));
            		GameAssets.mainMenu_Click.play(1);
            		System.gc();
            	}
            	if (inBounds (event1, (480/2)-25, (320/2)+90, 50, 50)) {
            		killAll = false;
            		game.setScreen(new GameCredits(game));
            		GameAssets.mainMenu_Click.play(1);
            		System.gc();
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
	    	//Draw function, will constantly draw assets to the menu
	    public void present(float deltaTime) {
	        g.drawPixmap(GameAssets.menuScreen, 0, 0);
	        g.drawPixmap(GameAssets.playButton, (480/2)-25, (320/2)-30);
	        g.drawPixmap(GameAssets.helpButton, (480/2)-25, (320/2)+30);
	        g.drawPixmap(GameAssets.creditButton, (480/2)-25, (320/2)+90);
	    }

	    public void pause() {        
	    	GameAssets.mainMenu_BGM.pause();
	    	System.gc();
	    }

	    public void resume() {
	    	GameAssets.mainMenu_BGM.play();
	    	System.gc();
	    }

	    public void dispose() {
	    	GameAssets.mainMenu_BGM.dispose();
	    	System.gc();
	    	if (killAll == true) {
	    		System.exit(0);
	    	}
	    }
	    
}
