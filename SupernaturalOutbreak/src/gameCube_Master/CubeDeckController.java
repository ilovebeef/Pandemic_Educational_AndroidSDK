package gameCube_Master;
import java.util.Stack;


public class CubeDeckController {
	private CubeDeckModel model;
	private CubeDeckView view;
	
	public CubeDeckController(CubeDeckModel model, CubeDeckView view){
		this.model = model;
		this.view = view;
	}
	
	public void setCubeDeck (String color, int numberOfcubes){
		model.setDeck(color, numberOfcubes);
	}
	
	public Stack<CubeModel> getCubeDeck (){
		return model.getDeck();
	}
	
	public void updateView(){
		view.printCubeDeckDetails(model.getDeck());
	}
	
	public String getCubeColor() {
		return model.getCubeColor();
	}
	
	public Boolean isEmpty() {
		return model.isEmpty();
	}
	
	public CubeModel popCube() {
		return model.popCube();
	}
	public void pushCube(CubeModel cube) {
        model.pushCube(cube);
	}

}
