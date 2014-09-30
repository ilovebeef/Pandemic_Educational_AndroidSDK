/*
 * Programmer: David Bloss
 */

package gameBoard_Master;

public class CuresDiscoveredController {
	private CuresDiscovered model;
	private CuresDiscoveredView view;
	
	public CuresDiscoveredController(CuresDiscovered model, CuresDiscoveredView view) {
		this.model = model;
        this.view = view;
    }
		// Black
	public void setBlackCure(boolean found){
		model.setBlackCure(found);
	}
	
	public boolean getBlackCure(){
		return model.getBlackCure();
	}
	
		// Blue
	public void setBlueCure(boolean found){
		model.setBlueCure(found);
	}
	
	public boolean getBlueCure(){
		return model.getBlueCure();
	}
	
		// Red
	public void setRedCure(boolean found){
		model.setRedCure(found);
	}
	
	public boolean getRedCure(){
		return model.getRedCure();
	}
	
		// Yellow
	public void setYellowCure(boolean found){
		model.setYellowCure(found);
	}
	
	public boolean getYellowCure(){
		return model.getYellowCure();
	}
	public boolean checkColorCure(String color) {
		return model.checkColorCure(color);
	}
	
	public void printCuresDiscoveredDetails(boolean black, boolean blue, 
											boolean red, boolean yellow){
		
		view.printCuresDiscoveredDetails(black, blue, red, yellow);
	}
	public boolean checkForVictory() {
		return model.checkForVictory();
	}
}
