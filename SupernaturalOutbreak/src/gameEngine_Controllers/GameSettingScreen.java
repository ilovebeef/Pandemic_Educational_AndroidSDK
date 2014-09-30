/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameInput_interfaceView.TouchEvent;
import gameUser_Settings.GameSetting_Controller;
import java.util.List;
import android.view.View;

public class GameSettingScreen extends GameScreen_View {
		//Model-View-Controller for GameSetting_Controller
	GameSetting_Controller gameSetting;
	boolean radioToggle_Easy, radioToggle_Normal, radioToggle_Hard, 
			radioToggle_2Players, radioToggle_3Players, radioToggle_4Players;
	int difficulty, numberOfPlayers;
	boolean killAll = true;
	List<TouchEvent> touchEvents;
	int len = 0;
	GameGraphic_interfaceView g;
	
	public GameSettingScreen (Game_interfaceView game) {
		super (game);
		gameSetting = new GameSetting_Controller ();
		radioToggle_Easy = true;
		radioToggle_Normal = false;
		radioToggle_Hard = false;
		radioToggle_2Players = true;
		radioToggle_3Players = false;
		radioToggle_4Players = false;
		difficulty = 4; // scale is 4 is easy, 5 is normal, 6 is hard.  This is due to how the deck is setup.
		numberOfPlayers = 2;
		g = game.getGraphics();
	}
		
	@Override
	public void update(float deltaTime) {
		touchEvents = game.getInput().getTouchEvents();
        //game.getInput().getKeyEvents();
        
        len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if (inBounds (event, 130, 90, 50, 50)) {
            		GameAssets.settings_Click.play(1);
            		radioToggle_Easy = true;
            		radioToggle_Normal = false;
            		radioToggle_Hard = false;
            		difficulty = 4;
                }
            	if (inBounds (event, 220, 90, 50, 50)) {
            		GameAssets.settings_Click.play(1);
            		radioToggle_Easy = false;
            		radioToggle_Normal = true;
            		radioToggle_Hard = false;
            		difficulty = 5;
                }
            	if (inBounds (event, 310, 90, 50, 50)) {
            		GameAssets.settings_Click.play(1);
            		radioToggle_Easy = false;
            		radioToggle_Normal = false;
            		radioToggle_Hard = true;
            		difficulty = 6;
                }
            	if (inBounds (event, 130, 190, 50, 50)) {
            		GameAssets.settings_Click.play(1);
            		radioToggle_2Players = true;
            		radioToggle_3Players = false;
            		radioToggle_4Players = false;
            		numberOfPlayers = 2;
            	}
            	if (inBounds (event, 220, 190, 50, 50)) {
            		GameAssets.settings_Click.play(1);
            		radioToggle_2Players = false;
            		radioToggle_3Players = true;
            		radioToggle_4Players = false;
            		numberOfPlayers = 3;
            	}
            	if (inBounds (event, 310, 190, 50, 50)) {
            		GameAssets.settings_Click.play(1);
            		radioToggle_2Players = false;
            		radioToggle_3Players = false;
            		radioToggle_4Players = true;
            		numberOfPlayers = 4;
            	}
            	if (inBounds (event, 207, 265 , 50, 50)) {
            		killAll = false;
            		game.setScreen(new GameMap(game, difficulty, numberOfPlayers));
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
		/**Will now create these radio button for the player
		 * to be able to select the different difficulty and
		 * amount of players
		**/
		
		g.drawPixmap(GameAssets.settingScreen,0,0);
		
		//player difficulty
		g.drawText(gameSetting.getGSmodel_difficultyName(0), 130, 90);
		g.drawText(gameSetting.getGSmodel_difficultyName(1), 220, 90);
		g.drawText(gameSetting.getGSmodel_difficultyName(2), 310, 90);
		// number of players
		g.drawText(gameSetting.getGSmodel_playersCount(0), 130, 190);
		g.drawText(gameSetting.getGSmodel_playersCount(1), 220, 190);
		g.drawText(gameSetting.getGSmodel_playersCount(2), 310, 190);
		// next button
		g.drawPixmap(GameAssets.playButton, 207, 265);
		
		//Control for radio toggle, simple toggles
		if (radioToggle_Easy == true) {
			g.drawPixmap(GameAssets.radioChecked, 130, 100);
		} 
		else {
			g.drawPixmap(GameAssets.radioUnchecked, 130, 100);
		}
		if (radioToggle_Normal == true) {
			g.drawPixmap(GameAssets.radioChecked, 220, 100);
		} 
		else {
			g.drawPixmap(GameAssets.radioUnchecked, 220, 100);
		}
		if (radioToggle_Hard == true) {
			g.drawPixmap(GameAssets.radioChecked, 310, 100);
		} 
		else {
			g.drawPixmap(GameAssets.radioUnchecked, 310, 100);
		}
		if (radioToggle_2Players == true) {
			g.drawPixmap(GameAssets.radioChecked, 130, 200);
		}
		else {
			g.drawPixmap(GameAssets.radioUnchecked, 130, 200);
		}
		if (radioToggle_3Players == true) {
			g.drawPixmap(GameAssets.radioChecked, 220, 200);
		}
		else {
			g.drawPixmap(GameAssets.radioUnchecked, 220, 200);
		}
		if (radioToggle_4Players == true) {
			g.drawPixmap(GameAssets.radioChecked, 310, 200);
		}
		else {
			g.drawPixmap(GameAssets.radioUnchecked, 310, 200);
		}
	}
		
	public void addListenerOnButton (View game) {
		
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