/*
 * Programmer: David Bloss
 */

package gameBoard_Master;

import java.util.Stack;

import gamePlayer_Controller.Player;
import gameCube_Master.CubeModel;

public class City {

	private String cityName,
				   countryColor;
	private String [] connectedCities;		// Array of cities connected by flight paths
	private int blackCubeCount,
				blueCubeCount,
				redCubeCount,				// 3 same color cubes max
				yellowCubeCount,			// 12 total cubes max
				xCord,
				yCord;
	private boolean researchStation;		
	private Stack<Player> playerStack;
	private Stack<CubeModel> blackCubes,
							 blueCubes,
							 redCubes,
							 yellowCubes;
	
	  //************
	  //Constructors
	  //************
	public City(){
		this.cityName = "";
		this.countryColor = "";
		this.connectedCities = new String[0];
		this.blackCubeCount = 0;
		this.blueCubeCount = 0;
		this.redCubeCount = 0;
		this.yellowCubeCount = 0;
		this.researchStation = false;
		this.xCord = 0;
		this.yCord = 0;
		this.playerStack = new Stack<Player>();
		this.blackCubes = new Stack<CubeModel>();
		this.blueCubes = new Stack<CubeModel>();
		this.redCubes = new Stack<CubeModel>();
		this.yellowCubes = new Stack<CubeModel>();
	}
	
	City(String city, String [] paths, String color, int x, int y){
		this.cityName = city;
		this.countryColor = color;
		int counter = 0;
		for (int i = 0; i < 10; i++) {
			if (paths[i] != null) {
				counter++;
			}
		}
		this.connectedCities = new String[counter];  // declare space in memory for connections
		for (int i = 0; i < counter; i++) {
			this.connectedCities[i] = paths[i];		// put connections into array excluding null values
		}			
		this.blackCubeCount = 0;					// Up to 3 cubes of each color. At 3 cubes: Outbreak occurs
		this.blueCubeCount = 0;
		this.redCubeCount = 0;
		this.yellowCubeCount = 0;
		this.researchStation = false;				// Atlanta begins with research station, initialized in Map.java
		this.xCord = x;
		this.yCord = y;
		this.playerStack = new Stack<Player>();
		this.blackCubes = new Stack<CubeModel>();
		this.blueCubes = new Stack<CubeModel>();
		this.redCubes = new Stack<CubeModel>();
		this.yellowCubes = new Stack<CubeModel>();
	}
	
	  //************
	  //Mutators
	  //************
	
	public void buildResearchStation(){
		this.researchStation = true;
	}
	
	public void addPlayer(Player player){
		this.playerStack.push(player);
	}
	
	public Player removePlayer(Player player) {
		int size = playerStack.size();  // verifies number of times to loop through stack
		for (int i = 0; i < size; i++) {
			if (this.playerStack.get(i).getName().compareTo(player.getName()) == 0) {  // if they have the same name, they're the same player
				return playerStack.remove(i);  // remove the player from the stack, and give it to the caller.
			}
		}
		return player; // If the player isn't there, don't remove him, just give back what you originally sent in
	}
	
	public boolean addCube(CubeModel cube){
		String color = cube.getColor();
		
		if ((color.compareTo("black") == 0) && (this.blackCubeCount < 3)){
			this.blackCubes.push(cube);
			this.blackCubeCount++;
			return false;
		}
		else if ((color.compareTo("blue") == 0) && (this.blueCubeCount < 3)){
			this.blueCubes.push(cube);
			this.blueCubeCount++;
			return false;
		}
		else if ((color.compareTo("red") == 0 ) && (this.redCubeCount < 3)){
			this.redCubes.push(cube);
			this.redCubeCount++;
			return false;
		}
		else if ((color.compareTo("yellow") == 0 )&& (this.yellowCubeCount < 3)){
			this.yellowCubes.push(cube);
			this.yellowCubeCount++;
			return false;
		}
			// if true, calling function should initiate Outbreak
			// Note: be careful to not "consume" a cube since the City won't return it
		else
			return true;

	}
	
	public CubeModel removeCube(String color){
		
		if (color.equalsIgnoreCase("black") && (blackCubeCount > 0)){
			this.blackCubeCount--;
			return this.blackCubes.pop();
		}
		else if (color.equalsIgnoreCase("blue") && (blueCubeCount > 0)){
			this.blueCubeCount--;
			return this.blueCubes.pop();
		}
		else if (color.equalsIgnoreCase("red") && (redCubeCount > 0)){
			this.redCubeCount--;
			return this.redCubes.pop();
		}
		else if (color.equalsIgnoreCase("yellow") && (yellowCubeCount > 0)){
			this.yellowCubeCount--;
			return this.yellowCubes.pop();
		}
		else{
			return null;
		}

	}
	
		// overloading removeCube() until preference chosen
	public boolean removeCube(CubeModel cube){
		
		String color = cube.getColor();
		
		if (color.equalsIgnoreCase("black") && (blackCubeCount > 0)){
			this.blackCubeCount--;
			this.blackCubes.pop();
			return true;
		}
		else if (color.equalsIgnoreCase("blue") && (blueCubeCount > 0)){
			this.blueCubeCount--;
			this.blueCubes.pop();
			return true;
		}
		else if (color.equalsIgnoreCase("red") && (redCubeCount > 0)){
			this.redCubeCount--;
			this.redCubes.pop();
			return true;
		}
		else if (color.equalsIgnoreCase("yellow") && (yellowCubeCount > 0)){
			this.yellowCubeCount--;
			this.yellowCubes.pop();
			return true;  // return true if you were able to remove a cube
		}
		else{		// return false if you couldn't remove a cube
			return false;
		}	
	}
	
	  //************
	  //Observers
	  //************
	public String getName(){
		return this.cityName;
	}
	
	public String getColor(){
		return this.countryColor;
	}
	
	public String[] getConnections(){
		return this.connectedCities;
	}
	
	public int getBlackCubeCount(){
		return this.blackCubeCount;
	}
	
	public int getBlueCubeCount(){
		return this.blueCubeCount;
	}
	
	public int getRedCubeCount(){
		return this.redCubeCount;
	}
	
	public int getYellowCubeCount(){
		return this.yellowCubeCount;
	}
	
	public int getXCoordinate(){
		return this.xCord;
	}
	
	public int getYCoordinate(){
		return this.yCord;
	}
	
	public boolean getResearchCenterStatus(){
		return this.researchStation;				// elsewhere, list of all research stations??
	}

    public Stack<Player> getPlayerStack() {
        return playerStack;
    }
}
