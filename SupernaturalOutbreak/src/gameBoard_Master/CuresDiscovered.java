/*
 * Programmer: David Bloss
 */

package gameBoard_Master;
 
public class CuresDiscovered {

	private boolean blackCure,
					blueCure,
					redCure,
					yellowCure;

		//**********************
		// 		Constructor
		//**********************
	
	public CuresDiscovered(){
			// initially all cures not discovered
		this.blackCure = false;
		this.blueCure = false; 
		this.redCure = false;
		this.yellowCure = false;
	}
	
		//**********************
		// 		Mutators
		//**********************
	
	public void setBlackCure(boolean found){
		this.blackCure = found;
	}
	
	public void setBlueCure(boolean found){
		this.blueCure = found;
	}
	
	public void setRedCure(boolean found){
		this.redCure = found;
	}
	
	public void setYellowCure(boolean found){
		this.yellowCure = found;
	}

		//**********************
		// 		Observers
		//**********************
	
	public boolean getBlackCure(){
		return this.blackCure;
	}
	
	public boolean getBlueCure(){
		return this.blueCure;
	}
	
	public boolean getRedCure(){
		return this.redCure;
	}
	
	public boolean getYellowCure(){
		return this.yellowCure;
	}
	
	public boolean checkColorCure(String color) {
		if (color.compareTo("black") == 0) {
			return this.blackCure;
		}
		else if (color.compareTo("blue") == 0) {
			return this.blueCure;
		}
		else if (color.compareTo("red") == 0) {
			return this.redCure;
		}
		else if (color.compareTo("yellow") == 0) {
			return this.yellowCure;
		}
		else {
			return false; // if the color was punched in wrong, just assume the cure isn't discovered for debugging purposes
		}
	}
	public boolean checkForVictory() {
		if (this.blackCure == true && this.blueCure == true && this.redCure == true && this.yellowCure == true) {
			return true;
		}
		else {
			return false;
		}
	}
}

