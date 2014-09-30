// Written by Matt Roycroft
package gameDeck_Master;
import java.util.Stack;

public class DeckView {
	public void printDeckDetails(String deckName) {
		System.out.println("Current Deck's name: " + deckName);		
	}
	
	public void showCardsInDeck(final Stack<Card> stackOfCards) {
		Stack<Card> temp = new Stack<Card>();
		temp = stackOfCards;
		Card tempCard = new Card();
		int size = temp.size();
		System.out.println("Size of deck reported by deckView: " + size);
		for (int i = 0; i < size; i++) {
			tempCard = temp.elementAt(i);
			System.out.println("Card #" + (i + 1) + ":");
			System.out.println("Card Name: " + tempCard.getFileName());
			System.out.println("Card Type: " + tempCard.getType());
			System.out.println("Card color: " + tempCard.getColor());
		}
	}
}
