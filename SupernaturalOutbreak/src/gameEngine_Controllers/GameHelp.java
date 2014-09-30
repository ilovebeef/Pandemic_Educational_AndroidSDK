/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import java.util.List;


import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameInput_interfaceView.TouchEvent;

public class GameHelp extends GameScreen_View {
	boolean killAll = true;  // If you press the back button, completely exit the game
	List<TouchEvent> touchEvents;
	int len = 0;
	TouchEvent event1;
	int page = 0;
	GameGraphic_interfaceView g = game.getGraphics();
	
	public GameHelp (Game_interfaceView game) {
		super (game);
	}
	@Override
    public void update(float deltaTime) {
		
        touchEvents = game.getInput().getTouchEvents();
        //game.getInput().getKeyEvents();
        
        len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            event1 = touchEvents.get(i);
            if(event1.type == TouchEvent.TOUCH_UP) {
            	// left arrow action
            	if (inBounds (event1, 0, 320-50, 50, 50)) {
            		killAll = false;
                    GameAssets.mainMenu_Click.play(1);
            		switch(page) {
            		case 0:
            			game.setScreen(new GameMainMenu(game));
            			break;
            		case 1:
            			page = 0;
            			break;
            		case 2:
            			page = 1;
            			break;
            		}
                }
            	// right arrow action
            	if( inBounds(event1, 430, 320-50, 50, 50)){
                    killAll = false;
                    GameAssets.mainMenu_Click.play(1);
                    switch(page) {
                    case 0:
                    	page = 1;
                    	break;
                    case 1:
                    	page = 2;
                    	break;
                    case 2: // exit to main menu, why should we go all the way back?
                    	game.setScreen(new GameMainMenu(game));
            			break;
                    }
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
    	switch (page) {
    	case 0:
    		g.drawPixmap(GameAssets.helpScreen, 0, 0);
    		break;
    	case 1:
    		g.drawPixmap(GameAssets.pandemic_directions, 0, 0);
    		break;
    	case 2:
    		g.drawPixmap(GameAssets.pandemic_directions2, 0, 0);
    		break;
    	}
        g.drawPixmap(GameAssets.backButton, 0, 320-50);
        g.drawPixmap(GameAssets.nextButton, 430, 320-50);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    	if (killAll == true) {
    		System.exit(0);
    	}
    }
}
