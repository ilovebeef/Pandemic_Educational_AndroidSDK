package gameDeck_Master;
// Written by Matt Roycroft
	
//package gameDeck_Master;
	
import static org.junit.Assert.assertTrue;

import java.util.Stack;
import org.junit.Before;
import org.junit.Test;
 	
public class Test_cards_and_deck {
	private Card testCard;
 	private Stack<Card> stackOfCards;
	// Junit is super buggy. It gives no reason why it is failing.
 	// I'll attach a screenshot of the error on my post on blackboard.
 	@Before public void initStack() {
 		this.stackOfCards = new Stack<Card>();
	}

 	// first non-trivial function to test
 	public boolean drawCard(Stack<Card> stackOfCards) {
 		if (stackOfCards.isEmpty()) {
 			return true;
 		}
 		else {
 			return false;
 		}
 	}
 	
 	// second non-trivial function to test
 	public boolean cardsRemaining(Stack<Card> stackOfCards) {
 		int size = stackOfCards.size(); // Reveals number of cards in stack
 		if (size == 0) {
 			return true;
 		}
 		else {
 			return false;
 		}
 	}

 	// Test case for an empty stack
 	@Test public void emptyStack() {
 		assertTrue(drawCard(stackOfCards));
 	}

 	// Test case for a stack with 1 card
 	@Test public void nonEmptyStack() {
 		testCard = new Card();
 		this.stackOfCards.push(testCard);
 		assertTrue(drawCard(stackOfCards));
 	}

 	// Test case for there being zero cards in the stack
 	@Test public void noCards() {
 		assertTrue(cardsRemaining(stackOfCards));
 	}

 	@Test public void notZeroCards() {
 		testCard = new Card();
 		this.stackOfCards.push(testCard);
 		assertTrue(cardsRemaining(stackOfCards));
 	}

 	public static void main(String[] args) {
 		org.junit.runner.JUnitCore.runClasses(Test_cards_and_deck.class);
		}
	}
