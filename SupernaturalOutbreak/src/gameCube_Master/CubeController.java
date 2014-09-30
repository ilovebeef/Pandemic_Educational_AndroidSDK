package gameCube_Master;
public class CubeController {
	private CubeModel model;
	private CubeView view;
	
	public CubeController (CubeModel model, CubeView view){
		this.model = model;
		this.view = view;
	}
	
	public void setCubeColor (String color){
		model.setColor(color);
	}
	
	public String getCubeColor (){
		return model.getColor();
	}
	
	public void updateView(){
		view.printCubeDetails(model.getColor());
	}
}
