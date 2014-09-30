// Written by Matt Roycroft
package gameDeck_Master;

import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameGraphic_interfaceView.PixmapFormat;
import gamePlayer_Controller.Player;
import java.util.*;

public class Deck {
	private Stack<Card> stackOfCards; // ADT to hold cards in the deck
	private String name; // Either Infection deck, Player deck, Infect discard, Player discard, or Removed from game
	private boolean deckInitialized;
	private GameGraphic_interfaceView gameGraphic;
	
	public Deck (String nameOfDeck, Game_interfaceView game) {
		this.initializeStack();
		this.name = nameOfDeck;
		this.gameGraphic = game.getGraphics();
		if (nameOfDeck.compareTo("Infection Deck") == 0) { // If nameOfDeck is Infection Deck, create it as follows
			createCityBasedCards("Infection Card"); // create infection cards
		}
		if (nameOfDeck.compareTo("Player Deck") == 0) {
			// creating the city cards and the event cards, loading them into the deck.
			// Epidemic cards are added after the players draw their initial hands.
			createCityBasedCards("Player Card");
		}
	}	
	
	public Stack<Card> getStackOfCards() {
		return this.stackOfCards;
	}
	
	public void setStackOfCards(Card [] arrayOfCards) {  // accepts array of cards, will load into stack
		if (deckInitialized != true) {
			initializeStack();
		}
		for (int i = 0; i < arrayOfCards.length; i++) {
			this.stackOfCards.push(arrayOfCards[i]);
		}
	}
	
	public void putDiscardedOnTopOfDrawDeck(Stack<Card> discardStack) {  // accepts array of cards, will load into stack
		if (deckInitialized != true) {
			initializeStack();
		}
		int size = discardStack.size();  // gets number of cards in discard pile
		Card [] arrayOfCards = new Card[size];  // creates a temporary array to hold discarded cards
		for (int i = (size - 1); i >= 0; i--) {
			arrayOfCards[i] = discardStack.pop();  // remove bottom card, one at a time.  Put it into the array.
		}
		setStackOfCards(arrayOfCards);  // put array of cards onto deck
	}
	
	private void initializeStack() {
		this.stackOfCards = new Stack<Card>();
		this.deckInitialized = true;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void pushCard(Card aCard) {
		this.stackOfCards.push(aCard);
	}
	
	public Card popCard() {
		return this.stackOfCards.pop();
	}
	
	public Boolean isEmpty() {
		return this.stackOfCards.isEmpty();
	}
	
	public int cardsRemaining() { // THIS IS A STUB
		int size = this.stackOfCards.size();  // Reveals number of cards in stack
		// This is a stub because this information needs to be reliably conveyed to the map class
		// It should be updated each time a card is successfully drawn, to tell the player 
		// how many cards they have left to draw.  The code implementation of that is still
		// unknown/incomplete, therefore a stub.
		return size;
	}
	
	public void peekAtPlayerCards (Player player) {  // THIS IS A STUB
		// It is still unknown if players will hold a stack of cards, or some other format
		// If they will use a stack, let this enable such an activity
		
		// player.theirCards = displaythemSomehow
	}
	
	// order of cities: yellow, black, blue, red (in groups of 12, each group separated by a line
	// can be used by both city cards and infection cards.  The type parameter makes the difference.
	private void createCityBasedCards(String type) {
		int deckType;
		int arraySize;
		
		if (type.compareTo("Player Card") == 0) {
			deckType = 1; // player deck
			arraySize = 53;
		}
		else {
			deckType = 2; // infection deck
			arraySize = 48;
		}			
		Card [] cardArray = new Card[arraySize];
		Card [] eventArray = new Card[5];
		int ev = 0; // counter for eventArray index
		int c = 0; // counter for cities array index
		eventArray = createEventCards();
		String assetLocation;
		String color = "yellow";
		
		for (int i = 0; i < arraySize; i++, c++) {
			// below if statement prevents event cards from being added to an infection deck.
			if (deckType == 1) {
				// added one to i, since 0 mod 10 is 0, and I wanted to only add 1 event card per roughly 10 city cards
				assetLocation = "Player_Cards/"; // loads images from Player_Card directory in assets (sets it to do so anyway).
				if ((i + 1) % 10 == 0) { 
					cardArray[i] = eventArray[ev];
					i++;  // prevents the event cards from being overwritten by a city card
					ev++; // increments counter for eventArray
				}
				
			}
			else { // if its deck type 2, ie infection deck, load images from the file folder Infection_Cards instead
				assetLocation = "Infection_Cards/";
			}

			Card init = new Card();
			init.setFileName(CityNames.cities[c]);
			init.setFormalName(CityNames.formalCityNames[c]);
			init.setType(type);
			if (type.compareTo("Event Card") != 0) {  // sets color for city and infection cards, but not event cards.
				// every 12th card, excluding event cards, change colors. The ev counter excludes event cards in this case.
				if ((i - ev) == 12) {
					color = "black";
				}
				if (i - ev == 24) {
					color = "blue";
				}
				if (i - ev == 36) {
					color = "red";
				}
				init.setColor(color); // color of card is set here
			}
			init.setImage(this.gameGraphic.newPixmap((assetLocation + CityNames.cities[c] + ".png"), PixmapFormat.ARGB4444));
			cardArray[i] = init;
		}
		//Random mixCards = new Random();
		//int randomNumber = mixCards.nextInt(10) + 10; // (random # between 10 and 20)
		//for (int i = 0; i < randomNumber; i++) {
			// mixing cards up a bunch to keep each deck very fresh and random
		this.shuffleCards(arraySize, cardArray);  // trying out using the collections shuffle routine
		//}
		this.setStackOfCards(cardArray);
	}
	
	private Card [] createEventCards() {
		String [] feventCards = {"Airlift", "OneQuietNight", "GovernmentGrant",
								"ResilientPopulation", "Forecast"};
		String [] neventCards = {"Airlift", "One Quiet Night", "Government Grant",
				"Resilient Population", "Forecast"};
		
		Card [] eventArray = new Card[5];
		for (int i = 0; i < 5; i++) {
			Card init = new Card();
			init.setFileName(feventCards[i]);
			init.setFormalName(neventCards[i]);
			init.setType("Event Card");
			init.setImage(this.gameGraphic.newPixmap(("Player_Cards/" + feventCards[i] + ".png"), PixmapFormat.ARGB4444));
			init.setColor("N/A");
			eventArray[i] = init;
		}
		shuffleCards(5, eventArray);
		return eventArray;
	}
	
	// create, split, and shuffle cards for initializing the game
	// @param num: an integer between 4 and 6
	public void setupPlayerDeck(int num) {		
		int splitSize = this.stackOfCards.size();
		int stackSize = (splitSize / num) + 1; // add 1 to give room for epidemic card to be added on top
		int [] sizeOfEachStack = new int[num]; // array to store individual size of each split into an array
		for (int i = 0; i < num; i++) {
			sizeOfEachStack[i] = stackSize;
		}
		int remCards = splitSize % num;
		for (int i = 0; i < remCards; i++) { // splits up remaining cards, if any, between split piles
			sizeOfEachStack[i]++;
		}
		
		Card [] temp1 = new Card[20]; // in case the difficultly is set to heroic
		Card [] temp2 = new Card[20]; // in case the difficulty is set to normal
		
		switch (num) {  // using fall-through code to split up the deck
		case 6: Card[] split6 = new Card[sizeOfEachStack[5]];
				injectEpidemicCardsAndShuffle(sizeOfEachStack[5], split6);
				temp1 = split6;
		case 5: Card[] split5 = new Card[sizeOfEachStack[4]];
				injectEpidemicCardsAndShuffle(sizeOfEachStack[4], split5);
				temp2 = split5;
		case 4: Card[] split4 = new Card[sizeOfEachStack[3]];
				Card[] split3 = new Card[sizeOfEachStack[2]];
				Card[] split2 = new Card[sizeOfEachStack[1]];
				Card[] split1 = new Card[sizeOfEachStack[0]];
				injectEpidemicCardsAndShuffle(sizeOfEachStack[3], split4);
				injectEpidemicCardsAndShuffle(sizeOfEachStack[2], split3);
				injectEpidemicCardsAndShuffle(sizeOfEachStack[1], split2);
				injectEpidemicCardsAndShuffle(sizeOfEachStack[0], split1);
				if (num == 6) {  // prevents trying to add empty arrays to the deck
					this.setStackOfCards(temp1); // puts shuffled cards back into deck
				}
				if (num == 5 || num == 6) { // prevents trying to add empty arrays to the deck
					this.setStackOfCards(temp2); // puts shuffled cards back into deck
				}
				this.setStackOfCards(split4); // puts shuffled cards back into deck
				this.setStackOfCards(split3); // puts shuffled cards back into deck
				this.setStackOfCards(split2); // puts shuffled cards back into deck
				this.setStackOfCards(split1); // puts shuffled cards back into deck
				break;
		default: System.out.println("Deck must be split using an integer between 4 and 6");
				break;
				
		}
	}
		
	// epidemic card is added at this stage, then shuffled
	// Not intended to be called from main, this is for game initialization only
	private void injectEpidemicCardsAndShuffle(int arySize, Card [] splitDeck) {
		// creating a new epidemic card
		Card epidemic = new Card();
		epidemic.setFileName("Epidemic");
		epidemic.setType("Epidemic Card");
		epidemic.setImage(this.gameGraphic.newPixmap(("Player_Cards/" + epidemic.getFileName() + ".png"), PixmapFormat.ARGB4444));
		epidemic.setColor("N/A");
		splitDeck[0] = epidemic;
		for (int i = 1; i < arySize; i++) {
			splitDeck[i] = this.stackOfCards.pop();
		}
		shuffleCards(arySize, splitDeck);
	}
		
	// shuffle a Deck of cards, rather than an array of cards
	// This is in case some deck of cards must be shuffled as a whole deck
	public void shuffleCards() {
		int size = this.stackOfCards.size();
		Card [] arrayOfCards = new Card[size];
		for (int i = 0; i < size; i++) {
			arrayOfCards[i] = this.stackOfCards.pop();
		}
		shuffleCards(size, arrayOfCards);
		this.setStackOfCards(arrayOfCards);
	}
	
	// Shuffles and array of cards
	// each card is shuffled randomly, swapping array indexes through a single for loop.
	public void shuffleCards(int sizeOfDeck, Card [] cards) {
		final int max = cards.length;
		//Card temp = new Card();
		Random randomShuffle = new Random(); // run shuffle routine a random # of times between 3 and 5
		int randomQuantity = randomShuffle.nextInt(3) + 3;
		// Max number of shuffles is 5.  I add 3 at the end to raise the minimum roll to 3.
		// The 3 indicates a random number in the range between 0 and 2.  the +3 indicates it adds that much to the value, 
		// therefore your range is between 3 and 5.
		List<Card> randomizeCards = new ArrayList<Card>();
		for (int i = 0; i < max; i++) {
			randomizeCards.add(cards[i]);  // stuff cards into an array list
		}
		for (int j = 0; j < randomQuantity; j++) {
			Collections.shuffle(randomizeCards);  // use the list randomize function
		}
		for (int i = 0; i < max; i++) {
			cards[i] = randomizeCards.remove(0);  // toss the now shuffled cards back into the array
		}
		/*  Beta testing the list's version of shuffling.
		int shuffleCount = randomShuffle.nextInt(4) + 3;
		int randomSwap;
		Random r = new Random();
		for (int j = 0; j < shuffleCount; j++) {
			for (int i = 0; i < sizeOfDeck; i++) {
				do {
					randomSwap = r.nextInt(max);
					if (max <= 1) { // in case you try to shuffle 1 or 0 cards, it wont get stuck in an infinite loop
						break;
					}
				} while (randomSwap == i);
				temp = cards[i];
				cards[i] = cards[randomSwap];
				cards[randomSwap] = temp;
			}
		}
		*/
	}
}
