/*
 * Programmer: David Bloss
 */

package gameBoard_Master;

import gameCube_Master.CubeModel;
import gamePlayer_Controller.Player;

public class CityController {
	private City model;
	private CityView view;
		
	public CityController(City model, CityView view) {
		this.model = model;
        this.view = view;
    }
    
    public String getCityName() {
            return model.getName();
    }
    
    public String getCityColor() {
        return model.getColor();
    }

    public String[] getCityConnetions(){
    	return model.getConnections();
    }
    
    public boolean addCube(CubeModel cube){
    	return model.addCube(cube);
    }
    
    public CubeModel removeCube(String color){
    	return model.removeCube(color);
    }
    
    	// overloaded removeCube() until preference chosen
    public boolean removeCube(CubeModel cube){
    	return model.removeCube(cube);
    }

    public int getBlackCubeCount(){
    	return model.getBlackCubeCount();
    }
    
    public int getBlueCubeCount(){
    	return model.getBlueCubeCount();
    }
    
    public int getRedCubeCount(){
    	return model.getRedCubeCount();
    }
    
    public int getYellowCubeCount(){
    	return model.getYellowCubeCount();
    }
    
    public int getXCoordinate(){
    	return model.getXCoordinate();
    }
    
    public int getYCoordinate(){
    	return model.getYCoordinate();
    }
    
    public void addResearchCenter(){
    	model.buildResearchStation();
    }
    
    public boolean getResearchCenterStatus(){
    	return model.getResearchCenterStatus();
    }
    
    public void addPlayerToCity(Player player){
    	model.addPlayer(player);
    }
    
    public Player removePlayerFromCity(Player player) {
    	return model.removePlayer(player);
    }							
    
    public void updateView() {
            view.printCityDetails(model.getName(), model.getColor(), model.getConnections(), 
            					  model.getBlackCubeCount(), model.getBlueCubeCount(), 
            					  model.getRedCubeCount(), model.getYellowCubeCount(),   
            					  model.getXCoordinate(), model.getYCoordinate(), 
            					  model.getResearchCenterStatus());
    }
}
