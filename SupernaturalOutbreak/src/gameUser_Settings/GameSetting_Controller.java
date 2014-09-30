/**
 * Created and Implemented by: Hong Nguyen
**/
package gameUser_Settings;

public class GameSetting_Controller {
	private GameSetting_Model gsModel;
	private GameSetting_View gsView;
	public GameSetting_Controller () {
		gsModel = new GameSetting_Model ();
		gsView = new GameSetting_View ();
	}
	public void setGSmodel(String _difficulty, int _players) {
		gsModel.setGame_Setting(_difficulty, _players);
	}
	public void setGSview (String _difficulty, int _players) {
		gsView.printGame_Settings(_difficulty, _players);
	}
	public String getGSmodel_Difficulty () {
		return gsModel.getDifficulty();
	}
	public String getGSmodel_difficultyName (int _index) {
		return gsModel.getGame_difficultyName(_index);
	}
	public String getGSmodel_playersCount(int _index) {
		return gsModel.getGame_PlayersCount(_index);
	}
	public int getGSmodel_difficultyNumber (int _index) {
		return gsModel.getGame_DifficultyNumber(_index);
	}
	public int getGSmodel_Players () {
		return gsModel.getPlayers ();
	}
	public int getGSmodel_maxPlayers () {
		return gsModel.getMax_Players();
	}
}
