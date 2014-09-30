package gameCube_Master;
import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameGraphic_interfaceView.PixmapFormat;

import java.util.Stack;
public class CubeDeckModel {
	private Stack <CubeModel> cubeDeck;
	private String color;
	private GameGraphic_interfaceView gameGraphic;
	
	public CubeDeckModel(String color, int numberOfCubes, Game_interfaceView game) {
		this.gameGraphic = game.getGraphics();
		this.cubeDeck = new Stack<CubeModel>();      //create a empty cubeDeck
						//store the color in the temporary cube
		this.color = color;							// store the color for the entire deck
		for (int i = 0; i < numberOfCubes; i++){	//store the temporary cube as many as players want to the cubedeck
			CubeModel tempCube = new CubeModel ();		//create a temporary cube
			tempCube.setColor(color);	
			tempCube.setImage(this.gameGraphic.newPixmap(("Cubes/" + color + "Cube_0.png"), PixmapFormat.ARGB4444));
			tempCube.setBigImage(this.gameGraphic.newPixmap(("Cubes/" + color + "Cube_1.png"), PixmapFormat.ARGB4444));
			this.cubeDeck.push(tempCube);
		}
	}
	
	public void setDeck(String color, int numberOfcubes){
		this.cubeDeck = new Stack<CubeModel>();      //create a empty cubeDeck
		CubeModel tempCube = new CubeModel ();		//create a temporary cube
		tempCube.setColor(color);					//store the color in the temporary cube
		for (int i = 0; i < numberOfcubes; i++){	//store the temporary cube as many as players want to the cubedeck
			this.cubeDeck.push(tempCube);
		}
	}
	
	public Stack<CubeModel> getDeck(){
		return this.cubeDeck;
	}
	
	public CubeModel popCube(){
		if (this.cubeDeck.empty()) {
			System.out.println("No more cubes in this color");
			CubeModel failCube = new CubeModel();
			failCube.setColor("fail");
			failCube.setImage(this.gameGraphic.newPixmap("play_icon.png", PixmapFormat.ARGB4444));
			return failCube;
		}
		else
			return this.cubeDeck.pop();
	}
	
	public void pushCube(CubeModel cube){
		if (this.color.compareTo(cube.getColor()) == 0)
			this.cubeDeck.push(cube);
        else
        	System.out.println("Color unmatched");
	}
	
	public boolean isEmpty(){
		if (this.cubeDeck.empty())
			return true;
		else
			return false;
	}
	
	public String getCubeColor() {
		return this.color;
	}
}
