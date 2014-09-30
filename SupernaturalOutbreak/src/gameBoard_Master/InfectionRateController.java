/*
 * Programmer: David Bloss
 */

package gameBoard_Master;

public class InfectionRateController {
	private InfectionRate model;
	private InfectionRateView view;
	
	public InfectionRateController(InfectionRate model, InfectionRateView view) {
		this.model = model;
        this.view = view;
    }
	
	public void setInfectionRate(){
			// takes no arguments, rate incremented by 1 
		model.setInfectRate();
	}
	
	public int getRateMarker(){
		return model.getRateMarker();
	}
	
	public int getInfectionRate(){
			// 2, 3, or 4 returned
			// marker position not returned
		return model.getInfectRate();
	}
	
    public void updateView() {
        view.printInfectionRateDetails(model.getInfectRate());
    }
}
