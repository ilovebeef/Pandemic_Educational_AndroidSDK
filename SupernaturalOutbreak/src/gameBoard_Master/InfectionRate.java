/*
 * Programmer: David Bloss
 */

package gameBoard_Master;

public class InfectionRate {

	private final int[] rate = {2,2,2,3,3,4,4};			// Order: Initial ---> Final
	private int rateMarker;								// Keeps track of rate severity 
	
	public InfectionRate(){
		//Bitmap bMap
			// Should never go beyond Final. Final = 6
		this.rateMarker = 0;						
	}
	
	public InfectionRate(int r){
		this.rateMarker = r;
	}
	 
		// Increments by 1
	public void setInfectRate(){	
			// Final Position = 6
		if (this.rateMarker < 6)
			this.rateMarker += 1;
	}
	
	public int getRateMarker(){
		return this.rateMarker;
	}
	
	public int getInfectRate(){
			// rateLocatoin Range: 0-6
		return this.rate[rateMarker];
	}
}
