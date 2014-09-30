// Written by Matt Roycroft
package gameDeck_Master;
import java.util.Stack;

public class DeckController {
	private Deck model;
	private DeckView view;
	
	public DeckController(Deck model, DeckView view) {
		this.model = model;
		this.view = view;
	}
	
	public void setDeckName(String name) {
		model.setName(name);
	}
	
	public String getDeckName() {
		return model.getName();
	}
	
	public void setStackOfCards(Card [] arrayOfCards) {
		model.setStackOfCards(arrayOfCards);
	}
	
	public Stack<Card> getStackOfCards() {
		return model.getStackOfCards();
	}
	
	public void viewDeck() {
		view.printDeckDetails(model.getName());
	}
	
	public void viewCardsInDeck() {
		view.showCardsInDeck(model.getStackOfCards());
	}
	
	// draws cards from a given deck
	public Card drawCard() {
		return model.popCard();
	}
	
	public Boolean isEmpty() {
		return model.isEmpty();
	}
	
	public void pushCard(Card card) {
		model.pushCard(card);
	}
	
	public Card drawBottomCard() {
		Card temp = new Card();
		int size = model.getStackOfCards().size();
		temp = model.getStackOfCards().elementAt((size - 1));  // passes a copy of the card to temp, located at the bottom of the stack
		model.getStackOfCards().remove((size - 1)); // removes the bottom card from the stack, and automatically adjusts stack to account for change
		return temp;
	}
	
	public void shuffleCards() {
		model.shuffleCards();
	}
	
	public void putShuffledCardsOnTop(Stack<Card> stackOfInfectionDiscardDeck) {
		model.putDiscardedOnTopOfDrawDeck(stackOfInfectionDiscardDeck);
	}
	
	public void addEpidemicCards(int difficulty) {
		model.setupPlayerDeck(difficulty);
	}

}
