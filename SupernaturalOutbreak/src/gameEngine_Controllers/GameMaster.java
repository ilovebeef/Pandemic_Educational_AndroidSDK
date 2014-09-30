/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import gameEngine_frameworkModel.AndroidGame_Model;
import gameEngine_frameworkView.GameScreen_View;

public class GameMaster extends AndroidGame_Model {
		
		public GameScreen_View getStartScreen() {
			return new GameLoad (this);
		}

}
