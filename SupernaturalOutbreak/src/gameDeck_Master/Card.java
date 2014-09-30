// Written by Matt Roycroft
package gameDeck_Master;

import gameEngine_frameworkView.GamePixmap_interfaceView;

public class Card {
	private String fileName; // The name of the file that corresponds to an image in the assets folder 
	private String type; // defines whether it is a city card, event card, infection card, etc
	private String formalName; // this is formal name of the city, for comparison with maps class
	private String color; // If applicable, the color of the card is kept here
	private GamePixmap_interfaceView image; // each card holds a unique image 
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormalName() {
		return formalName;
	}

	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	public GamePixmap_interfaceView getImage() {
		return image;
	}

	public void setImage(GamePixmap_interfaceView image) {
		this.image = image;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}