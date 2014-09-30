package gameBoard_Master;

public class Outbreaks {

	// count goes from 0 - 8 (8 being worldwide panic)
	
	private int [] outbreaksLevel = {0,1,2,3,4,5,6,7,8};
	private int outbreaksIntensity;
	
	public Outbreaks() {
		this.outbreaksIntensity = 0;
	}
	
	public int getOutbreakLevel(){
		return this.outbreaksLevel[this.outbreaksIntensity];
	}
	
	public boolean increaseOutbreaks(){
		if (this.outbreaksIntensity < 7){ // off by one
			this.outbreaksIntensity++;
			return false;
		}
			// if returned true, should initiate game over.
		else
			if (outbreaksIntensity == 7) {
				this.outbreaksIntensity++; // to advance the marker to skull and crossbones and no further to prevent array out of bounds exception
			}
			return true;
	}
	
}
