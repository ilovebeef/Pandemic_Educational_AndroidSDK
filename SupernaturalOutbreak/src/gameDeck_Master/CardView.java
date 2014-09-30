// Written by Matt Roycroft
package gameDeck_Master;

public class CardView {
	// simple function to identify cards by the view, to whichever entity is interested in knowing
	public void printCardDetails(String cardName, String cardType) {
		System.out.println("Current card's name: " + cardName);
		System.out.println("Current card's type: " + cardType);
	}

}
