/**
 * Created and Implemented by: Hong Nguyen
**/
package gameUser_Settings;

public class GameSetting_Model {
	private String Difficulty;
	private String[] gameDifficulty_Name;
	private String[] gamePlayers_Count;
	private int [] gameDifficulty_Number;
	private int Players;
	private int maxPlayers;
	
	public GameSetting_Model() {
		this.Difficulty = "";
		this.gameDifficulty_Name = new String[] {"Easy", "Normal", "Hard"};
		this.gamePlayers_Count = new String[] {"2 Players", "3 Players", "4 Players"};
		this.gameDifficulty_Number = new int[] {4, 5, 6};
		this.Players = 0;
		this.maxPlayers = 4;
	}
		//return the available game difficulty 
	public String getGame_difficultyName (int _index) {
		return gameDifficulty_Name[_index];
	}
		// return the available number of players
	public String getGame_PlayersCount (int _index) {
		return gamePlayers_Count[_index];
	}
	public int getGame_DifficultyNumber (int _index) {
		return gameDifficulty_Number[_index]; 
	}
		//get the selected difficulty
	public String getDifficulty () {
		return Difficulty;
	}
		//get current player playing
	public int getPlayers () {
		return Players;
	}
		//get max allowed players
	public int getMax_Players () {
		return maxPlayers;
	}
		//set all settings at once
	public void setGame_Setting (String _difficulty, int _players) {
		Difficulty = _difficulty;
		Players = _players;
	}
}
