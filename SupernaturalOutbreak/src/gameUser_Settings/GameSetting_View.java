/**
 * Created and Implemented by: Hong Nguyen
**/
package gameUser_Settings;

import android.util.Log;

public class GameSetting_View {
	public GameSetting_View () {
		//does nothing
	}
	public void printGame_Settings (String _difficulty, int _players) {
			//will just print to log, because model info can be prompted directly to screen
		Log.d("Print Difficulty", "Difficulty: " + _difficulty + "\nPlayers: " +_players);
	}
}
