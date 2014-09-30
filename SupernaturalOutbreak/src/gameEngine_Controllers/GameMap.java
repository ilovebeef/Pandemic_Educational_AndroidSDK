package gameEngine_Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import gameBoard_Master.City;
import gameBoard_Master.CuresDiscovered;
import gameBoard_Master.CuresDiscoveredController;
import gameBoard_Master.CuresDiscoveredView;
import gameBoard_Master.InfectionRate;
import gameBoard_Master.InfectionRateController;
import gameBoard_Master.InfectionRateView;
import gameBoard_Master.Outbreaks;
import gameBoard_Master.OutbreaksController;
import gameBoard_Master.OutbreaksView;
import gameBoard_Master.World;
import gameBoard_Master.WorldController;
import gameBoard_Master.WorldView;
import gameCube_Master.CubeDeckController;
import gameCube_Master.CubeDeckModel;
import gameCube_Master.CubeDeckView;
import gameCube_Master.CubeModel;
import gameDeck_Master.Card;
import gameDeck_Master.Deck;
import gameDeck_Master.DeckController;
import gameDeck_Master.DeckView;
import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameInput_interfaceView.TouchEvent;
import gamePlayer_Controller.Player;
import gameUser_Settings.GameSetting_Controller;
import gamePlayer_Controller.Roles;


public class GameMap extends GameScreen_View {
	
	// variables for difficulty and number of players, decided at settings screen via radio toggle
	GameSetting_Controller gameSetting;
	int difficulty;
	int numberOfPlayers;
	Paint color1 = new Paint();
	GameGraphic_interfaceView g = game.getGraphics();
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    int touchLen = 0;
    TouchEvent event1;
	
	/*****************************************
	 * Control for tracking the map and scroll
	 * bars
	 *****************************************/
	boolean trackToggle = false;
	boolean trackToggle_Toggle = true;
	int offsetX = 107;  // to account for relative position of the scroll bar
	int offsetY = 26;	// to account for the relative position of the scroll bar
	int trackX = -150;
	int trackY = -200;
	int trackBall_Xaxis = 65;
	final int trackBall_XaxisLow = 50;
	final int trackBall_XaxisHigh = 340;
	int trackBall_Yaxis = 60;
	final int trackBall_YaxisLow = 10;
	final int trackBall_YaxisHigh = 175;
	int scrollX_limitLow = 0;
	int scrollX_limitHigh = -833;
	int scrollY_limitLow = 0;
	int scrollY_limitHigh = -562;
	/*****************************************
	 * Card resizing control
	 *****************************************/
	final int mapCard_sizeX = 95;
	final int mapCard_sizeY = 140;
	final int mapCard_incrementY = 30;
	int cardSizeX = 0;
	int cardSizeY = 0;
	/*****************************************
	 * Player Auto move to location control
	 *****************************************/
	int scrollMoveX = 0;
	int scrollMoveY = 0;
	int tempPlayer_twoX = -100;
	int tempPlayer_twoY = -100;
	final int moveBy_midPointX = 240;
	final int moveBy_midPointY = 160;
	int newPlayerX = 0;
	int newPlayerY = 0;
	boolean tempPlayer_moveToggle = false;
	boolean tempPlayer_moveBack = false;
	/*****************************************
	 * Map assets control
	 *****************************************/
	int drawMap_assetToggle = 1;
	/*****************************************
	 * Cube example
	 *****************************************/
	int distancex = 0;
	int distancey = 0;
	
	// Creating the player
	Player[] playerArray;
	
	// Creating the decks via controller
	DeckController playerDeck = new DeckController((new Deck("Player Deck", game)), (new DeckView()));
	DeckController playerDiscard = new DeckController((new Deck("Player Discard", game)), (new DeckView()));
	DeckController infectionDeck = new DeckController((new Deck("Infection Deck", game)), (new DeckView()));
	DeckController infectionDiscard = new DeckController((new Deck("Infection Discard", game)), (new DeckView()));
	
	// Creating the purgatory deck.  Any card removed from the game goes here without exception.
	DeckController purgatory = new DeckController((new Deck("Purgatory", game)), (new DeckView()));

	// Add the cubes via controllers
	CubeDeckController redCubes = new CubeDeckController((new CubeDeckModel("red", 24, game)), (new CubeDeckView()));
	CubeDeckController blueCubes = new CubeDeckController((new CubeDeckModel("blue", 24, game)), (new CubeDeckView()));
	CubeDeckController yellowCubes = new CubeDeckController((new CubeDeckModel("yellow", 24, game)), (new CubeDeckView()));
	CubeDeckController blackCubes = new CubeDeckController((new CubeDeckModel("black", 24, game)), (new CubeDeckView()));
	CubeDeckController [] cubeControllerArray = {blackCubes, blueCubes, redCubes, yellowCubes};
	
	// Creating the infectionrate object
	InfectionRateController infectRate = new InfectionRateController((new InfectionRate()), (new InfectionRateView()));
	
	// Creating the outbreakcontroller object
	OutbreaksController outbreaks = new OutbreaksController((new Outbreaks()), (new OutbreaksView()));
	
	// Creating the cities objects
	WorldController worldCities = new WorldController((new World()), (new WorldView()));
	
	// creating the cures discovered object
	CuresDiscoveredController curesDiscovered = new CuresDiscoveredController(new CuresDiscovered(), new CuresDiscoveredView());
	
	public GameMap(Game_interfaceView _game, int diff, int numPlayers) {
		super(_game);
		this.difficulty = diff;
		this.numberOfPlayers = numPlayers;
		GameAssets.gameMap_BGM = game.getAudio().newMusic("Audio/gameMap_BGM.mp3");
		GameAssets.gameMap_BGM.play();
		GameAssets.gameMap_BGM.setLooping(true);
		setupGame();
	    GameAssets.settings_Click.dispose();
	    System.gc();
	}
	
	// Creating some variables for the player turn and for testing/debugging
	boolean gameOver = false;  // switch to true if lose condition satisfied
	static int airliftFlyer;
	static int cardHolder;
	int forecastChoice;
	int [] forecastIndex = new int[6];	// declaration for the array to store which index of cards are being picked
	Card [] forecastReturn = new Card[6];	// holds correctly the order in which the above cards were picked
	int [] forecastX = new int[6];  // coordinates for the x-axis for touch listener for choosing the order of cards
	int playerTurnTracker = 0;
	int numberOfPlayerTurns = 1;
	int chosenShareKnowledge;
	int chosenCardPos;
	int chosenResearchOverwrite;
	int playersInSameCity;
	int eventCardsAvailable;
	int researcherIndex;
	int eventCardPlayed = 0;
	int medicIndex;
	int quarantineIndex;
	int opsExpertSacrifice;
	int tabSwitcher = 1;
	int cardInfoTabSwitcher = 0;
	int chosenPawnIndex;	// for dispatcher joining pawns, it is the index of the pawn chosen to get moved to another pawn's city
	int cardPageView = 0;
	int [] giveCardPos = new int[3];
	int [] eventCardOwners = new int[5];  // up to a max of 5 event cards exist
	int [] eventCardLoc = new int[5];	  // position in each player's hand of where event cards are
	int [] researcherCardLoc = new int[7];
	int [] researcherTakeLoc = new int[7];
	int [] opsCardLoc = new int[7];  // location of card in ops. expert hand
	int [] opsCardCoord = new int[7];  // location for where touch listener should respond to touches for picking a card to sacrifice
	int [] plannerCardLoc = new int[5]; // location of card in discard pile
	int [] plannerCardCoord = new int[5];	// location of where touch listener should respond to touches to select which event card to take
	int [] chosenPawnCoord = new int[4];	// coords of where the pawns are for the touch listener
	int [] chosenPawnLoc = new int[4];		// location of where the chosen pawn is in the player array
	int [] joinX = new int[4];
	int [] joinY = new int[4];
	City [] joinCity = new City[4];
	int [] controlX = new int[48];
	int [] controlY = new int[48];
	City [] controlCity = new City[48];
	int [] resilientX = new int[4];	// where to listen for the touch listener on x-axis for choosing a city to remove from the game
	int [] resilientIndex = new int[4];	// index of where in the discard pile the potential card will be removed and sent to purgatory
	int [] discoverIndex = new int[7];  // stores indexes of which cards to keep if discovering a cure and had more than the number of cards required.
	int [] discoverX = new int[7];		// stores x-axis of player cards to check for when discovering a cure and more than number of cards required in hand
	Card [] discoverSave = new Card[3];  // Up to 7 cards could be the same color, and with a scientist,  up to 3 could be saved.  Save them here, add them back in later
	String discoverColor = "";
	int discoverQuantity = 0;
	String [] dispatcherJoinDest = new String[4];  //max of 3 places a pawn could go to, but will hold all 4 places for reference purposes
	CubeModel removeCubeSuccess = null;
	CubeModel medicCube = null;
	boolean onlyThis = false;
	String eventPlayed = "";
	String gameOverReason = "";
	int [] overLimitX = new int[15];
	int [] overLimitIndex = new int[15];
	boolean medicPresent = false;
	boolean quarantinePresent = false;
	boolean phaseOneComplete = false;
	boolean actions = false;
	boolean driving = false;
	boolean charter = false;
	boolean directFlight = false;
	boolean shuttleFlight = false;
	boolean airlift = false;
	boolean forecast = false;
	boolean useForecast = false;
	boolean governmentGrant = false;
	boolean oneQuietNight = false;
	boolean useOneQuietNight = false;
	boolean resilientPopulation = false;
	boolean actionInProgress = false;
	boolean treatDisease = false;
	boolean shareKnowledge = false;
	boolean sharingDecisionMade = false;
	boolean giving = false;
	boolean taking = false;
	boolean revealCards = false;
	boolean useEventCard = false;
	boolean shareCardSuccess = false;
	boolean overlapDelay = false;
	boolean researchGive = false;
	boolean nonResearcher = false;
	boolean researcher = false;
	boolean chosenLately = false;
	boolean opsExpertMove = false;  // flag for when the ops expert wants to use their transport ability
	boolean opsExpertMoveUsed = false;  // ops Expert can only use special transport move once per turn
	boolean chosenSacrifice = false;  // indicates if the card to be sacrificed has been chosen yet
	boolean contPlannerStealEventCard = false;
	boolean dispatcherMove = false;
	boolean dispatcherJoin = false;
	boolean dispatcherControl = false;
	boolean pawnChosen = false;
	boolean dispatchDriving = false;
	boolean dispatchDirect = false;
	boolean dispatchCharter = false;
	boolean dispatchShuttle = false;
	boolean dispatchActive = false;
	boolean victory = false;  // always assume game isn't won in the beginning
	boolean discoverCure = false;  // a boolean to indicate whether you are actively trying to discover a cure and you have more than the required number of cards
	boolean extraCards = false;
	boolean phaseTwo = false;
	boolean phaseThree = false;
	boolean cardsDrawn = false;
	boolean cardsResolved = false;
	boolean card1Resolved = false;
	boolean card2Resolved = false;
	boolean card1Drawn = false;
	boolean card2Drawn = false;
	boolean dump = false;
	boolean preventCancel = false;
	boolean lifted = true;
	Card phase2Draw1;
	Card phase2Draw2;
	Card phase3Draw;
	// Variables for determining dynamic drive/ferry locations.  Set to a super low number so it doesnt trigger a false positive touch
	Stack<Integer> destX = new Stack<Integer>();
	Stack<Integer> destY = new Stack<Integer>();
	int[] shareX = new int[7];
	int[] shareY = new int[7];
	int[] shareZ = new int[7];
	Stack<City> destCity = new Stack<City>();
	City[] researchCities = new City[6];
	City[] allCities;
	int[] destX1 = new int[48];
	int[] destY1 = new int[48];
	City[] destCity1 = new City[48];
	
	public void setupGame() {
		// Infect 9 cities, create players, distribute cards
		initializeGame();
	}
	
	public void setupPlayers() {
		playerArray = new Player[numberOfPlayers];
		List<Integer> randomRole = new ArrayList<Integer>();
		int role;
		for (int i = 0; i < 7; i++) {
			randomRole.add(i); // we have 7 roles, so we add 7 numbers to the list.
		}
		Collections.shuffle(randomRole); // shuffle the order of the list of numbers, so that it is random every time
		for (int i = 0; i < numberOfPlayers; i++) {
			role = randomRole.remove(i);  // since order is shuffled, get first one and remove it from the list
			// player created, random role chosen, non-repeating, starts off in Atlanta.
			Player player = new Player(("Player " + String.valueOf(i + 1)), 0, 0, role, worldCities.findCity("Atlanta"), game); 
			playerArray[i] = player;
			// checks to see if a Medic is in the game, and if so, where in the array the Medic is
			// This is to reduce overhead on games that do not have a Medic and to enforce the constant
			// cured disease cube removal employed by the Medic role.
			if (player.getRole() == Roles.MEDIC) {
				medicPresent = true;
				medicIndex = i;
			}
			if (player.getRole() == Roles.QUARANTINE_SPECIALIST) {
				quarantinePresent = true;
				quarantineIndex = i;
			}
		}
		// giving Atlanta the first research station per game rules
		worldCities.buildResearchStation(worldCities.findCity("Atlanta"));
	}
	
	public void initializeGame() {
		// STEP 1: Infect cities.  This is to prevent Quarantine specialist from activating initially
		Card temp2;
		for (int i = 0; i < 9; i++) {  // infect 9 cities total
			temp2 = infectionDeck.drawCard();
			if (i < 3) {
				infectCity(3, temp2); // infect first three cities with 3 cubes
			}
			if (i >=3 && i <= 5) {
				infectCity(2, temp2); // infect next three cities with 2 cubes
			}
			if (i > 5) {
				infectCity(1, temp2); // infect another three cities with 1 cube each
			}
		}
		
		// STEP 2: Create the players themselves.
		setupPlayers();
				
		// STEP 3: Distribute cards to players
		int cardsToDistribute;
		if (numberOfPlayers == 2) {
			cardsToDistribute = 4; // if 2 players, give each 4 cards
		}
		else if (numberOfPlayers == 3) {
			cardsToDistribute = 3; // if 3 players, give each 3 cards
		}
		else {
			cardsToDistribute = 2; // if 4 players, give each 2 cards
		}
		Card temp; // a temporary card to load cards into player hand
		for (int i = 0; i < cardsToDistribute; i++) { // run it 4 times 2 players, 3 times for 3 players, 2 times for 4 players
			for (int j = 0; j < numberOfPlayers; j++) { // give a card to each respective player
				temp = playerDeck.drawCard();
				playerArray[j].addCard(temp); // add to player's hand				
			}
		}
		
		// Rules say after handing out initial cards, to then add epidemics and split/shuffle deck
		playerDeck.addEpidemicCards(difficulty);		
		// Initialize the allCities array for usages in present
		allCities = worldCities.getWorldCities();
	}
	
	public void resolveCard(Player player, Card drawnCard) {
		Card temp;
		if (drawnCard.getType().compareTo("Epidemic Card") == 0 ) {
			temp = infectionDeck.drawBottomCard();  // draw the bottom infection card, it's in the rules
			infectCity(3, temp);	// infect city with 3 cubes		
			infectRate.setInfectionRate();
			infectionDiscard.shuffleCards();  // shuffle the discarded infection cards
			infectionDeck.putShuffledCardsOnTop(infectionDiscard.getStackOfCards());  // put the shuffled discard stack on the draw card stack
			playerDiscard.pushCard(drawnCard);  // put epidemic card into discard pile for player deck
			// epidemic card resolved
			// If there is 2 epidemics in a row, only the single infection card in the discard pile is placed back on top of the deck
		}
	}
	
	public void infectCity(int cubesToPlace, Card drawnCard) { // num is number of cubes to infect city with.  Card is the one you want to infect
		int j = 0;  // counter to pick the right cube stack to dispense cubes
		String cityColor = drawnCard.getColor(); // compare city color card to cube to find the right controller to use
		while (cubeControllerArray[j].getCubeColor().compareTo(cityColor) != 0) {
			j++;
		}
		String cityToInfect = drawnCard.getFormalName();
		boolean outbreak = false;  // assume city doesn't have an outbreak initially
		CubeModel cubeInfect;
		
		// this checks for eradication.  If the color of the infection card drawn matches a color that a cure has been discovered,
		// then half the conditions for eradication are met.  The other is that all the cubes of that color must be off the game board.
		// there are 24 cubes per color total, so if 24 are in supply of that color, it means that the disease is eradicated and
		// no more diseases of that color will be placed.  Will still discard the infection card, but wont place any disease cubes
		// The other checks do not matter, an outbreak would not occur if the cube of an eradicated disease is never placed to begin with
		if (curesDiscovered.checkColorCure(cityColor) == true && cubeControllerArray[j].getCubeDeck().size() == 24) {
			infectionDiscard.pushCard(drawnCard);  // discard the card and leave the function.
			return;
		}
		
		// this never runs on initial setup because players are created afterwards.  This is as intended.
		if (quarantinePresent == true) {  // build a city immunity list if a quarantine specialist is in the game
			// create an array of immune cities, the size of it's connections + the city itself
			boolean immunityFound = false;
			int length = ((playerArray[quarantineIndex].getCurrentCity().getConnections().length) + 1);
			String[] immuneCities = new String[length];
			String[] connections = playerArray[quarantineIndex].getCurrentCity().getConnections();
			// manually copy over city + connections, add current city to last index of array
			immuneCities[length - 1] = playerArray[quarantineIndex].getCurrentCity().getName();
			for (int m = 0; m < (length - 1); m++) {
				immuneCities[m] = connections[m];
			}
			for (int k = 0; k < length; k++) {
				// if the quarantine specialist is in or adjacent to a city about to be infected, prevent it from occurring per role rules.
				// outbreaks cannot occur if disease cubes can't be placed by prevention, this fulfills the quarantine specialist role.
				if (immuneCities[k].compareTo(cityToInfect) == 0) {
					immunityFound = true;
				}
			}
			if (immunityFound == true) {  // if the city that was to be infected is on the immunity list, discard the card, don't infect, and leave function immediately 
				infectionDiscard.pushCard(drawnCard);
				return;
			}
		}
		
		if (medicPresent == false) { // if there's no medic, don't even bother to check for an index for Medic that doesn't exist
			for (int i = 0; i < cubesToPlace; i++) {
				if (cubeControllerArray[j].isEmpty() == false) { // give cubes for as long as there is any to give
					cubeInfect = cubeControllerArray[j].popCube(); // only give a cube if there's still any left
					outbreak = worldCities.findCity(cityToInfect).addCube(cubeInfect);  // infect this city with a cube
					if (outbreak == true) {
						
						// rules regarding outbreaks: once per card, a city may outbreak a maximum of one time.
						Stack<String> outbreakOnce = new Stack<String>();  // holds stack of cities which should not be
						// attempted to infect again since an outbreak occurred there on this card instance
						outbreakOnce.push(cityToInfect);
						cubeControllerArray[j].pushCube(cubeInfect);  // put the cube back!  Outbreak happened, can't put 4th cube on city.
						// outbreak, note city of origin to get connections and prevent infinite outbreak syndrome
						// for the first outbreak, the cityToInfect and origin are the same.
						outbreak(cityToInfect, outbreakOnce, cubeInfect.getColor());  
						infectionDiscard.pushCard(drawnCard);  // since we're leaving the function, put the card into discard now
						return;  // exit out of function if there's an outbreak, so it doesn't erroneously trigger more than 1 outbreak 
					}
				}
				else { // if the cube dispenser is empty, game over
					gameOver = true;
					gameOverReason = "You ran out of " + cubeControllerArray[j].getCubeColor() + " cubes";
					return;
				}
			}
		}
				// if there isn't a medic in the city, and if the disease isn't cured, proceed with infections.  Otherwise, stop them altogether
				// If curesDiscovered returns false, even if there's a medic there, the disease must be cured for this to take effect.
				// A city cannot outbreak if it cannot infect, so this fulfills the remainder of the Medic's abilities.
		else {  // if a medic is playing, then do the check for whether they are in the city and cures were discovered.
			if (playerArray[medicIndex].getCurrentCity().getName().compareTo(cityToInfect) != 0 && curesDiscovered.checkColorCure(cityColor) == false) {
				for (int i = 0; i < cubesToPlace; i++) {
					if (cubeControllerArray[j].isEmpty() == false) { // give cubes for as long as there is any to give
						cubeInfect = cubeControllerArray[j].popCube(); // only give a cube if there's still any left
						outbreak = worldCities.findCity(cityToInfect).addCube(cubeInfect);  // infect this city with a cube
						if (outbreak == true) {
							
							// rules regarding outbreaks: once per card, a city may outbreak a maximum of one time.
							Stack<String> outbreakOnce = new Stack<String>();  // holds stack of cities which should not be
							// attempted to infect again since an outbreak occurred there on this card instance
							outbreakOnce.push(cityToInfect);
							cubeControllerArray[j].pushCube(cubeInfect);  // put the cube back!  Outbreak happened, can't put 4th cube on city.
							// outbreak, note city of origin to get connections and prevent infinite outbreak syndrome
							// for the first outbreak, the cityToInfect and origin are the same.
							outbreak(cityToInfect, outbreakOnce, cubeInfect.getColor());  
							infectionDiscard.pushCard(drawnCard);  // since we're leaving the function, put the card into discard now
							return;  // exit out of function if there's an outbreak, so it doesn't erroneously trigger more than 1 outbreak 
						}
					}
					else { // if the cube dispenser is empty, game over
						gameOver = true;
						gameOverReason = "You ran out of " + cubeControllerArray[j].getCubeColor() + " cubes";
						return;
					}
				}
			}
		}
		// discard the infection card now that you infected the city
		infectionDiscard.pushCard(drawnCard);
	}
	
	public void infectCity(String cubeColor, String cityToInfect, Stack<String> cityOfOrigin) {  // only call from outbreak
		int j = 0;  // counter to pick the right cube stack to dispense cubes
		while (cubeControllerArray[j].getCubeColor().compareTo(cubeColor) != 0) {
			j++;
		}
		boolean outbreak = false;  // assume city doesn't have an outbreak initially
		CubeModel cubeInfect;
		
		// adding this check to the secondary infectCity function was necessary.  This is only called if an outbreak occurs.
		// This checks if the cubes that are spread from an outbreak are in the range of the quarantine specialist.
		// This operates on the proviso that the quarantine specialist was not in range of the city that experienced the outbreak,
		// but anything that falls in the periphery of the quarantine specialist is prevented.
		// A similar check is performed on the preliminary version of this function.
		if (quarantinePresent == true) {  // build a city immunity list if a quarantine specialist is in the game
			// create an array of immune cities, the size of it's connections + the city itself
			boolean immunityFound = false;
			int length = ((playerArray[quarantineIndex].getCurrentCity().getConnections().length) + 1);
			String[] immuneCities = new String[length];
			String[] connections = playerArray[quarantineIndex].getCurrentCity().getConnections();
			// manually copy over city + connections, add current city to last index of array
			immuneCities[length - 1] = playerArray[quarantineIndex].getCurrentCity().getName();
			for (int m = 0; m < (length - 1); m++) {
				immuneCities[m] = connections[m];
			}
			for (int k = 0; k < length; k++) {
				// if the quarantine specialist is in or adjacent to a city about to be infected, prevent it from occurring per role rules.
				// outbreaks cannot occur if disease cubes can't be placed by prevention, this fulfills the quarantine specialist role.
				if (immuneCities[k].compareTo(cityToInfect) == 0) {
					immunityFound = true;
				}
			}
			if (immunityFound == true) {  // if the city that was to be infected is on the immunity list, discard the card, don't infect, and leave the function
				return;
			}
		}
		
		if (cubeControllerArray[j].isEmpty() == false) { // give cubes for as long as there is any to give
			cubeInfect = cubeControllerArray[j].popCube(); // only give a cube if there's still any left
			outbreak = worldCities.findCity(cityToInfect).addCube(cubeInfect);  // infect this city with a cube
			
			if (outbreak == true) {
				
				cityOfOrigin.push(cityToInfect);  // add new outbreak city to stack of cities to prevent a second outbreak.
				cubeControllerArray[j].pushCube(cubeInfect);  // put the cube back!  Outbreak happened, can't put 4th cube on city.
				outbreak(cityToInfect, cityOfOrigin, cubeInfect.getColor());  // outbreak, note city of origin to get connections and prevent infinite outbreak syndrome
				return;  // exit out of function if there's an outbreak, so it doesn't erroneously trigger more than 1 outbreak 
			}
		}
		else { // if the cube dispenser is empty, game over
			gameOver = true;
			gameOverReason = "You ran out of " + cubeControllerArray[j].getCubeColor() + " cubes";
			return;
		}
	}
	
	public void outbreak(String cityToInfect, Stack<String> cityOfOrigin, String cubeColor) {
		boolean worldWidePanic;
		worldWidePanic = outbreaks.increaseOutbreaksLevel();  // increment outbreak counter
		if (worldWidePanic == true) {  // game ends if 8th outbreak occurs
			gameOver = true;
			gameOverReason = "There were too many outbreaks";
			return;
		}
		City sourceOutbreak = worldCities.findCity(cityToInfect);  // copy city of origin to temp variable
		String[] sourceConnections = sourceOutbreak.getConnections();
		int whileTracker;
		int sizeOfOutbreaks = cityOfOrigin.size();
		for (int i = 0; i < sourceConnections.length; i++) {
			boolean found = false;	// if a city has had an outbreak already, don't re-infect it again via chain reaction
			whileTracker = 0;
			while (whileTracker < sizeOfOutbreaks) {
				if (sourceConnections[i].compareTo(cityOfOrigin.elementAt(whileTracker)) == 0) {
					found = true; // if you find a matching city in list, don't infect it
				}
				whileTracker++;
			}
			if (found == false) {  // if the current connection doesnt match a previously infected city, then infect it
				infectCity(cubeColor, sourceConnections[i], cityOfOrigin);
			}
			// The size needs to be re-affirmed each loop.  If you had a chain reaction of outbreaks, then new cities
			// were added to the list of outbreaks, and you must ensure they are not infected again a second time if
			// they already suffered an outbreak.
			sizeOfOutbreaks = cityOfOrigin.size();
		}
	}
	
	public void update(float deltaTime) {
		touchEvents = game.getInput().getTouchEvents();
		//game.getInput().getKeyEvents();
        touchLen = touchEvents.size();
        for(int i = 0; i < touchLen; i++) {
            event1 = touchEvents.get(i);
            if (event1.type == TouchEvent.TOUCH_DOWN) {
            	lifted = false;
            }
            if (event1.type == TouchEvent.TOUCH_DRAGGED) {
            	
            	if (inBounds (event1, 30, 30, 357, 180)) {
            	}
        			//listens for touch on x axis scroll bar
            	if (inBounds (event1, /*30, 0, 360, 40*/0, 0, 360, 180)) {
        		//Log.d("track is event X", Integer.toString(event.x));
        		//Log.d("track is event Y", Integer.toString(event.y));
            		if (lifted == true) {
            			distancex = 0;
            		}
            		if (event1.x <= trackBall_XaxisLow) {
            			trackBall_Xaxis = trackBall_XaxisLow;
            			
            		}
            		else if (event1.x >= trackBall_XaxisLow && event1.x <= trackBall_XaxisHigh) {
            			/*
            			if (event1.x - trackBall_Xaxis > 5 || event1.x - trackBall_Xaxis < -5) {
            				//distancex = getXaxis_Percentage(event1.x) - trackX;
            				//trackX = getXaxis_Percentage(distancex);
            				//trackX = getXaxis_Percentage(event1.x);
            				//trackBall_Xaxis = event1.x;
            				//distancex = -1;
            				Log.d("event1x - trackBall_Xaxis - distancex = ", Integer.toString(event1.x - trackBall_Xaxis - distancex));
            				distancex = trackBall_Xaxis += (event1.x - trackBall_Xaxis);
            			}
            			else {
            				//Log.d("event1x - trackBall_Xaxis = ", Integer.toString(event1.x - trackBall_Xaxis));
                			Log.d("distancex is ", Integer.toString(distancex));
                			if (event1.x > trackBall_Xaxis){
                				trackBall_Xaxis += (event1.x - trackBall_Xaxis);
                			}
                			else if (event1.x < trackBall_Xaxis) {
                				trackBall_Xaxis += (event1.x - trackBall_Xaxis);
                			}
                			//trackBall_Xaxis = event1.x;
            			}
            			*/
            			//trackBall_Xaxis += (event1.x - trackBall_Xaxis);
            			
            			if (event1.x > distancex) {
            				if (event1.x - distancex > 10) {
            					//trackBall_Xaxis += 1;
            					//trackBall_Xaxis -= 1;
            				}
            				else if (event1.x - distancex <= 10 && event1.x - distancex > 0) {
            					//trackBall_Xaxis += event1.x - distancex;
            					trackBall_Xaxis -= event1.x - distancex;
            				}
            				
            			}
            			else {
            				if (event1.x - distancex < -10) {
            					//trackBall_Xaxis -= 1;
            					//trackBall_Xaxis += 1;
            				}
            				else if (event1.x - distancex >= -10 && event1.x - distancex < 0){
            					//trackBall_Xaxis += event1.x - distancex;
            					trackBall_Xaxis -= event1.x - distancex;
            				}
            			}
            			Log.d("distancex = ", Integer.toString(distancex));
            			
            			distancex = event1.x;
            			//trackBall_Xaxis = event1.x;
            			
            		}
            		trackX = getXaxis_Percentage (trackBall_Xaxis);
            		Log.d("trackX ", Integer.toString(trackX));
            		Log.d("eventX ", Integer.toString(event1.x));
            		overlapDelay = true;
            		
            	}
        			//listens for touch on y axis scroll bar
            	if (inBounds (event1, /*0, 10, 40, 290*/0, 0, 360, 180)) {
            		if (lifted == true) {
            			distancey = 0;
            		}
            		if (event1.y <= trackBall_YaxisLow) {
            			trackBall_Yaxis = trackBall_YaxisLow;
            			
            		}
            		else if (event1.y >= trackBall_YaxisLow && event1.y <= trackBall_YaxisHigh) {
            			/*
            			if (event1.y - trackBall_Yaxis > 5 || event1.y - trackBall_Yaxis < -5) {
            				
            				//distancey = getYaxis_Percentage(event1.y) - trackY;
            				//trackY = getYaxis_Percentage(distancey);
            				//trackY = getYaxis_Percentage(event1.y);
            			}
            			//Log.d("distancey is ", Integer.toString(distancey));
            			trackBall_Yaxis = event1.y;
            			*/
            			if (event1.y > distancey) {
            				if (event1.y - distancey > 10) {
            					//trackBall_Yaxis += 1;
            					//trackBall_Yaxis -= 1;
            				}
            				else if (event1.y - distancey <= 10 && event1.y - distancey > 0) {
            					//trackBall_Yaxis += event1.y - distancey;
            					trackBall_Yaxis -= event1.y - distancey;
            					
            				}
            				
            			}
            			else {
            				if (event1.y - distancey < -10) {
            					//trackBall_Yaxis -= 1;
            					//trackBall_Yaxis += 1;
            					
            				}
            				else if (event1.y - distancey >= -10 && event1.y - distancey < 0){
            					//trackBall_Yaxis += event1.y - distancey;
            					trackBall_Yaxis -= event1.y - distancey;
            				}
            			}
            			Log.d("distancey = ", Integer.toString(distancey));
            			distancey = event1.y;
            		}
            		
            		
            		trackY = getYaxis_Percentage (trackBall_Yaxis);
            		//Log.d("trackY ", Integer.toString(trackY));
            		overlapDelay = true;
            		
            	}
            }
            
            if(event1.type == TouchEvent.TOUCH_UP) {
            	lifted = true;
            	if (onlyThis == true) {
            			// vertical cancel button
            		if (inBounds (event1, 448, 211, 30, 100)){
            			onlyThis = false;
            			cardInfoTabSwitcher = 0;
            		}
            	}
            		// under cubeStats tab, displaying player discard pile
            	else if (tabSwitcher == 4 && cardInfoTabSwitcher == 4){
            		// vertical cancel button
            		if (inBounds (event1, 448, 211, 30, 100)){
            			cardInfoTabSwitcher = 0;
            			cardPageView = 0;
            		}
            		if (inBounds (event1, 417, 231, 30, 105)) { // listens for the right arrow
            			int limit = (playerDiscard.getStackOfCards().size() / 5) - 1;
            			int limitRemainder = playerDiscard.getStackOfCards().size() % 5;
            			if (limitRemainder > 0) {
            				limit++;
            			}
            			if (cardPageView <  limit) {
            				cardPageView++;
            			}
            		}
            		if (inBounds (event1, 0, 231, 30, 105) && cardPageView > 0) { // listens for the left arrow
            			cardPageView--;
            		}
            	}
            		// under cubeStats tab, displaying infection discard pile
            	else if (tabSwitcher == 4 && cardInfoTabSwitcher == 5){
            		// vertical cancel button
            		if (inBounds (event1, 448, 211, 30, 100)){
            			cardInfoTabSwitcher = 0;
            			cardPageView = 0;
            		}
            		if (inBounds (event1, 417, 231, 30, 105)) { // listens for the right arrow
            			int limit = (infectionDiscard.getStackOfCards().size() / 4) - 1;
            			int limitRemainder = infectionDiscard.getStackOfCards().size() % 4;
            			if (limitRemainder > 0) {
            				limit++;
            			}
            			if (cardPageView <  limit) {
            				cardPageView++;
            			}
            		}
            		if (inBounds (event1, 0, 231, 30, 105) && cardPageView > 0) { // listens for the left arrow
            			cardPageView--;
            		}  
            	}
            		// everything else
            	else if (tabSwitcher == 4 && onlyThis == false) {
            			// vertical cancel button
            		if (inBounds (event1, 448, 211, 30, 100) && cardInfoTabSwitcher == 0) {
            			// canceling from cards info tab will show you the home tab by default, rather than nothing 
            			tabSwitcher = 1;
            		}
            		// second layer vertical cancel button
            		if (inBounds (event1, 448, 211, 30, 100) && cardInfoTabSwitcher > 0) {
            			cardInfoTabSwitcher = 0;
            		}
        			// player one cards
            		if (inBounds (event1, 10, 245, 35, 35)) {
            			cardInfoTabSwitcher = 1;
            		}
            			// player two cards
            		if (inBounds (event1, 85, 245, 35, 35) && playerArray.length > 2) {
            			cardInfoTabSwitcher = 2;
            		}
            			// player three cards
					if (inBounds (event1, 160, 245, 35, 35) && playerArray.length > 3) {
						cardInfoTabSwitcher = 3;
					}
						// player discard piles
					if (inBounds (event1, 300, 240, 44, 60)){
						cardInfoTabSwitcher = 4;
					}
						// infection discard piles
					if (inBounds (event1, 365, 255, 60, 44)){
						cardInfoTabSwitcher = 5;
					}
            	}
            		// listens to everything other than cards tab
            	else {
            		if (inBounds (event1, 415, 210, 70, 50)) {  // closes action menu
            			if (actions == true) {  // this brings back the tabs menu
            				if (actionInProgress != true) {
            					actions = false;
            				}
            			}
            		}
            		if (inBounds (event1, 2, 210, 73, 40)) {  // opens home tab
            			if (actions == false && drawMap_assetToggle == 1) { // This prevents the touch listener from activating for the tabs while in action menu
            				tabSwitcher = 1;
            				// auto scroll to player's position
            				tempPlayer_moveToggle = true;
                    		scrollMoveX = getPlayer_midPointX (playerArray[playerTurnTracker].getCurrentCity().getXCoordinate() + offsetX + 16);
                    		scrollMoveY = getPlayer_midPointY (playerArray[playerTurnTracker].getCurrentCity().getYCoordinate() + offsetY + 16);
                    		newPlayerX = -(playerArray[playerTurnTracker].getCurrentCity().getXCoordinate() - offsetX);
                    		newPlayerY = -(playerArray[playerTurnTracker].getCurrentCity().getYCoordinate() - offsetX);
                    		if (scrollMoveX > 0) {
                    			scrollMoveX = -scrollMoveX;
                    		}
                    		if (scrollMoveY > 0) {
                    			scrollMoveY = - scrollMoveY;
                    		}
                    		trackBall_Xaxis = moveScroll_withPlayerX (scrollMoveX);
                    		trackBall_Yaxis = moveScroll_withPlayerY (scrollMoveY);
            			}
            		}
            		if (inBounds (event1, 79, 210, 73, 40)) {  // opens cubes tab
            			if (actions == false && drawMap_assetToggle == 1) { // This prevents the touch listener from activating for the tabs while in action menu
            				tabSwitcher = 2;
            			}
            		}
            		if (inBounds (event1, 156, 210, 73, 40)) {  // opens stats tab
            			if (actions == false && drawMap_assetToggle == 1) { // This prevents the touch listener from activating for the tabs while in action menu
            				tabSwitcher = 3;
            			}
            		}
            		if (inBounds (event1, 233, 210, 73, 40)) {  // opens cards tab
            			if (actions == false && drawMap_assetToggle == 1) { // This prevents the touch listener from activating for the tabs while in action menu
            				tabSwitcher = 4;
            			}
            		}
            	}
            	if (inBounds (event1, 15, 210, 70, 50) && overlapDelay == false) { // launches Drive/Ferry action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            			driving = true;
            			actionInProgress = true;
            			overlapDelay = true;
            			}
            		}
            	}
            	if (inBounds (event1, 15, 270, 70, 55) && overlapDelay == false) { // launches Charter Flight action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				charter = true;
            				actionInProgress = true;
            				overlapDelay = true;
            			}
            		}
            	}
            	if (inBounds (event1, 95, 210, 70, 50) && overlapDelay == false) { // launches Direct Flight action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				directFlight = true;
            				actionInProgress = true;
            				overlapDelay = true;
            			}
            		}
            	}
            	if (inBounds (event1, 95, 270, 70, 55) && overlapDelay == false) { // launches Shuttle Flight action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				shuttleFlight = true;
            				actionInProgress = true;
            				overlapDelay = true;
            			}
            		}
            	}
            	
            	if (useEventCard == true && actionInProgress == true && overlapDelay == false) { // Event card
            		if (dump == false) { 
            			// dirty way of getting an event card that is destined for discard to get played nicely
                		Card temp;
                		if (eventCardsAvailable >= 1) {
                			if (inBounds (event1, 15, 235, 45, 70)) { // listens to one of 5 possible event cards, and plays them
                				cardHolder = eventCardOwners[0];
                				// handle event cards played by contingency planners differently
                				if (playerArray[eventCardOwners[0]].getRole() == Roles.PLANNER && eventCardLoc[0] == -15000) {
                					temp = playerArray[eventCardOwners[0]].removeContingencyPlannerEventSpecial();
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					purgatory.pushCard(temp); // removes the card from the game if it was a specially stored event card by a planner per game gules
                				}
                				else {
                					temp = playerArray[eventCardOwners[0]].removeCard(eventCardLoc[0]);
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					playerDiscard.pushCard(temp);  // removes event card to demonstrate at least removing the card
                				}            				
                			}
                		}
                		if (eventCardsAvailable >= 2) {
                			if (inBounds (event1, 75, 235, 45, 70)) { // listens to one of 5 possible event cards, and plays them
                				cardHolder = eventCardOwners[1];
                				// handle event cards played by contingency planners differently
                				if (playerArray[eventCardOwners[1]].getRole() == Roles.PLANNER && eventCardLoc[1] == -15000) {
                					temp = playerArray[eventCardOwners[1]].removeContingencyPlannerEventSpecial();
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					purgatory.pushCard(temp); // removes the card from the game if it was a specially stored event card by a planner per game gules
                				}
                				else {
                					temp = playerArray[eventCardOwners[1]].removeCard(eventCardLoc[1]);
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					playerDiscard.pushCard(temp);  // removes event card to demonstrate at least removing the card
                				}  
                			}
                		}
                		if (eventCardsAvailable >= 3) {
                			if (inBounds (event1, 135, 235, 45, 70)) { // listens to one of 5 possible event cards, and plays them
                				cardHolder = eventCardOwners[2];
                				// handle event cards played by contingency planners differently
                				if (playerArray[eventCardOwners[2]].getRole() == Roles.PLANNER && eventCardLoc[2] == -15000) {
                					temp = playerArray[eventCardOwners[2]].removeContingencyPlannerEventSpecial();
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					purgatory.pushCard(temp); // removes the card from the game if it was a specially stored event card by a planner per game gules
                				}
                				else {
                					temp = playerArray[eventCardOwners[2]].removeCard(eventCardLoc[2]);
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					playerDiscard.pushCard(temp);  // removes event card to demonstrate at least removing the card
                				}  
                			}
                		}
                		if (eventCardsAvailable >= 4) {
                			if (inBounds (event1, 195, 235, 45, 70)) { // listens to one of 5 possible event cards, and plays them
                				cardHolder = eventCardOwners[3];
                				// handle event cards played by contingency planners differently
                				if (playerArray[eventCardOwners[3]].getRole() == Roles.PLANNER && eventCardLoc[3] == -15000) {
                					temp = playerArray[eventCardOwners[3]].removeContingencyPlannerEventSpecial();
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					purgatory.pushCard(temp); // removes the card from the game if it was a specially stored event card by a planner per game gules
                				}
                				else {
                					temp = playerArray[eventCardOwners[3]].removeCard(eventCardLoc[3]);
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					playerDiscard.pushCard(temp);  // removes event card to demonstrate at least removing the card
                				}  
                			}
                		}
                		if (eventCardsAvailable >= 5) {
                			if (inBounds (event1, 255, 235, 45, 70)) { // listens to one of 5 possible event cards, and plays them
                				cardHolder = eventCardOwners[4];
                				// handle event cards played by contingency planners differently
                				if (playerArray[eventCardOwners[4]].getRole() == Roles.PLANNER && eventCardLoc[4] == -15000) {
                					temp = playerArray[eventCardOwners[4]].removeContingencyPlannerEventSpecial();
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					purgatory.pushCard(temp); // removes the card from the game if it was a specially stored event card by a planner per game gules
                				}
                				else {
                					temp = playerArray[eventCardOwners[4]].removeCard(eventCardLoc[4]);
                					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(temp);
                					playerDiscard.pushCard(temp);  // removes event card to demonstrate at least removing the card
                				}  
                			}
                		}
            		}
            		
            		switch(eventCardPlayed){
            		case 1:
            			airlift = true;
            			useEventCard = false;
            			eventCardPlayed = 0;
            			break;
            		case 2:	
            			forecast = true;
            			useEventCard = false;
            			eventCardPlayed = 0;
            			break;
            		case 3:
            			governmentGrant = true;
            			useEventCard = false;
            			eventCardPlayed = 0;
            			break;
            		case 4:
            			oneQuietNight = true;
            			useEventCard = false;
            			eventCardPlayed = 0;
            			break;
            		case 5:
            			resilientPopulation = true;
            			useEventCard = false;
            			cardPageView = 0;  // resets to default position of zero
            			eventCardPlayed = 0;
            			break;
            		default:
            			useEventCard = false;
            			actionInProgress = false;
            			airlift = false;
                		forecast = false;
                		governmentGrant = false;
                		oneQuietNight = false;
                		resilientPopulation = false;
                		cardPageView = 0;  // resets to default position of zero
            			eventCardPlayed = 0;
            			dump = false;
            			preventCancel = false;
            		}
            		
            		if (inBounds (event1, 320, 240, 170, 70) && preventCancel == false) {// listener for cancel for event card
            			useEventCard = false;
                		actionInProgress = false;
                		airlift = false;
                		forecast = false;
                		governmentGrant = false;
                		oneQuietNight = false;
                		resilientPopulation = false;
                		cardPageView = 0;  // resets to default position of zero
                		// help to debug if there is bad usage of cardholder
            			cardHolder = -1;
                		dump = false;
                		preventCancel = false;
            		}
            		
            		if (dump == true) {
            			dump = false;
            		}
            		
        			overlapDelay = true;
            	}
            	if (inBounds (event1, 335, 270, 70, 55) && overlapDelay == false) { // launches Use Event Card action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				useEventCard = true;
            				actionInProgress = true;
            				overlapDelay = true;
            			}
            		}
            	}
            	if (inBounds (event1, 175, 210, 70, 50) && overlapDelay == false) { // launches Treat Disease action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				treatDisease = true;
            				overlapDelay = true;
            			}
            		}
            	}
        		// actual implementation of Treat Disease action
            	if (treatDisease == true && actionInProgress == true && overlapDelay == false && playerArray[playerTurnTracker].getAction() < 4) {
            		int dildotreat;  // brought back as an inside joke
            		if (inBounds (event1, 230, 235, 35, 35)) {
            			// handles dilemma of managing multiple cubes as a medic.  Action only incrementing once on last cube.  Uncured diseases still take an action to treat, even as a medic
            			if (playerArray[playerTurnTracker].getRole() == Roles.MEDIC || curesDiscovered.getBlackCure() == true) {
            				while (playerArray[playerTurnTracker].getCurrentCity().getBlackCubeCount() > 0) {
            					removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("black", curesDiscovered);
            					if (removeCubeSuccess != null) {
            						blackCubes.pushCube(removeCubeSuccess);
            					}
            				}
            			}
            			else {
            				removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("black", curesDiscovered);
            				if (removeCubeSuccess != null) {
                				blackCubes.pushCube(removeCubeSuccess);
                			}
            			}
            		}
            		if (inBounds (event1, 110, 235, 35, 35)) {
            			if (playerArray[playerTurnTracker].getRole() == Roles.MEDIC || curesDiscovered.getBlueCure() == true) {
            				while (playerArray[playerTurnTracker].getCurrentCity().getBlueCubeCount() > 0) {
            					removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("blue", curesDiscovered);
            					if (removeCubeSuccess != null) {
            						blueCubes.pushCube(removeCubeSuccess);
            					}
            				}
            			}
            			else {
            				removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("blue", curesDiscovered);
            				if (removeCubeSuccess != null) {
                				blueCubes.pushCube(removeCubeSuccess);
                			}
            			}
            		}
            		if (inBounds (event1, 230, 280, 35, 35)) {
            			if (playerArray[playerTurnTracker].getRole() == Roles.MEDIC || curesDiscovered.getRedCure() == true) {
            				while (playerArray[playerTurnTracker].getCurrentCity().getRedCubeCount() > 0) {
            					removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("red", curesDiscovered);
            					if (removeCubeSuccess != null) {
            						redCubes.pushCube(removeCubeSuccess);
            					}
            				}
            			}
            			else {
            				removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("red", curesDiscovered);
            				if (removeCubeSuccess != null) {
                				redCubes.pushCube(removeCubeSuccess);
                			}
            			}
            		}
            		if (inBounds (event1, 110, 280, 35, 35)) {
            			if (playerArray[playerTurnTracker].getRole() == Roles.MEDIC || curesDiscovered.getYellowCure() == true) {
            				while (playerArray[playerTurnTracker].getCurrentCity().getYellowCubeCount() > 0) {
            					removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("yellow", curesDiscovered);
            					if (removeCubeSuccess != null) {
            						yellowCubes.pushCube(removeCubeSuccess);
            					}
            				}
            			}
            			else {
            				removeCubeSuccess = playerArray[playerTurnTracker].treatDisease("yellow", curesDiscovered);
            				if (removeCubeSuccess != null) {
                				yellowCubes.pushCube(removeCubeSuccess);
                			}
            			}
            		}
            		if (playerArray[playerTurnTracker].getAction() >= 4) {
            			treatDisease = false;
            		}
            		if (inBounds (event1, 320, 240, 170, 70)) {
            			treatDisease = false;
                		actionInProgress = false;
                		overlapDelay = true;
            		}
            	}
            	// actual implementation of airlift event
            	if (airlift == true && actionInProgress == true ) {
            		if (inBounds (event1, 110, 235, 35, 35)) {
            			airliftFlyer = 0;
            			charter = true;
            		}
            		if (inBounds (event1, 230, 235, 35, 35)) {
            			airliftFlyer = 1;
            			charter = true;
            		}
            		if ((playerArray.length > 2) && inBounds (event1, 110, 280, 35, 35)) {
            			airliftFlyer = 2;
            			charter = true;
            		}
            		if ((playerArray.length > 3) && inBounds (event1, 230, 280, 35, 35)) {
            			airliftFlyer = 3;
            			charter = true;
            		}
            		if (inBounds (event1, 320, 240, 170, 70) && preventCancel == false){
            				// card returned to original player, if airlift cancelled
            			playerArray[cardHolder].addCard(playerDiscard.drawCard());
            			// help to debug if there is bad usage of cardholder
            			cardHolder = -1;
            			airlift = false;
            			actionInProgress = false;
            			useEventCard = false;
            			dump = false;
            			preventCancel = false;
            		}
            	}
            	// cancel button for governmentGrant Event Card
            	if (governmentGrant == true && actionInProgress == true && preventCancel == false){
            		if (inBounds (event1, 320, 240, 170, 70)) {// listener for cancel for event card
            			// takes back the spent card from discard if the player hits cancel
            			playerArray[cardHolder].addCard(playerDiscard.drawCard());
        				governmentGrant = false;
        				useEventCard = false;
            			actionInProgress = false;
            			// help to debug if there is bad usage of cardholder
            			cardHolder = -1;
            			dump = false;
            			preventCancel = false;
            			for (int t = 0; t < 48; t++) {
        					destX1[t] = 0;		// removes stale data
        					destY1[t] = 0;		// removes stale data
        					destCity1[t] = null;// removes stale data
        				}
        			}
            	}
            	
            	// implementation of forecast
            	if (useForecast == true && actionInProgress == true && overlapDelay == false){
            		for (int r = 0; r < 6; r++) {
            			if (inBounds (event1, forecastX[r], 240, 75, 55) && forecastX[r] != 0){
            				forecastChoice--;	// decrement by one to indicate a choice was made
            				forecastReturn[forecastChoice] = infectionDeck.getStackOfCards().remove(forecastIndex[r]);  // store in order the cards picked by the player
            				for (int t = 0; t < 6; t++) {
            					forecastX[t] = 0;			// zero out the data
            					forecastIndex[t] = 0;	// zero out the data
            				}
            			}
            		}
            		if (forecastChoice == 0){
            			// now that you're out of choices, return the cards back on top of the infection draw deck
            			// the direction you run the for loop will indicate the order in which cards are put onto the stack
            			// current order: first is on bottom, last card picked is on top.
            			for (int t = 5; t >= 0; t--) {
            				infectionDeck.pushCard(forecastReturn[t]);
            			}
            			useForecast = false;
            			useEventCard = false;
            			actionInProgress = false;
            			dump = false;
            			preventCancel = false;
            		}
            			
            	}
            	// option stage of forecast
            	if (forecast == true && actionInProgress == true && overlapDelay == false){
            		// listener for yes choice
            		if (inBounds (event1, 140, 230, 170, 70)) {
            			forecast = false;
            			useForecast = true;
            			forecastChoice = 6;
            		}
            		if (inBounds (event1, 320, 240, 170, 70) && preventCancel == false) {// listener for cancel for event card
            			// takes back the spent card from discard if the player hits cancel
            			playerArray[cardHolder].addCard(playerDiscard.drawCard());
            			// help to debug if there is bad usage of cardholder
            			cardHolder = -1;
            			forecast = false;
        				useEventCard = false;
            			actionInProgress = false;
            			overlapDelay = true;
            			dump = false;
            			preventCancel = false;
            		}
            	}
            	
            	if(oneQuietNight == true && actionInProgress == true && overlapDelay == false){
            			// listener for yes choice
            		if (inBounds (event1, 140, 230, 170, 70)) {
            			useOneQuietNight = true;
            			oneQuietNight = false;
            			useEventCard = false;
            			actionInProgress = false;
            			overlapDelay = true;
            			dump = false;
            			preventCancel = false;
            		}
            			// listener for cancel button
            		if (inBounds (event1, 320, 240, 170, 70) && preventCancel == false){
            			// takes back the spent card from discard if the player hits cancel
            			playerArray[cardHolder].addCard(playerDiscard.drawCard());
            			// help to debug if there is bad usage of cardholder
            			cardHolder = -1;
            			oneQuietNight = false;
            			actionInProgress = false;
            			useEventCard = false;
            			overlapDelay = true;
            			dump = false;
            			preventCancel = false;
            		}
            	}
            	
            	// listens for the choice for resilient pop. event card.  Look at the cards and choose one to remove.
            	if (resilientPopulation == true && actionInProgress == true && overlapDelay == false) { 
    				for (int y = 0; y < 4; y++) {
    					if (inBounds (event1, resilientX[y], 240, 90, 65) && resilientX[y] != 0) { // listens for the card itself being chosen for purgatory
    						// the chosen card is sent to purgatory, ie, removed from the game.
    						purgatory.getStackOfCards().push(infectionDiscard.getStackOfCards().remove(resilientIndex[y]));
    						resilientPopulation = false;
    						actionInProgress = false;
    						dump = false;
    						preventCancel = false;
    						overlapDelay = true;
    					}
    				}
            		
            		if (inBounds (event1, 417, 231, 30, 105)) { // listens for the right arrow
            			int limit = (infectionDiscard.getStackOfCards().size() / 4) - 1;
            			int limitRemainder = infectionDiscard.getStackOfCards().size() % 4;
            			if (limitRemainder > 0) {
            				limit++;
            			}
            			if (cardPageView <  limit) {
            				cardPageView++;
            				overlapDelay = true;
            			}
            		}
            		if (inBounds (event1, 0, 231, 30, 105) && cardPageView > 0) { // listens for the left arrow
            			cardPageView--;
            			overlapDelay = true;
            		}            		
            		if (inBounds (event1, 448, 231, 30, 105) && preventCancel == false) { // separate listener to cancel resilient population event card
            			 resilientPopulation = false;
            			 actionInProgress = false;
            			 playerArray[cardHolder].addCard(playerDiscard.drawCard());  // rips the spent card back out of the discard pile
            			 cardPageView = 0;  // resets to default position of zero
            			 // help to debug if there is bad usage of cardholder
             			 cardHolder = -1;
            			 overlapDelay = true;
            			 dump = false;
            			 preventCancel = false;
            		}
            	}
            	
            	if (shareKnowledge == true && actionInProgress == true && researchGive == true && overlapDelay == false){ 
            		if (inBounds (event1, 448, 231, 30, 105)) { // separate listener to cancel researcher initiating giving a card
            			researchGive = false;
            		}
            	}
            	if (shareKnowledge == true && actionInProgress == true && researchGive == true && overlapDelay == false) {
            		for (int s = 0; s < 7; s++) {
            			if (inBounds (event1, shareX[s], 220, 60, 80) && shareX[s] != 0){
            				chosenResearchOverwrite = shareY[s];
            				researchGive = false;
            				for (int t = 0; t < 7; t++) {
                    			shareX[t] = 0;	// zero out the array to remove stale data
                    			shareY[t] = 0;	// zero out the array to remove stale data
                    		}
            				overlapDelay = true;
            			}
            		}
            	}
            	
            	if (shareKnowledge == true && actionInProgress == true && sharingDecisionMade == true && overlapDelay == false) {  // once decision is made, decide who to give/take from
            		if (nonResearcher == true) {
            			if (inBounds (event1, 0, 230, 50, 75) && giving == false) {  // listener for if you are taking a card from a non-researcher
                			shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[chosenShareKnowledge], playerArray[playerTurnTracker], playerArray[chosenShareKnowledge].getCards().get(chosenCardPos));
                			overlapDelay = true;
                		}
            		}
            		if (researcher == true) {
            			for (int s = 0; s < 7; s++) {  // listens for researcher cards if taking from them
                    		if (inBounds (event1, shareZ[s], 220, 50, 75) && shareZ[s] != 0){
                    			chosenResearchOverwrite = researcherTakeLoc[s];
                    			shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[researcherIndex], playerArray[playerTurnTracker], playerArray[researcherIndex].getCards().get(chosenResearchOverwrite));
                    			for (int t = 0; t < 7; t++) {
                    				shareZ[t] = 0;	// zero out the array to remove stale data
                    			}
                    			overlapDelay = true;
                    		}
                    	}
            		}
            		if (playersInSameCity >= 1) {
            			if (inBounds (event1, 20, 225, 45, 25) && giving == true) {  // listener for if you are giving a card away
            				if (playerArray[playerTurnTracker].getRole() == Roles.RESEARCHER) {
            					shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[playerTurnTracker], playerArray[giveCardPos[0]], playerArray[playerTurnTracker].getCards().get(chosenResearchOverwrite));
            				}
            				if (chosenLately == true) {  // prevents stale data from getting used
            					shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[playerTurnTracker], playerArray[giveCardPos[0]], playerArray[playerTurnTracker].getCards().get(chosenCardPos));
            				}
            				
                		}
            		}
            		if (playersInSameCity >= 2) {
            			if (inBounds (event1, 105, 225, 45, 25) && giving == true) {  // listener for if you are giving a card away
            				if (playerArray[playerTurnTracker].getRole() == Roles.RESEARCHER) {
            					shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[playerTurnTracker], playerArray[giveCardPos[1]], playerArray[playerTurnTracker].getCards().get(chosenResearchOverwrite));
            				}
            				if (chosenLately == true) {   // prevents stale data from getting used
            					shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[playerTurnTracker], playerArray[giveCardPos[1]], playerArray[playerTurnTracker].getCards().get(chosenCardPos));
            				}	
                		}
            		}
            		if (playersInSameCity == 3) {
            			if (inBounds (event1, 190, 225, 45, 25) && giving == true) {  // listener for if you are giving a card away
            				if (playerArray[playerTurnTracker].getRole() == Roles.RESEARCHER) {
            					shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[playerTurnTracker], playerArray[giveCardPos[2]], playerArray[playerTurnTracker].getCards().get(chosenResearchOverwrite));
            				}
            				if (chosenLately == true) {
            					shareCardSuccess = playerArray[playerTurnTracker].giveCityCard(playerArray[playerTurnTracker], playerArray[giveCardPos[2]], playerArray[playerTurnTracker].getCards().get(chosenCardPos));
            				}
                		}
            		}
            		if (inBounds (event1, 448, 231, 30, 105)) {  // listener for cancel, last chance to back out of share knowledge
            			shareKnowledge = false;
            			actionInProgress = false;
            			sharingDecisionMade = false;
            			researchGive = false;
            			giving = false;
            			taking = false;
            			nonResearcher = false;
            			researcher = false;
            			chosenLately = false;
            			overlapDelay = true;
            		}
            		if (shareCardSuccess == true) {
            			// this needs to be done manually, the giver or receiver could be the one whose turn the action should be incremented
            			playerArray[playerTurnTracker].manuallyIncreaseActionCount();
        				shareKnowledge = false;
            			actionInProgress = false;
            			sharingDecisionMade = false;
            			giving = false;
            			taking = false;
            			shareCardSuccess = false;
            			nonResearcher = false;
            			researcher = false;
            			chosenLately = false;
            			overlapDelay = true;
        			}
            	}
            	if (shareKnowledge == true && actionInProgress == true && sharingDecisionMade == false && overlapDelay == false) { // listener for making choice: give or take a card
            		if (inBounds (event1, 10, 250, 160, 60)) {  // listener for giving a card
            			sharingDecisionMade = true;
            			giving = true;
            			if (playerArray[playerTurnTracker].getRole() == Roles.RESEARCHER) {
            				researchGive = true;
            			}
            		}
            		if (inBounds (event1, 170, 250, 160, 60)) {  // listener for taking a card
            			sharingDecisionMade = true;
            			giving = false;
            		}
            		if (inBounds (event1, 330, 250, 160, 60)) { // listener for the cancel option
            			shareKnowledge = false;
            			actionInProgress = false;
            			sharingDecisionMade = false;
            			researchGive = false;
            			giving = false;
            			taking = false;
            			nonResearcher = false;
            			researcher = false;
            			chosenLately = false;
            			overlapDelay = true;
            		}
            		
            	}
            	if (inBounds (event1, 175, 270, 70, 55) && overlapDelay == false) { // launches Share Knowledge/card action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				shareKnowledge = true;
            				actionInProgress = true;
            				overlapDelay = true;
            			}
            		}
            	}
            	if (discoverCure == true && extraCards == true && overlapDelay == false) {
                	for (int z = 0; z < 7; z++) {
                		if (inBounds (event1, discoverX[z], 235, 68, 96) && discoverX[z] != 0) {
                			discoverSave[discoverQuantity] = playerArray[playerTurnTracker].removeCard(discoverIndex[z]);
                			discoverQuantity++;
                			for (int t = 0; t < 7; t++) {
                				discoverX[t] = 0;		// zero out stale data
                				discoverIndex[t] = 0;	// zero out stale data
                			}
                			String discoverColor = cureColor();
                			int exactNeeded = colorCardCounter(discoverColor);
                			
                			// if you have the minimum required cards, go find the cure.  Otherwise keep going back to choose cards to save.
                			// The discover cure function is non-deterministic beyond color, so this is necessary for saving cards.
                			if (playerArray[playerTurnTracker].getRole() == Roles.SCIENTIST) {
                				if (exactNeeded == 4) {
                					playerArray[playerTurnTracker].discoverCure(playerDiscard, curesDiscovered);
                					for (int w = 0; w < discoverQuantity; w++) {  // add the saved cards back to the player hand
                						if (discoverSave[w] != null) {
                							playerArray[playerTurnTracker].addCard(discoverSave[w]);
                						}
                						discoverSave[w] = null;
                					}
                					discoverCure = false;
                					extraCards = false;
                					actionInProgress = false;
                					overlapDelay = true;
                					discoverQuantity = 0;
                				}
                			}
                			else {
                				if (exactNeeded == 5) {
                					playerArray[playerTurnTracker].discoverCure(playerDiscard, curesDiscovered);
                					for (int w = 0; w < discoverQuantity; w++) { // add the saved cards back to the player hand
                						if (discoverSave[w] != null) {
                							playerArray[playerTurnTracker].addCard(discoverSave[w]);
                						}
                						discoverSave[w] = null;
                					}
                					discoverCure = false;
                					extraCards = false;
                					actionInProgress = false;
                					overlapDelay = true;
                					discoverQuantity = 0;
                				}
                			}
                		}
                	}
                }            	
            	
            	if (inBounds (event1, 255, 210, 70, 50) && overlapDelay == false) { // launches Find Cure action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				String discoverColor = cureColor();
                			int exactNeeded = colorCardCounter(discoverColor);
            				boolean exactAmount = false;
            				discoverCure = false;	// ensures it is not erroneously set
            				extraCards = false;		// ensures it is not erroneously set
            				if (playerArray[playerTurnTracker].getRole() == Roles.SCIENTIST) {
            					if (exactNeeded ==  4) {
            						exactAmount = true;
            					}
            					if (exactNeeded > 4) {
            						extraCards = true;
            						discoverCure = true;
            						actionInProgress = true;
            						discoverQuantity = 0;  // tracks where to store saved cards later on.  Reset between cures discovered.
            					}
            				}
            				else {
            					if (exactNeeded == 5) {
            						exactAmount = true;
            					}
            					if (exactNeeded > 5) {
            						extraCards = true;
            						discoverCure = true;
            						actionInProgress = true;
            						discoverQuantity = 0;  // tracks where to store saved cards later on.  Reset between cures discovered.
            					}
            				}
            				if (exactAmount == true) {
            					playerArray[playerTurnTracker].discoverCure(playerDiscard, curesDiscovered);
            				}
            				overlapDelay = true;
            			}
            		}
            	}
            	
            	if (inBounds (event1, 255, 270, 70, 55) && overlapDelay == false) { // launches Build Research Station action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				if (worldCities.getNumberOfResearchStations() < 6) {
            					// attempts to build a research station without applying any changes
            					boolean hasRightCard = playerArray[playerTurnTracker].buildStation(playerArray[playerTurnTracker].getCurrentCity());
            					
            					// if you have the card and right location, build a research station, dump the card into discard
            					if (hasRightCard == true) {
            						// builds a research station for the GUI
            						worldCities.buildResearchStation(playerArray[playerTurnTracker].getCurrentCity());
            						// discards from player hand into player discard deck
            						// if the player is an operations expert, they do not need to discard a card
            						if (playerArray[playerTurnTracker].getRole() != Roles.OPERATIONS_EXPERT) {
            							playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(playerArray[playerTurnTracker].getCurrentCity()));
            						}
            						
            					}
            				}
            			}
            		}
            	}
            	if (inBounds (event1, 335, 210, 70, 50) && overlapDelay == false) { // launches End Turn action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				phaseOneComplete = true;
            			}
            		}
            	}
            	if (inBounds (event1, 415, 270, 70, 55) && overlapDelay == false) { // launches Special (Role Based) action
            		if (actions == true) {  // Do not remove this line.  This lets you use the touch listener for when the action bar is open
            			if (actionInProgress != true) {
            				// if the player is an ops expert in a city with a research station, and hasn't used the ability yet during their turn, allow the special ability to activate
            				if (playerArray[playerTurnTracker].getRole() == Roles.OPERATIONS_EXPERT && playerArray[playerTurnTracker].getCurrentCity().getResearchCenterStatus() == true && opsExpertMoveUsed == false) {
            					actionInProgress = true;
            					opsExpertMove = true;
            					overlapDelay = true;
            				}
            				// if the player is a contingency planner, let them steal cards from the player discard pile if available
            				if (playerArray[playerTurnTracker].getRole() == Roles.PLANNER) {
            					actionInProgress = true;
            					contPlannerStealEventCard = true;
            					overlapDelay = true;
            				}
            				// if the player is a dispatcher, then enable their pawn-moving capabilities
            				if (playerArray[playerTurnTracker].getRole() == Roles.DISPATCHER) {
            					actionInProgress = true;
            					dispatcherMove = true;
            					overlapDelay = true;
            				}
            			}
            		}
            	}
            	if (actions == true && dispatchActive == false && actionInProgress == true && dispatcherMove == true && overlapDelay == false && pawnChosen == true && dispatcherControl == true) {
            		if (inBounds (event1, 15, 210, 70, 50)) { // launches Drive/Ferry action for controlled pawn
            			dispatchDriving = true;
            			dispatchActive = true;  // gets these listeners to stop listening while traveling under control of dispatcher
            			overlapDelay = true;
            		}
                	if (inBounds (event1, 15, 270, 70, 55)) { // launches Charter Flight action for controlled pawn
                		dispatchCharter = true;
                		dispatchActive = true;  // gets these listeners to stop listening while traveling under control of dispatcher
            			overlapDelay = true;
                	}
                	if (inBounds (event1, 95, 210, 70, 50)) { // launches Direct Flight action for controlled pawn
                		dispatchDirect = true;
                		dispatchActive = true;  // gets these listeners to stop listening while traveling under control of dispatcher
            			overlapDelay = true;
                	}
                	if (inBounds (event1, 95, 270, 70, 55)) { // launches Shuttle Flight action for controlled pawn
                		dispatchShuttle = true;
                		dispatchActive = true;  // gets these listeners to stop listening while traveling under control of dispatcher
            			overlapDelay = true;
                	}
                	if (inBounds (event1, 322, 240, 156, 60)) { // dispatcher Control cancel button listener 
                		dispatchDriving = false;
                		dispatchCharter = false;
                		dispatchDirect = false;
                		dispatchShuttle = false;
            			dispatcherMove = false;
            			dispatcherJoin = false;
            			dispatcherControl = false;
            			dispatchActive = false;
            			actionInProgress = false;
            			pawnChosen = false;
            			overlapDelay = true;
            		}
            	}
            	
            	if (actions == true && actionInProgress == true && dispatcherMove == true && overlapDelay == false && pawnChosen == true && dispatcherJoin == true) {
            		for (int v = 0; v < 4; v++) {
            			if (inBounds (event1, joinX[v], joinY[v], 33, 33) && joinX[v] != 0){
            				playerArray[playerTurnTracker].movePlayerToACity(playerArray[chosenPawnIndex], joinCity[v]);
                			dispatcherControl = false;
                			dispatcherJoin = false;
                			dispatcherMove = false;
                			pawnChosen = false;
                			actionInProgress = false;
                			overlapDelay = true;
                			for (int t = 0; t < 4; t++) {
                    			joinX[t] = 0;		// zero out the array to remove stale data
                    			joinY[t] = 0;		// zero out the array to remove stale data
                    			joinCity[t] = null; // zero out the array to remove stale data
                    		}
            			}
            		}
            		
            		if (inBounds (event1, 322, 240, 156, 60)) { // dispatcher Join cancel button listener
            			dispatcherMove = false;
            			dispatcherJoin = false;
            			dispatchActive = false;
            			dispatcherControl = false;
            			actionInProgress = false;
            			pawnChosen = false;
            			overlapDelay = true;
            		}
            	}
            	
            	// implements the dispatcher move a pawn as it's own capability
            	if (actions == true && dispatchActive == true && actionInProgress == true && dispatcherMove == true && overlapDelay == false && dispatcherControl == true && (dispatchDriving == true || dispatchDirect == true || dispatchShuttle == true || dispatchCharter == true)) {
            		for (int a = 0; a < 48; a++) {
            			if (inBounds (event1, controlX[a], controlY[a], 33, 33) && controlX[a] != 0) {
            				if (dispatchDriving == true) {
            					playerArray[playerTurnTracker].dispatchDriveFerry(playerArray[chosenPawnIndex], controlCity[a]);
            					dispatchDriving = false;
            					dispatchActive = false;
            					overlapDelay = true;
            				}
            				if (dispatchDirect == true) {
            					boolean success = playerArray[playerTurnTracker].dispatchDirect(playerArray[chosenPawnIndex], controlCity[a]);
            					if (success == true) {
            						playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(controlCity[a]));
            					}
            					dispatchDirect = false;
            					dispatchActive = false;
            					overlapDelay = true;
            				}
            				if (dispatchCharter == true) {
            					int success = playerArray[playerTurnTracker].dispatchCharter(playerArray[chosenPawnIndex], controlCity[a]);
            					if (success != -1) {
        							playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(success));
        						}
            					dispatchCharter = false;
            					dispatchActive = false;
            					overlapDelay = true;
            				}
            				if (dispatchShuttle == true) {
            					playerArray[playerTurnTracker].dispatchShuttle(playerArray[chosenPawnIndex], controlCity[a], researchCities);
            					dispatchShuttle = false;
            					dispatchActive = false;
            					overlapDelay = true;
            				}
            				for (int t = 0; t < 48; t++) {
        						controlX[t] = 0;		// zero out the array to remove stale data
        						controlY[t] = 0;		// zero out the array to remove stale data
        						controlCity[t] = null;	// zero out the array to remove stale data
        					}
            				if (playerArray[playerTurnTracker].getAction() >= 4) { // if dispatcher used up 4 actions, put the toys down and go back to the cahpet (kindergarten cop reference)
        						dispatchShuttle = false;
                				dispatchCharter = false;
                				dispatchDirect = false;
                				dispatchDriving = false;
            					dispatchActive = false;
                    			dispatcherMove = false;
                    			dispatcherJoin = false;
                    			dispatcherControl = false;
                    			actionInProgress = false;
                    			pawnChosen = false;
                    			overlapDelay = true;
        					}
            			}
            		}
            		if (inBounds (event1, 322, 240, 156, 60)) { // dispatcher cancel button listener
        				dispatchShuttle = false;
        				dispatchCharter = false;
        				dispatchDirect = false;
        				dispatchDriving = false;
    					dispatchActive = false;
            			dispatcherMove = false;
            			dispatcherJoin = false;
            			dispatcherControl = false;
            			actionInProgress = false;
            			pawnChosen = false;
            			overlapDelay = true;
            		}
            	}
            	
            	// dispatcher pawn selection screen (you're picking a pawn to move at this stage)
            	if (actions == true && dispatchActive == false && actionInProgress == true && dispatcherMove == true && overlapDelay == false && pawnChosen == false && (dispatcherJoin == true || dispatcherControl == true)) {
            		for (int u = 0; u < 4; u++) {
            			if (inBounds (event1, chosenPawnCoord[u], 250, 25, 25) && chosenPawnCoord[u] != 0) {
            				pawnChosen = true;
            				chosenPawnIndex = chosenPawnLoc[u]; // index of the chosen pawn in the player array
            				overlapDelay = true;
            				for (int t = 0; t < 4; t++) {
                    			chosenPawnLoc[t] = 0;		// zero out the array to remove stale data
                    			chosenPawnCoord[t] = 0;		// zero out the array to remove stale data
                    		}
            			}
            		}
            		if (inBounds (event1, 322, 240, 156, 60)) { // dispatcher cancel button listener
            			dispatcherMove = false;
            			dispatchActive = false;
            			dispatcherJoin = false;
            			dispatcherControl = false;
            			actionInProgress = false;
            			pawnChosen = false;
            			overlapDelay = true;
            		}
            	}
            	
            	// dispatcher choice menu.  Here is where you choose to either move one pawn to another, or to control another pawn as your own piece
            	if (actions == true && dispatchActive == false && actionInProgress == true && dispatcherMove == true && overlapDelay == false && dispatcherJoin == false && dispatcherControl == false) { // listener for dispatcher special move, for choosing control pawn vs joining pawn
            		if (inBounds (event1, 2, 240, 156, 60)) { // dispatcher join pawns listener
            			dispatcherJoin = true;
            		}
            		if (inBounds (event1, 162, 240, 156, 60)) { // dispatcher pawn control listener
            			dispatcherControl = true;
            		}
            		if (inBounds (event1, 322, 240, 156, 60)) { // dispatcher cancel button listener
            			dispatchActive = false;
            			dispatcherMove = false;
            			dispatcherJoin = false;
            			dispatcherControl = false;
            			actionInProgress = false;
            			pawnChosen = false;
            			overlapDelay = true;
            		}
            	}

            	if (actions == true && actionInProgress == true && contPlannerStealEventCard == true && overlapDelay == false) { // special contingency planner steal/use event card
            		if (playerArray[playerTurnTracker].checkForStoredContingencyPlannerCard() == false) {
            			for (int q = 0; q < 5; q++) {
                			if (inBounds (event1, plannerCardCoord[q], 213, 70, 100) && plannerCardCoord[q] != 0) {
                				playerArray[playerTurnTracker].addContingencyPlannerEventSpecial(playerDiscard.getStackOfCards().remove(plannerCardLoc[q]));
                				contPlannerStealEventCard = false;
                				actionInProgress = false;
                				overlapDelay = true;
                				playerArray[playerTurnTracker].manuallyIncreaseActionCount();  // planner stealing a card takes an action
                				for (int t = 0; t < 5; t++) {
                        			plannerCardLoc[t] = 0;		// zero out the array to remove stale data
                        			plannerCardCoord[t] = 0;	// zero out the array to remove stale data
                        		}
                			}
                		}
            		}
            		
            		if (inBounds (event1, 448, 231, 30, 105) && overlapDelay == false) {  // listener for cancel for the contingency planner steal/use event card special
            			contPlannerStealEventCard = false;
            			actionInProgress = false;
            			overlapDelay = true;
            		}
            	}
            	if (actions == true && actionInProgress == true && opsExpertMove == true  && chosenSacrifice == false && overlapDelay == false) {  // touch listener for ops expert travel card selection stage
            		for (int p = 0; p < 7; p++) {
            			if (inBounds (event1, opsCardCoord[p], 220, 60, 80) && opsCardCoord[p] != 0) {
            				opsExpertSacrifice = opsCardLoc[p];
            				chosenSacrifice = true;
            				// point of no return.  Can change later, but for now, once you dump the card, you must travel
            				playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(opsExpertSacrifice));
            				charter = true;  // uses the same functionality as a charter flight, so why not tap into it?  David's great idea!
            				for (int t = 0; t < 7; t++) {
                    			opsCardLoc[t] = 0;		// zero out the array to remove stale data
                    			opsCardCoord[t] = 0;	// zero out the array to remove stale data
                    		}
            				overlapDelay = true;
            			}
            		}
            		if (inBounds (event1, 448, 231, 30, 105)) {  // listener for cancel, last chance to back out of ops expert special travel ability
            			opsExpertMove = false;
            			actionInProgress = false;
            			chosenSacrifice = false;
            			overlapDelay = true;
            		}
            	}
            	
            	if (actions == true && overlapDelay == false && (driving == true || directFlight == true || shuttleFlight == true || charter == true)){ 
            		if (inBounds (event1, 330, 250, 160, 60) && preventCancel == false) { // separate listener to cancel traveling for any type
            			directFlight = false;
            			charter = false;
            			shuttleFlight = false;
            			driving = false;
            			actionInProgress = false;
            			overlapDelay = true;
            			for (int t = 0; t < 48; t++) {
        					destX1[t] = 0;		// removes stale data
        					destY1[t] = 0;		// removes stale data
        					destCity1[t] = null;// removes stale data
        				}
            		}
            	}
            	if (actions == true && overlapDelay == false && (driving == true || directFlight == true || shuttleFlight == true || charter == true || governmentGrant == true)) {
            		for (int k = 0; k < 48; k++) {
            			if (inBounds (event1, destX1[k], destY1[k], 33, 33) && destX1[k] != 0){
            				if (driving == true) {
            					playerArray[playerTurnTracker].driveFerry(destCity1[k]);
            					driving = false;
            				}
            				if (directFlight == true) {
            					City temp = destCity1[k];
            					boolean success = playerArray[playerTurnTracker].directFlight(temp);
            					if (success == true) {
            						playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(temp));
            					}
            					directFlight = false;
            				}
            				if (shuttleFlight == true) {
            					City temp = destCity1[k];
            					playerArray[playerTurnTracker].shuttleFlight(temp, researchCities);
            					shuttleFlight = false;
            				}
            				if (charter == true) {
            					City temp = destCity1[k];
            					// airlift from event card, no discard necessary
            					if (airlift == true){
            						playerArray[airliftFlyer].airlift(temp);
            						airlift = false;
            						charter = false;
            						dump = false;
            						preventCancel = false;
            					}
            					else if (opsExpertMove == true) {
            						// same functionality as an airlift, but uses an action and can only move self once per turn
            						playerArray[playerTurnTracker].airlift(temp);
            						playerArray[playerTurnTracker].manuallyIncreaseActionCount();
            						charter = false;
            						opsExpertMove = false;
            						chosenSacrifice = false;
            						opsExpertMoveUsed = true;
            					}
            					else {
            						int success = playerArray[playerTurnTracker].charterFlight(temp);
            						if (success != -1) {
            							playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(success));
            						}
            						charter = false;
            					}
            				}
            				actionInProgress = false;
            				
            				if (governmentGrant == true){
            					City temp = destCity1[k];
            					if (worldCities.buildResearchStation(temp) == true) {
            						governmentGrant = false;
            						dump = false;
            						preventCancel = false;
            					}
            					else{
            						actionInProgress = true;
            						//dump = false;
            						//preventCancel = false;
            					}
            				}
            				
            				for (int t = 0; t < 48; t++) {
            					destX1[t] = 0;		// removes stale data
            					destY1[t] = 0;		// removes stale data
            					destCity1[t] = null;// removes stale data
            				}
            				overlapDelay = true;
            			}
            		}
            	}
            	if (phaseOneComplete == true && card1Drawn == false && overlapDelay == false) {
            		if (inBounds (event1, 30, 30, mapCard_sizeX, mapCard_sizeY)) {
            			card1Drawn = true;
            			phase2Draw1 = playerDeck.drawCard();
            			resolveCard(playerArray[playerTurnTracker],phase2Draw1);
            			if (phase2Draw1.getType().compareTo("Epidemic Card") != 0) {
							playerArray[playerTurnTracker].addCard(phase2Draw1);
							
						}
            			card1Resolved = true;
            			overlapDelay = true;
            		}
            		
            	}
            	if (phaseOneComplete == true && card2Drawn == false && overlapDelay == false) {
            		if (inBounds (event1, 30, 30, mapCard_sizeX, mapCard_sizeY)) {
            			card2Drawn = true;
            			phase2Draw2 = playerDeck.drawCard();
            			resolveCard(playerArray[playerTurnTracker],phase2Draw2);
            			overlapDelay = true;
            			if (phase2Draw2.getType().compareTo("Epidemic Card") != 0) {
							playerArray[playerTurnTracker].addCard(phase2Draw2);
							
						}
            			card2Resolved = true;
            			overlapDelay = true;
            		}		
            	}
            	if (playerArray[playerTurnTracker].getTotalCards() > 7 && overlapDelay == false && preventCancel == false) {
            		for (int k = 0; k < 15; k++) {
            			if (inBounds (event1, overLimitX[k], 235, 48, 67) && overLimitX[k] != 0) {
            				// discards chosen card
            				//temp = playerArray[eventCardOwners[4]].removeCard(eventCardLoc[4]);
            				if (playerArray[playerTurnTracker].getCards().get(overLimitIndex[k]).getType().compareTo("Event Card") == 0) {
            					dump = true;
            					preventCancel = true;
            					useEventCard = true;
            					eventPlayed = playerArray[playerTurnTracker].getCards().get(overLimitIndex[k]).getFormalName();
            					cardHolder = playerTurnTracker;
            					eventCardPlayed = playerArray[playerTurnTracker].useEventCard(playerArray[playerTurnTracker].getCards().get(overLimitIndex[k]));
            					playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(overLimitIndex[k]));
            				}
            				else {
            					playerDiscard.pushCard(playerArray[playerTurnTracker].removeCard(overLimitIndex[k]));
            				}
            				for (int t = 0; t < 15; t++) {
            					overLimitX[t] = 0;		// removes stale data
            					overLimitIndex[t] = 0;	// removes stale data
            				}
            				if (playerArray[playerTurnTracker].getCards().size() < 8 && dump == false) {
            					actionInProgress = false;
            				}
            			}
            		}
            	}
            	
            	if (inBounds (event1, 30, 30, 357, 180) && actions != true) { // touch listener to hide/show cards and tabs menu
            		if (actionInProgress != true) {
            			if (drawMap_assetToggle == 0) {
                			//drawMap_assetToggle = 1;
                		}
                		else if (drawMap_assetToggle ==1) {
                			//drawMap_assetToggle = 0;
                		}
                		//Log.d("touch map", "isTrue");
                	}
            	}
            	if (inBounds (event1, 310, 210, 73, 40) && tabSwitcher < 4) {  // opens action menu.  Moved to bottom to correct action misfires
            		if (actions == false && drawMap_assetToggle == 1) { // This prevents the touch listener from activating for the tabs while in action menu
            			actions = true;
            		}
            	}
            } // end of TOUCH_UP
            
            overlapDelay = false;
        }
	}
	
	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }
	
	@Override
	public void present(float deltaTime) {
		g.drawPixmap(GameAssets.gameMap,(trackX + offsetX),trackY + offsetY);
		String showGameOver = "";
		
		// debugging: all cures discovered to test medic auto-remove capability.  It works.
		/*
		curesDiscovered.setBlackCure(true);
		curesDiscovered.setBlueCure(true);
		curesDiscovered.setRedCure(true);
		curesDiscovered.setYellowCure(true);
		*/
		
		// Display all the research stations in the world
		if (worldCities != null) {
			for (int i = 0; i < worldCities.getWorldCities().length; i++) {
				if (allCities == null) {
					allCities = worldCities.getWorldCities();
				}
				else {
					if (allCities[i].getResearchCenterStatus() == true)
						// draws a tab box just to help show when you discover a cure, a visual aid if you will
					g.drawPixmap(GameAssets.researchStation, allCities[i].getXCoordinate() + offsetX + trackX - 16, allCities[i].getYCoordinate() + offsetY + trackY - 16);
				}
			}
		}
		
		//playerArray[playerTurnTracker].getRoleCardImage();
		// Displays all pawns on the map
		if (playerArray != null) {
			int xAdjust = 0;
			int yAdjust = 0;
			for (int i = 0; i < playerArray.length; i++) {
				if (i == 0) {
					xAdjust = -28;
					yAdjust = -11;
				}
				else if (i == 1) {
					xAdjust = 10;
					yAdjust = -11;
				}
				else if (i == 2) {
					xAdjust = -10;
					yAdjust = 11;
				}
				else {
					xAdjust = -10;
					yAdjust = -31;
				}
				g.drawPixmap(playerArray[i].getPawnImage(), playerArray[i].getCurrentCity().getXCoordinate() + offsetX + trackX + xAdjust, playerArray[i].getCurrentCity().getYCoordinate() + offsetY + trackY + yAdjust);
				if (playerTurnTracker == i) {
					g.drawPixmap(GameAssets.currentPlayer, playerArray[i].getCurrentCity().getXCoordinate() + offsetX + trackX + xAdjust - 2, playerArray[i].getCurrentCity().getYCoordinate() + offsetY + trackY + yAdjust - 2);
				}
			}
		}
		
		// Displays cubes for all infected cities on the map
		if (worldCities != null) {
			for (int i = 0; i < worldCities.getWorldCities().length; i++) {
				if (allCities == null) {
					allCities = worldCities.getWorldCities();
				}
				else {
					color1.setColor(Color.GREEN);
					if (allCities[i].getBlackCubeCount() != 0) {
						g.drawTextColor(String.valueOf(allCities[i].getBlackCubeCount()),trackX + offsetX + allCities[i].getXCoordinate()+19, trackY + offsetY + allCities[i].getYCoordinate() +20, color1);
						g.drawPixmap(GameAssets.blackCube, trackX + offsetX + 4 + allCities[i].getXCoordinate(), trackY + offsetY + 8 + allCities[i].getYCoordinate());
					}
					if (allCities[i].getBlueCubeCount() != 0) {
						g.drawTextColor(String.valueOf(allCities[i].getBlueCubeCount()),trackX + offsetX + allCities[i].getXCoordinate()-29, trackY + offsetY + allCities[i].getYCoordinate()+20, color1);
						g.drawPixmap(GameAssets.blueCube, trackX + offsetX + allCities[i].getXCoordinate()-19, trackY + offsetY + 8 + allCities[i].getYCoordinate());
					}
					if (allCities[i].getRedCubeCount() != 0) {
						g.drawTextColor(String.valueOf(allCities[i].getRedCubeCount()),trackX + offsetX + allCities[i].getXCoordinate()-29, trackY + offsetY + allCities[i].getYCoordinate()-12, color1);
						g.drawPixmap(GameAssets.redCube, trackX + offsetX + allCities[i].getXCoordinate()-19, trackY + offsetY + allCities[i].getYCoordinate()-24);
					}
					if (allCities[i].getYellowCubeCount() != 0) {
						g.drawTextColor(String.valueOf(allCities[i].getYellowCubeCount()),trackX + offsetX + allCities[i].getXCoordinate()+19, trackY + offsetY + allCities[i].getYCoordinate()-12, color1);
						g.drawPixmap(GameAssets.yellowCube, trackX + offsetX + 4 + allCities[i].getXCoordinate(), trackY + offsetY + allCities[i].getYCoordinate()-24);
					}
				}
			}
		}
		
		// draw the cards second to last once the new action menu is in place
		if (drawMap_assetToggle == 1) {
			if (actions == false)	{
				g.drawPixmap(GameAssets.mainTabs, 0, 211);
			}
			// Display the cards in the current player's hand
			//playerCards = playerArray[playerTurnTracker].getCards(); 
			for (int i = 0 ; i < playerArray[playerTurnTracker].getTotalCards(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getType().compareTo("Player Card") == 0) {
					cardSizeX = 121;
					cardSizeY = 167;
				}
				else {
					cardSizeX = 244;
					cardSizeY = 339;
				}
				g.drawPixmap(playerArray[playerTurnTracker].getCards().get(i).getImage(), 386, (mapCard_incrementY * i), 0, 0, cardSizeX, cardSizeY, mapCard_sizeX, mapCard_sizeY);
				// if you're a contingency planner, show that you did steal an event card by displaying it in your hand
				if (playerArray[playerTurnTracker].getRole() == Roles.PLANNER && i == (playerArray[playerTurnTracker].getTotalCards() - 1) && playerArray[playerTurnTracker].checkForStoredContingencyPlannerCard() == true) {
					g.drawPixmap(playerArray[playerTurnTracker].getContingencyPlannerEventSpecial().getImage(), 386, (mapCard_incrementY * (i + 1)), 0, 0, 244, 339, mapCard_sizeX, mapCard_sizeY);
				}
			}
				
		}
		else if (drawMap_assetToggle == 0) {
			
		}
		
		// actual card sizes for your reference
		// 244 339 = epidemic card	(x, y)
		// 244 339 = event card		(x, y)
		// 121 167 = city card 		(x, y)
		// 119 168 = player card back (x, y)
		// 167 118 = infection card (x, y)  <-- size not tested on screen yet
				
		 // If the player runs out of actions, end player turn
		if (playerArray[playerTurnTracker].getAction() == 4) {
			phaseOneComplete = true;
		}
		
		//abstraction for playing the game
		if (phaseOneComplete == true) {
			actionInProgress = true;
			victory = curesDiscovered.checkForVictory();  // check for victory condition.  Would happen after actions are complete, not at end of player turn
			if (victory == true) {
				gameOver = true;
				gameOverReason = "You have saved the world!";
			}
			if (gameOver == false) {
				// BEGINNING OF PHASE 2 OF PLAYER TURN
				
				// PHASE II: Draw 2 cards
				
				// Draw the first card, show it on screen
				if (card1Drawn == false) {
					if (playerDeck.isEmpty() == false) {
						if (playerDeck.getStackOfCards().peek().getType().compareTo("Player Card") == 0) {
							cardSizeX = 121;
							cardSizeY = 167;
						}
						else {
							cardSizeX = 244;
							cardSizeY = 339;
						}
						g.drawPixmap(playerDeck.getStackOfCards().peek().getImage(), 30, 30, 0, 0, cardSizeX, cardSizeY, mapCard_sizeX, mapCard_sizeY);
					}
					else {
						gameOver = true;
						gameOverReason = "You ran out of player cards to draw.";
					}
				}
				
				// Draw the second card, show it on screen	
				if (card2Drawn == false) {
					if (playerDeck.isEmpty() == false) {
						if (playerDeck.getStackOfCards().peek().getType().compareTo("Player Card") == 0) {
							cardSizeX = 121;
							cardSizeY = 167;
						}
						else {
							cardSizeX = 244;
							cardSizeY = 339;
						}
						g.drawPixmap(playerDeck.getStackOfCards().peek().getImage(), 30, 30, 0, 0, cardSizeX, cardSizeY, mapCard_sizeX, mapCard_sizeY);
					}
					else {
						gameOver = true;
						gameOverReason = "You ran out of player cards to draw.";
					}
				}
				
				// Only if both cards are drawn, then proceed to confirming if they're resolved
				if (card1Drawn == true && card2Drawn == true) {
					cardsDrawn = true;
				}

				// Only if both cards were resolved, should you proceed and mark phase II as complete
				if (card1Resolved == true && card2Resolved == true) {
					phaseTwo = true;
				}
				
				// beginning of phase 3: making sure it isn't run before all the above steps are finished
				if (phaseTwo == true  && playerArray[playerTurnTracker].getTotalCards() <= 7 && phaseThree == false) {
					// PHASE III : Infect new cities
					
					// code for if the event card one quiet night is played 
					if (useOneQuietNight == true){
						useOneQuietNight = false;
						oneQuietNight = false;
					}
					else{
						for (int i = 0; i < infectRate.getInfectionRate(); i++) {
							if (infectionDeck.isEmpty() == false) {
								phase3Draw = infectionDeck.drawCard();
								infectCity(1, phase3Draw); // add 1 cube to the city displayed on the card
							}
							else {
								gameOver = true;
								gameOverReason = "Somehow, you ran out of infection cards.  Weird.";
							}
										
						}
					}
					// marks the end of phase III
					phaseThree = true;
				}
				
				// PLAYER TURN OFFICIALLY OVER
				if (phaseTwo == true && phaseThree == true && playerArray[playerTurnTracker].getTotalCards() <= 7) {
					playerArray[playerTurnTracker].setAction(0);
					playerTurnTracker++;
					if (playerTurnTracker == playerArray.length) {
						playerTurnTracker = 0;
					}
					numberOfPlayerTurns++;
					phaseOneComplete = false;
					phaseTwo = false;
					phaseThree = false;
					card1Resolved = false;
					card2Resolved = false;
					card1Drawn = false;
					card2Drawn = false;
					cardsDrawn = false;
					if (dump == false && preventCancel == false) {
						actionInProgress = false;
					}
					opsExpertMoveUsed = false;  // if there is an ops expert, make sure it is unused each turn
					tempPlayer_moveToggle = true;
					// move to new player's location automatically at end of turn
					// Need to look into the airlift event card being played if discarding down to 7.  It auto-scrolls
					// prematurely.
            		scrollMoveX = getPlayer_midPointX (playerArray[playerTurnTracker].getCurrentCity().getXCoordinate() + offsetX + 16);
            		scrollMoveY = getPlayer_midPointY (playerArray[playerTurnTracker].getCurrentCity().getYCoordinate() + offsetY + 16);
            		newPlayerX = -(playerArray[playerTurnTracker].getCurrentCity().getXCoordinate() - offsetX);
            		newPlayerY = -(playerArray[playerTurnTracker].getCurrentCity().getYCoordinate() - offsetX);
            		if (scrollMoveX > 0) {
            			scrollMoveX = -scrollMoveX;
            		}
            		if (scrollMoveY > 0) {
            			scrollMoveY = - scrollMoveY;
            		}
            		trackBall_Xaxis = moveScroll_withPlayerX (scrollMoveX);
            		trackBall_Yaxis = moveScroll_withPlayerY (scrollMoveY);
				}
				
			}
			else {
				if (victory == false) {
					showGameOver = "GAME OVER, YOU LOSE!";
					color1.setColor(Color.GREEN);
					g.drawTextColor(showGameOver, 100, 100, color1);
					g.drawTextColor(gameOverReason, 100, 115, color1);
				}
				else {
					showGameOver = "YOU WON!  YOU DISCOVERED ALL FOUR CURES!";
					color1.setColor(Color.GREEN);
					g.drawTextColor(showGameOver, 100, 100, color1);
					g.drawTextColor(gameOverReason, 100, 115, color1);
				}
			}
		}
		
		if (playerArray[playerTurnTracker].getTotalCards() > 7 && dump == false && useEventCard == false && (oneQuietNight == false && resilientPopulation == false && forecast == false && governmentGrant == false && airlift == false)) {
			actionInProgress = true;
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("You are over the 7 card limit.  Choose a card to discard", 5, 225);
			int offset = 3;
			int index = 0;
			for (int i = 0; i < playerArray[playerTurnTracker].getTotalCards(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getType().compareTo("Player Card") == 0) {
					cardSizeX = 121;
					cardSizeY = 167;
				}
				else {
					cardSizeX = 244;
					cardSizeY = 339;
				}
				g.drawPixmap(playerArray[playerTurnTracker].getCards().get(i).getImage(), offset, 235, 0, 0, cardSizeX, cardSizeY, 48, 67);
				overLimitX[index] = offset;
				overLimitIndex[index] = i;
				offset += 48;
				index++;
			}
			
		}
		
		if (medicPresent == true) {  // forces the game to constantly remove CURED diseases from the city the Medic is in regardless of turn, without using an action
			if (curesDiscovered.getBlackCure() == true) {
				while (playerArray[medicIndex].getCurrentCity().getBlackCubeCount() > 0) {
					medicCube = playerArray[medicIndex].getCurrentCity().removeCube("black");
					if (medicCube != null) {
						blackCubes.pushCube(medicCube);
					}
				}
			}
			if (curesDiscovered.getBlueCure() == true) {
				while (playerArray[medicIndex].getCurrentCity().getBlueCubeCount() > 0) {
					medicCube = playerArray[medicIndex].getCurrentCity().removeCube("blue");
					if (medicCube != null) {
						blueCubes.pushCube(medicCube);
					}
				}
			}
			if (curesDiscovered.getRedCure() == true) {
				while (playerArray[medicIndex].getCurrentCity().getRedCubeCount() > 0) {
					medicCube = playerArray[medicIndex].getCurrentCity().removeCube("red");
					if (medicCube != null) {
						redCubes.pushCube(medicCube);
					}
				}
			}
			if (curesDiscovered.getYellowCure() == true) {
				while (playerArray[medicIndex].getCurrentCity().getYellowCubeCount() > 0) {
					medicCube = playerArray[medicIndex].getCurrentCity().removeCube("yellow");
					if (medicCube != null) {
						yellowCubes.pushCube(medicCube);
					}
				}
			}
		}
		
		if (driving == true) {
			int length = playerArray[playerTurnTracker].getCurrentCity().getConnections().length;
			String [] driveFerryDest = playerArray[playerTurnTracker].getCurrentCity().getConnections();
			for (int i = 0; i < length; i++){
				g.drawPixmap(GameAssets.travelring, worldCities.findCity(driveFerryDest[i]).getXCoordinate() + offsetX + trackX - 17, worldCities.findCity(driveFerryDest[i]).getYCoordinate() + offsetY + trackY - 17);
				destX1[i] = worldCities.findCity(driveFerryDest[i]).getXCoordinate() + offsetX + trackX - 16;
				destY1[i] = worldCities.findCity(driveFerryDest[i]).getYCoordinate() + offsetY + trackY - 16;
				destCity1[i] = worldCities.findCity(driveFerryDest[i]);
			}
			g.drawPixmap(GameAssets.cancelButton, 320, 240);
		}
		
		if (charter == true){
			if (allCities == null) {
				allCities = worldCities.getWorldCities();
			}
			for (int i = 0; i < allCities.length; i++){
				if (allCities[i].getName().compareTo(playerArray[playerTurnTracker].getCurrentCity().getName()) != 0) {
					g.drawPixmap(GameAssets.travelring, allCities[i].getXCoordinate() + offsetX + trackX - 17, allCities[i].getYCoordinate() + offsetY + trackY - 17);
				}
			}
			if (destCity.size() <= 48) {
				for (int i = 0; i < allCities.length; i++){
					destX1[i] = allCities[i].getXCoordinate() + offsetX + trackX - 16;
					destY1[i] = allCities[i].getYCoordinate() + offsetY + trackY - 16;
					destCity1[i] = allCities[i];
				}	
			}
			g.drawPixmap(GameAssets.cancelButton, 320, 240);
		}
		
		if (directFlight == true) {
			City temp;
			for (int i = 0 ; i < playerArray[playerTurnTracker].getCards().size(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getType().compareTo("Player Card") == 0){
					if (playerArray[playerTurnTracker].getCards().get(i).getColor() != "N/A"){
						temp = worldCities.findCity(playerArray[playerTurnTracker].getCards().get(i).getFormalName());
						g.drawPixmap(GameAssets.travelring, temp.getXCoordinate() + offsetX + trackX - 17, temp.getYCoordinate() + offsetY + trackY - 17);
						destX1[i] = temp.getXCoordinate() + offsetX + trackX - 16;
						destY1[i] = temp.getYCoordinate() + offsetY + trackY - 16;
						destCity1[i] = temp;
					}
				}
			}
			g.drawPixmap(GameAssets.cancelButton, 320, 240);
		}
		
		if (shuttleFlight == true) {
			int j = 0;
			while (j < worldCities.getNumberOfResearchStations()) {
				researchCities[j] = worldCities.getResearchStations().elementAt(j);
				j++;
			}
			for (int i = 0; i < worldCities.getNumberOfResearchStations(); i++){
				if (researchCities[i].getName().compareTo(playerArray[playerTurnTracker].getCurrentCity().getName()) != 0) {
					g.drawPixmap(GameAssets.travelring, researchCities[i].getXCoordinate() + offsetX + trackX - 17, researchCities[i].getYCoordinate() + offsetY + trackY - 17);
				}
				destX1[i] = researchCities[i].getXCoordinate() + offsetX + trackX - 16;
				destY1[i] = researchCities[i].getYCoordinate() + offsetY + trackY - 16;
				destCity1[i] = researchCities[i];
			}
			g.drawPixmap(GameAssets.cancelButton, 320, 240);
		}
		if (treatDisease == true && playerArray[playerTurnTracker].getAction() < 4){
			int position = 0;
			for (int i = 0; i < allCities.length; i++){
				if (allCities[i] == playerArray[playerTurnTracker].getCurrentCity())
					position = i;
			}
			actionInProgress = true;
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText((playerArray[playerTurnTracker].getCurrentCity().getName() + "'s Disease Cubes"), 110, 225);
			g.drawPixmap(GameAssets.bigBlackCube, 230, 235);
			g.drawText(String.valueOf(allCities[position].getBlackCubeCount()), 265, 255);
			g.drawPixmap(GameAssets.bigBlueCube, 110, 235);
			g.drawText(String.valueOf(allCities[position].getBlueCubeCount()), 145, 255);
			g.drawPixmap(GameAssets.bigRedCube, 230, 280);
			g.drawText(String.valueOf(allCities[position].getRedCubeCount()), 265, 300);
			g.drawPixmap(GameAssets.bigYellowCube, 110, 280);
			g.drawText(String.valueOf(allCities[position].getYellowCubeCount()), 145, 300);
			g.drawPixmap(GameAssets.cancelButton, 320, 240);
			
		}
		// set charter to false, makes it a little less glitchy if you end up tapping a pawn which happens to be over a destination city
		if (airlift == true  && charter == false){	// special event
			actionInProgress = true;
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("Choose which Player to move: ", 110, 225);
			int offsetX = 110;
			int offsetY = 235;
			for (int i = 0; i < playerArray.length; i++) {
				if (i == 0 || i == 2) {
					offsetX = 110;
					if (i == 2) {
						offsetY = 280;
					}
				}
				else {
					offsetX = 230;
				}
				g.drawPixmap(playerArray[i].getPawnImage(), offsetX, offsetY);
				if (playerTurnTracker == i) {
					g.drawPixmap(GameAssets.currentPlayer, offsetX - 2, offsetY - 2);
				}
			}
			// X ordering:  	Y ordering:
			//		110				235
			//		230				235
			//		110				280
			//		230				280
			if (preventCancel == false) {
				g.drawPixmap(GameAssets.cancelButton, 320, 240);
			}
		}
		
		if (governmentGrant == true){
			if (allCities == null) {
				allCities = worldCities.getWorldCities();
			}
			for (int i = 0; i < allCities.length; i++){
				if (allCities[i].getResearchCenterStatus() == false) {
					g.drawPixmap(GameAssets.travelring, allCities[i].getXCoordinate() + offsetX + trackX - 17, allCities[i].getYCoordinate() + offsetY + trackY - 17);
					destX1[i] = allCities[i].getXCoordinate() + offsetX + trackX - 16;
					destY1[i] = allCities[i].getYCoordinate() + offsetY + trackY - 16;
					destCity1[i] = allCities[i];
				}
			}
			if (preventCancel == false) {
				g.drawPixmap(GameAssets.cancelButton, 320, 240);
			}
		}
		
		if (resilientPopulation == true) {
			int offset = 35;
			int displayMax = 4;
			int resIndex = 0;	// location of where to store
			int pageCount = (infectionDiscard.getStackOfCards().size() / 4) - 1;
			int remainder = infectionDiscard.getStackOfCards().size() % 4;
			if (cardPageView >=  (pageCount + 1) && remainder > 0) {
				displayMax = remainder;
			}
			if (remainder > 0) {
				pageCount++;
			}
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			if (preventCancel == false) {
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
			}
			g.drawText("Choose an infection card to remove from the game", 10, 225);
			for (int i = cardPageView * 4; i < (cardPageView * 4) + displayMax; i++) {
				if (cardPageView > 0) {
					g.drawPixmap(GameAssets.actionArrowLeft, 0, 211);
				}
				if (cardPageView < pageCount) {
					g.drawPixmap(GameAssets.actionArrowRight, 416, 211);
				}
				g.drawPixmap(infectionDiscard.getStackOfCards().get(i).getImage(), offset, 240, 0, 0, 167, 118, 95, 68);
				resilientX[resIndex] = offset;
				resilientIndex[resIndex] = i;
				offset += 95;
				resIndex++;
			}
		}
		
		// display while using forecast
		if (useForecast == true) {
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("Cards will be stacked in the order selected.  First card will be on bottom", 10, 225);
			int offset = 3;
			int top = (infectionDeck.getStackOfCards().size() - 1);  // top of the stack.  Decrementing lets you drill down further.
			for (int i = 0; i < forecastChoice; i++) {		// to fit 6, you need the max spacing to be 79 on the width.  Maintaining aspect ratio, the height it 71% of the width.
				g.drawPixmap(infectionDeck.getStackOfCards().get(top).getImage(), offset, 240, 0, 0, 167, 118, 79, 57);
				forecastX[i] = offset;
				forecastIndex[i] = top;
				offset += 79;
				top--;
			}
		}
			// displays option of using forecast
		if (forecast == true && useForecast == false){
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("Rearrange top 6 cards of Infection Draw Pile?", 130, 225);
			g.drawPixmap(GameAssets.yesButton, 140, 230);
			if (preventCancel == false) {
				g.drawPixmap(GameAssets.cancelButton, 320, 240);
			}
		}
		
		if (oneQuietNight == true && useOneQuietNight == false){
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("Skip Infection Stage next turn?", 130, 225);
			g.drawPixmap(GameAssets.yesButton, 140, 230);
			if (preventCancel == false) {
				g.drawPixmap(GameAssets.cancelButton, 320, 240);
			}
			
		}
		
		if (discoverCure == true && extraCards == true) {
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("You have more cards than required for discover cure.  Choose which you will keep", 5, 225);
			String discoverCureColor = cureColor();
			int offset = 3;
			int discIndex = 0;
			// if there wasn't a problem, go and display the cards
			if (discoverCureColor.compareTo("failed") != 0) {
				for (int i = 0; i < playerArray[playerTurnTracker].getCards().size(); i++) {
					// only display cards with the color being cured so the player can choose which cards they keep
					if (playerArray[playerTurnTracker].getCards().get(i).getColor().compareTo(discoverCureColor) == 0) {
						g.drawPixmap(playerArray[playerTurnTracker].getCards().get(i).getImage(), offset, 230, 0, 0, 121, 167, 68, 96);
						discoverX[discIndex] = offset;
						discoverIndex[discIndex] = i; 
						offset += 68;
						discIndex++;
					}
				}
			}
		}
		
		if (opsExpertMove == true) {
			if (chosenSacrifice == false) {
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				g.drawText("Select a card to sacrifice to move to any city if you're at a research station", 5, 330);
				int offset = 0;
				int cardIndex = 0;
				for (int i = 0; i < playerArray[playerTurnTracker].getCards().size(); i++) {
					// if the card isn't an event card, display it as an eligible card to sacrifice for the ops expert special move ability
					if (playerArray[playerTurnTracker].getCards().get(i).getType().compareTo("Event Card") != 0) {
						g.drawPixmap(playerArray[playerTurnTracker].getCards().get(i).getImage(), 3 + offset, 220, 0, 0, 121, 167, 64, 90);
						opsCardLoc[cardIndex] = i;
						opsCardCoord[cardIndex] = 3 + offset;
						offset += 64;
						cardIndex++;
					}
				}
			}
			else {
				for (int i = 0; i < allCities.length; i++){
					// show a travel ring for every city except the one that the ops. expert is in
					if (playerArray[playerTurnTracker].getCurrentCity().getName().compareTo(allCities[i].getName()) != 0) {
						g.drawPixmap(GameAssets.travelring, allCities[i].getXCoordinate() + offsetX + trackX - 17, allCities[i].getYCoordinate() + offsetY + trackY - 17);
					}
				}
				if (destCity.size() <= 48) {
					for (int i = 0; i < allCities.length; i++){
						if (playerArray[playerTurnTracker].getCurrentCity().getName().compareTo(allCities[i].getName()) != 0) {
							destX.push(allCities[i].getXCoordinate() + offsetX + trackX - 16);
							destY.push(allCities[i].getYCoordinate() + offsetY + trackY - 16);
							destCity.push(allCities[i]);
						}
					}	
				}
			}
		}
		
		if (contPlannerStealEventCard == true) {
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawPixmap(GameAssets.cancelVertical, 448, 211);
			// if the player doesn't have a stored event card, go steal one from the discard deck
			if (playerArray[playerTurnTracker].checkForStoredContingencyPlannerCard() == false) {
				int offset = 0;
				int cardIndex = 0;
				for (int i = 0; i < playerDiscard.getStackOfCards().size(); i++) {
					// only show event cards in the discard pile
					if (playerDiscard.getStackOfCards().elementAt(i).getType().compareTo("Event Card") == 0) {
						g.drawPixmap(playerDiscard.getStackOfCards().elementAt(i).getImage(), 3 + offset, 213, 0, 0, 244, 339, 76, 105);
						plannerCardLoc[cardIndex] = i;
						plannerCardCoord[cardIndex] = 3 + offset;
						offset += 76;
						cardIndex++;
					}
				}
			}
		}
		
		if (dispatcherMove == true) {
			// if a choice hasn't been made, display these images to inform player of their options
			if (dispatcherJoin == false && dispatcherControl == false) {
				g.drawPixmap(GameAssets.dispatcherPawnJoin, 0, 240);
		        g.drawPixmap(GameAssets.dispatcherPawnControl, 160, 240);
				g.drawPixmap(GameAssets.cancelButton, 320, 240);
			}
			else { // the player has decided join or control
				// choose a pawn to move
				if (pawnChosen == false) {
					g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
					g.drawPixmap(GameAssets.cancelButton, 320, 240);
					g.drawText("Choose which Player to move: ", 110, 225);
					int offset = 60;
					for (int i = 0; i < playerArray.length; i++) {
						g.drawPixmap(playerArray[i].getPawnImage(), offset, 250); // show image of all pawns
						dispatcherJoinDest[i] = playerArray[i].getCurrentCity().getName(); // store name of all possible destinations
						chosenPawnCoord[i] = offset;
						chosenPawnLoc[i] = i;
						if (i == playerTurnTracker) {
							g.drawPixmap(GameAssets.currentPlayer, offset - 2, 248);
						}
						offset += 60;
					}
				}
				else {
					// after choosing a pawn, select a valid destination for the pawn to move to
					if (dispatcherJoin == true) {
						for (int i = 0; i < playerArray.length; i++){
							// show a travel ring for cities that the pawn can go to that isn't their own city
							if (playerArray[chosenPawnIndex].getCurrentCity().getName().compareTo(dispatcherJoinDest[i]) != 0) {
								g.drawPixmap(GameAssets.travelring, worldCities.findCity(dispatcherJoinDest[i]).getXCoordinate() + offsetX + trackX - 17, worldCities.findCity(dispatcherJoinDest[i]).getYCoordinate() + offsetY + trackY - 17);
								joinX[i] = worldCities.findCity(dispatcherJoinDest[i]).getXCoordinate() + offsetX + trackX - 16;
								joinY[i] = worldCities.findCity(dispatcherJoinDest[i]).getYCoordinate() + offsetY + trackY - 16;
								joinCity[i] = worldCities.findCity(dispatcherJoinDest[i]);
							}
						}
						g.drawPixmap(GameAssets.cancelButton, 320, 240);
					}
					// after choosing a pawn to control, a new menu will pop up to choose what movements to inflict on the pawn being controlled
					if (dispatcherControl == true) {
						if (dispatchDriving == false && dispatchCharter == false && dispatchDirect == false && dispatchShuttle == false) {
							g.drawPixmap(GameAssets.dispatcherControlPanel, 0, 211);
							g.drawPixmap(GameAssets.dispatcherControlCancel, 320, 240);
						}
						if (dispatchDriving == true) {
							String [] driveFerryDest = playerArray[chosenPawnIndex].getCurrentCity().getConnections();
							for (int i = 0; i < driveFerryDest.length; i++){
								g.drawPixmap(GameAssets.travelring, worldCities.findCity(driveFerryDest[i]).getXCoordinate() + offsetX + trackX - 17, worldCities.findCity(driveFerryDest[i]).getYCoordinate() + offsetY + trackY - 17);
								controlX[i] = worldCities.findCity(driveFerryDest[i]).getXCoordinate() + offsetX + trackX - 16;
								controlY[i] = worldCities.findCity(driveFerryDest[i]).getYCoordinate() + offsetY + trackY - 16;
								controlCity[i] = worldCities.findCity(driveFerryDest[i]);
							}
							g.drawPixmap(GameAssets.cancelButton, 320, 240);
						}
						if (dispatchCharter == true) {
							// this function is based on a controlled pawn using a charter flight, which uses cards from the dispatcher's hand, not the controlled pawn's hand
							if (allCities == null) {
								allCities = worldCities.getWorldCities();
							}
							for (int i = 0; i < allCities.length; i++){
								if (allCities[i].getName().compareTo(playerArray[chosenPawnIndex].getCurrentCity().getName()) != 0) {
									g.drawPixmap(GameAssets.travelring, allCities[i].getXCoordinate() + offsetX + trackX - 17, allCities[i].getYCoordinate() + offsetY + trackY - 17);
									controlX[i] = allCities[i].getXCoordinate() + offsetX + trackX - 16;
									controlY[i] = allCities[i].getYCoordinate() + offsetY + trackY - 16;
									controlCity[i] = allCities[i];
								}
							}
							g.drawPixmap(GameAssets.cancelButton, 320, 240);
						}
						if (dispatchDirect == true) {
							City temp;
							// this function is based on a controlled pawn using a direct flight, which uses cards from the dispatcher's hand, not the controlled pawn's hand
							for (int i = 0 ; i < playerArray[playerTurnTracker].getCards().size(); i++) {
								if (playerArray[playerTurnTracker].getCards().get(i).getType().compareTo("Player Card") == 0){
									if (playerArray[playerTurnTracker].getCards().get(i).getColor() != "N/A"){
										temp = worldCities.findCity(playerArray[playerTurnTracker].getCards().get(i).getFormalName());
										g.drawPixmap(GameAssets.travelring, temp.getXCoordinate() + offsetX + trackX - 17, temp.getYCoordinate() + offsetY + trackY - 17);
										controlX[i] = temp.getXCoordinate() + offsetX + trackX - 16;
										controlY[i] = temp.getYCoordinate() + offsetY + trackY - 16;
										controlCity[i] = temp;
									}
								}
							}
							g.drawPixmap(GameAssets.cancelButton, 320, 240);
						}
						if (dispatchShuttle == true) {
							int j = 0;
							while (j < worldCities.getNumberOfResearchStations()) {
								researchCities[j] = worldCities.getResearchStations().elementAt(j);
								j++;
							}
							for (int i = 0; i < worldCities.getNumberOfResearchStations(); i++){
								// show a travel ring for locations unless the controlled pawn is at a research station already
								if (researchCities[i].getName().compareTo(playerArray[chosenPawnIndex].getCurrentCity().getName()) != 0) {
									g.drawPixmap(GameAssets.travelring, researchCities[i].getXCoordinate() + offsetX + trackX - 17, researchCities[i].getYCoordinate() + offsetY + trackY - 17);
								}
								controlX[i] = researchCities[i].getXCoordinate() + offsetX + trackX - 16;
								controlY[i] = researchCities[i].getYCoordinate() + offsetY + trackY - 16;
								controlCity[i] = researchCities[i];
							}
							g.drawPixmap(GameAssets.cancelButton, 320, 240);
						}
					}
				}
			}
		}
		
		if (shareKnowledge == true) {
			playersInSameCity = 0;
			if (sharingDecisionMade == false) {
				g.drawPixmap(GameAssets.giveCardButton, 0, 240);
				g.drawPixmap(GameAssets.takeCardButton, 160, 240);
				g.drawPixmap(GameAssets.cancelButton, 320, 240);
			}
			else {
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				int offset = 0;
				int currentPos = 0;
				int researchTakePos = 0;
				int researchGivePos = 0;
				int researchOffset = 59;
				for (int i = 0; i < playerArray.length; i++) {
					// if the player isn't self, and if the other player is in the same city, display their pawn
					if (playerArray[playerTurnTracker].getCurrentCity().getName().compareTo(playerArray[i].getCurrentCity().getName()) == 0) {
						if (giving == true) {  // if you're giving a card away, pick the player who is getting the card
							// find out where in the player's hand the card is that they're giving away.  Add an exception here for researcher, they can give away any card
							for (int k = 0; k < playerArray[playerTurnTracker].getTotalCards(); k++) {
								if (playerArray[playerTurnTracker].getRole() == Roles.RESEARCHER && i == 0) {
									// if the card isnt' an event card, store their location in an array
									if (playerArray[playerTurnTracker].getCards().get(k).getType().compareTo("Event Card") != 0) {
										researcherCardLoc[researchGivePos] = k;
										researchGivePos++;
									}
								}
								else {
									if (playerArray[playerTurnTracker].getCards().get(k).getFormalName().compareTo(playerArray[playerTurnTracker].getCurrentCity().getName()) == 0  && playerArray[playerTurnTracker].getRole() != Roles.RESEARCHER) {
									chosenCardPos = k;
									chosenLately = true;
									}
								}
							}
							// displays the researchers cards to choose which one to give before deciding on who to give said card to
							if (researchGive == true) {
								int offsetGive = 0;
								// display the cards.  # of cards revealed by the counter researchGivePos.
								for (int n = 0; n < researchGivePos; n++) {
									g.drawPixmap(playerArray[playerTurnTracker].getCards().get(researcherCardLoc[n]).getImage(), 3 + offsetGive, 220, 0, 0, 121, 167, 64, 90);
									shareX[n] = 3 + offsetGive;
									shareY[n] = researcherCardLoc[n];
									offsetGive += 64;
								}
							}
							// if the player isn't a researcher, then first show the pawns who can receive a card.  Otherwise they show up later for the researcher
							else {
								// don't print your own pawns, just other pawns
								if (playerArray[i].getRole() != playerArray[playerTurnTracker].getRole()) {
									g.drawPixmap(playerArray[i].getPawnImage(), 20 + offset, 225);
									g.drawPixmap(GameAssets.playerCardBack, 55 + offset, 218, 0, 0, 119, 168, 21, 30);
									offset += 85;
									giveCardPos[currentPos] = i;  // reveals index of each player who is in the same city
									currentPos++;
									playersInSameCity++;
									if (revealCards == true) {
										// maybe add ability to see other player's cards?
									}
								}
							}
						}
						else {  // if you're taking a card, you can only take from the person who actually has a card
							for (int j = 0; j < playerArray[i].getTotalCards(); j++) {
								// if the player is a researcher, show all their non-event cards, but don't show the researchers cards
								if (playerArray[i].getRole() == Roles.RESEARCHER && playerArray[playerTurnTracker].getRole() != playerArray[i].getRole()) {
									if (playerArray[i].findCard(j).getType().compareTo("Event Card") != 0) {
										g.drawPixmap(playerArray[i].findCard(j).getImage(), 2 + researchOffset, 220, 0, 0, 121, 167, 59, 83);
										researcherIndex = i;
										researcherTakeLoc[researchTakePos] = j;
										shareZ[researchTakePos] = 2 + researchOffset;
										researchTakePos++;
										researchOffset += 59;
										researcher = true;
									}
								}
								// if the player who is having their card taken away, show their eligible cards.
								else {
									// don't show your own cards, show other people's cards, but not researchers, their cards are up above already
									if (playerArray[i].getRole() != playerArray[playerTurnTracker].getRole()) {
										if (playerArray[i].findCard(j).getFormalName().compareTo(playerArray[i].getCurrentCity().getName()) == 0) {
											g.drawPixmap(playerArray[i].findCard(j).getImage(), 2, 220, 0, 0, 121, 167, 59, 83);
											chosenShareKnowledge = i;
											chosenCardPos = j;
											// I needed a way to find out if anyone else but a researcher is offering cards for the touch listener
											nonResearcher = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (dump == true) {
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawText("You are playing the " + eventPlayed + " event card.  Tap to continue.", 20, 250);
		}
		
		if (useEventCard == true && dump == false) {
			boolean planner = false;  // assume no contingency planner
			g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
			g.drawPixmap(GameAssets.cancelButton, 320, 240);
			int yoursCounter = 0;
			int currentSize = 0;
			for (int i = 0; i < playerArray.length; i++) {
				for (int j = 0; j < playerArray[i].getTotalCards(); j++) {
					// if the player has an event card, mark which player index has it
					if (playerArray[i].getCards().get(j).getType().compareTo("Event Card") == 0) {
						eventCardOwners[currentSize] = i;  // stores which player has event cards
						eventCardLoc[currentSize] = j;			   // stores where the event cards are in the player's hand
						currentSize++;
						if (i == playerTurnTracker) {
							yoursCounter++;
						}
					}
				}
				// if there's a planner, and they have a card stored, put it up for grabs as an eligible event card
				if (playerArray[i].getRole() == Roles.PLANNER && playerArray[i].checkForStoredContingencyPlannerCard() == true) {
					if (i == playerTurnTracker) {
						yoursCounter++;
					}
					eventCardOwners[currentSize] = i;  // stores where the contingency planner is in the player array
					eventCardLoc[currentSize] = -15000;  // an arbitrary negative number unlikely to appear to designate when you hit the planner's card
					currentSize++;  // increment the current size
					planner = true;
				}
			}
			if (currentSize > 0) {
				eventCardsAvailable = currentSize;
				int offset = 0;
				int othersOffset = 10;
				for (int k = 0; k < currentSize; k++) {
					// separate your event cards from others.  Ask permission to play other event cards
					if (eventCardOwners[k] == playerTurnTracker) {
						g.drawText("Your cards:", 5, 225);
						// if there's a planner and they have a card, print it separately since it is not part of their hand
						if (planner == true && playerArray[eventCardOwners[k]].checkForStoredContingencyPlannerCard() == true && playerArray[eventCardOwners[k]].getRole() == Roles.PLANNER && eventCardLoc[k] == -15000) {
							g.drawPixmap(playerArray[eventCardOwners[k]].getContingencyPlannerEventSpecial().getImage(), 10 + offset, 230, 0, 0, 244, 339, 56, 80);
						}
						else {
							if (eventCardLoc[k] != -15000) {
								g.drawPixmap(playerArray[eventCardOwners[k]].getCards().get(eventCardLoc[k]).getImage(), 10 + offset, 230, 0, 0, 244, 339, 56, 80);
							}
						}
						offset += 60;
					}
					else {
						g.drawText("Other player's event cards:", 10 + (60 * yoursCounter), 225);
						if (planner == true && playerArray[eventCardOwners[k]].checkForStoredContingencyPlannerCard() == true && eventCardLoc[k] == -15000) {
							g.drawPixmap(playerArray[eventCardOwners[k]].getContingencyPlannerEventSpecial().getImage(), othersOffset + (60 * yoursCounter), 230, 0, 0, 244, 339, 56, 80);
						}
						else {
							if (eventCardLoc[k] != -15000) {
								g.drawPixmap(playerArray[eventCardOwners[k]].getCards().get(eventCardLoc[k]).getImage(), othersOffset + (60 * yoursCounter), 230, 0, 0, 244, 339, 56, 80);
							}
						}
						othersOffset += 60;
					}
				}
			}
		}
		
		
		// START OF TABS
		//Code for each tabs
		if (actions == false && drawMap_assetToggle == 1 && tabSwitcher < 4) {
			switch(tabSwitcher) {
			//home tab
			case 1:
				g.drawText("You are", 8, 270);
				g.drawText(playerArray[playerTurnTracker].getName(), 8, 282);
				g.drawText("Your abilities:", 135, 270);
				g.drawText(playerArray[playerTurnTracker].getAbilities1(), 135, 282);
				g.drawText(playerArray[playerTurnTracker].getAbilities2(), 135, 294);
				g.drawText(playerArray[playerTurnTracker].getAbilities3(), 135, 306);
				if (playerArray[playerTurnTracker].getRole() == Roles.OPERATIONS_EXPERT ||
					playerArray[playerTurnTracker].getRole() ==	 Roles.QUARANTINE_SPECIALIST || 
					playerArray[playerTurnTracker].getRole() == Roles.PLANNER) {
					//String [] nameSplit = playerArray[playerTurnTracker].getRoleName().split(" ");
					g.drawText(playerArray[playerTurnTracker].getRoleName(), 63, 302);
					g.drawText(playerArray[playerTurnTracker].getRoleName1(), 63, 314);
				}
				else {
					g.drawText(playerArray[playerTurnTracker].getRoleName(), 63, 305);
				}
				g.drawPixmap(playerArray[playerTurnTracker].getHeadPhoto(), 65, 255);
				break;
			//cubes tab
			case 2:
				g.drawText(String.valueOf(blackCubes.getCubeDeck().size()), 18, 305);
				g.drawPixmap(GameAssets.bigBlackCube, 10, 260);
				g.drawText(String.valueOf(blueCubes.getCubeDeck().size()), 78, 305);
				g.drawPixmap(GameAssets.bigBlueCube, 70, 260);
				g.drawText(String.valueOf(redCubes.getCubeDeck().size()), 138, 305);
				g.drawPixmap(GameAssets.bigRedCube, 130, 260);
				g.drawText(String.valueOf(yellowCubes.getCubeDeck().size()), 198, 305);
				g.drawPixmap(GameAssets.bigYellowCube, 190, 260);
				
				g.drawText("Cures Discovered", 265, 275);
				g.drawPixmap(GameAssets.frameBlack, 255, 280);
				if (curesDiscovered.getBlackCure() == true){
					g.drawPixmap(GameAssets.cureBlack, 257, 282);
				}
				g.drawPixmap(GameAssets.frameBlue, 285, 280);
				if (curesDiscovered.getBlueCure() == true){
					g.drawPixmap(GameAssets.cureBlue, 287, 282);
				}
				g.drawPixmap(GameAssets.frameRed, 315, 280);
				if (curesDiscovered.getRedCure() == true){
					g.drawPixmap(GameAssets.cureRed, 317, 282);
				}
				g.drawPixmap(GameAssets.frameYellow, 345, 280);
				if (curesDiscovered.getYellowCure() == true){
					g.drawPixmap(GameAssets.cureYellow, 347, 282);
				}
				break;
			//rates status tab
			case 3:
				g.drawText("Infection Rate: ", 8, 275);
				int[] rate = {2,2,2,3,3,4,4};
				for (int i=0; i<7; i++){
					g.drawText(String.valueOf(rate[i]), 80+(i+1)*30, 275);
				}
				g.drawPixmap(GameAssets.gameMap_statsunderline, 75 + (infectRate.getRateMarker()+1)*30, 276);
				
				g.drawText("Outbreak Level: ", 10, 300);
				for (int i=0; i<8; i++){
					g.drawText(String.valueOf(i), 80+(i+1)*30, 300);
				}
				g.drawPixmap(GameAssets.gameMap_skullhead, 345, 282);
				g.drawPixmap(GameAssets.gameMap_statsunderline, 75 + (outbreaks.getOutbreakLevel()+1)*30, 301);
				break;
			//card tab
			case 4:
				// handled below, since the implementation is more sophisticated
				break;
			}
		}
		if (actions == false && drawMap_assetToggle == 1 && tabSwitcher == 4) {
			if (cardInfoTabSwitcher == 0){
				int offset = 10;
				//String [] splited;
				g.drawPixmap(GameAssets.actionBackdrop, 0, 210);
				g.drawText("Discard Piles", 335, 230);
				g.drawPixmap(GameAssets.playerCardBack, 300, 240, 0, 0, 120, 169, 44, 60);
				g.drawText("Player", 300, 310);
				g.drawPixmap(GameAssets.infectionCardBack, 365, 255, 0, 0, 167, 121, 60, 44);
				g.drawText("Infection", 370, 310);
				g.drawPixmap(GameAssets.cancelVertical, 448, 210);
				for (int j = 0; j < playerArray.length; j++){
					if (playerTurnTracker != j) {
						//splited = playerArray[j].getRoleName().split("\\s+");
						g.drawText(playerArray[j].getName(), offset, 235);
						g.drawPixmap(playerArray[j].getHeadPhoto(), offset, 245, 0, 0, 35, 35, 35, 35);
						if (playerArray[j].getRole() == Roles.OPERATIONS_EXPERT ||
								playerArray[j].getRole() ==	 Roles.QUARANTINE_SPECIALIST || 
								playerArray[j].getRole() == Roles.PLANNER) {
								//String [] nameSplit = playerArray[playerTurnTracker].getRoleName().split(" ");
								g.drawText(playerArray[j].getRoleName(), offset, 295);
								g.drawText(playerArray[j].getRoleName1(), offset, 307);
						}
						else {
							g.drawText(playerArray[j].getRoleName(), offset, 295);
						}
						/*
						for (int i=0; i<splited.length; i++){
							g.drawText(splited[i], offset, 295+i*12);
						}
						*/
						offset = offset + 75;
					}
				}
			}
				// player one stats
			if (cardInfoTabSwitcher == 1) {
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				for (int i = 0; i < playerArray[(playerTurnTracker + 1) % playerArray.length].getCardList().size(); i++){
					if (playerArray[(playerTurnTracker + 1) % playerArray.length].getCards().get(i).getType().compareTo("Player Card") == 0) {
						g.drawPixmap(playerArray[(playerTurnTracker + 1) % playerArray.length].getCardList().get(i).getImage(), 5 + (i*63), 220, 0, 0, 121, 167, 59, 83);
					}
					else {
						g.drawPixmap(playerArray[(playerTurnTracker + 1) % playerArray.length].getCardList().get(i).getImage(), 5 + (i*63), 220, 0, 0, 244, 339, 59, 83);
					}
				}
				onlyThis = true;
			}
				// player two stats
			if (cardInfoTabSwitcher == 2 && playerArray.length > 2) {
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				for (int i = 0; i < playerArray[(playerTurnTracker + 2) % playerArray.length].getCardList().size(); i++){
					if (playerArray[(playerTurnTracker + 2) % playerArray.length].getCards().get(i).getType().compareTo("Player Card") == 0) {
						g.drawPixmap(playerArray[(playerTurnTracker + 2) % playerArray.length].getCardList().get(i).getImage(), 5 + (i*63), 220, 0, 0, 121, 167, 59, 83);
					}
					else {
						g.drawPixmap(playerArray[(playerTurnTracker + 2) % playerArray.length].getCardList().get(i).getImage(), 5 + (i*63), 220, 0, 0, 244, 339, 59, 83);
					}
				}
				onlyThis = true;
			}
				// player three stats
			if ((cardInfoTabSwitcher == 3) && playerArray.length > 3) {
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				for (int i = 0; i < playerArray[(playerTurnTracker + 3) % playerArray.length].getCardList().size(); i++){
					if (playerArray[(playerTurnTracker + 3) % playerArray.length].getCards().get(i).getType().compareTo("Player Card") == 0) {
						g.drawPixmap(playerArray[(playerTurnTracker + 3) % playerArray.length].getCardList().get(i).getImage(), 5 + (i*63), 220, 0, 0, 121, 167, 59, 83);
					}
					else {
						g.drawPixmap(playerArray[(playerTurnTracker + 3) % playerArray.length].getCardList().get(i).getImage(), 5 + (i*63), 220, 0, 0, 244, 339, 59, 83);
					}
				}
				onlyThis = true;
			}
				// player discard pile
			if (cardInfoTabSwitcher == 4){
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				if (playerDiscard.getStackOfCards().size() > 0){
					int offset = 35;
					int displayMax = 5;
					int pageCount = (playerDiscard.getStackOfCards().size() / 5) - 1;
					int remainder = playerDiscard.getStackOfCards().size() % 5;
					if (cardPageView >=  (pageCount + 1) && remainder > 0) {
						displayMax = remainder;
					}
					if (remainder > 0) {
						pageCount++;
					}
					for (int i = cardPageView * 5; i < (cardPageView * 5) + displayMax; i++) {
						if (cardPageView > 0) {
							g.drawPixmap(GameAssets.actionArrowLeft, 0, 211);
						}
						if (cardPageView < pageCount) {
							g.drawPixmap(GameAssets.actionArrowRight, 416, 211);
						}
						if (playerDiscard.getStackOfCards().get(i).getType().compareTo("Event Card") == 0){
							g.drawPixmap(playerDiscard.getStackOfCards().get(i).getImage(), offset, 220, 0, 0, 244, 339, 68, 95);
						}
						else if (playerDiscard.getStackOfCards().get(i).getType().compareTo("Epidemic Card") == 0){
							g.drawPixmap(playerDiscard.getStackOfCards().get(i).getImage(), offset, 220, 0, 0, 244, 339, 68, 95);
						}
						else{
							g.drawPixmap(playerDiscard.getStackOfCards().get(i).getImage(), offset, 220, 0, 0, 119, 168, 68, 95);
						}
						offset += 75;
					}
				}
			}
				// infection discard pile
			if (cardInfoTabSwitcher == 5){
				g.drawPixmap(GameAssets.actionBackdrop, 0, 211);
				g.drawPixmap(GameAssets.cancelVertical, 448, 211);
				int offset = 35;
				int displayMax = 4;
				int pageCount = (infectionDiscard.getStackOfCards().size() / 4) - 1;
				int remainder = infectionDiscard.getStackOfCards().size() % 4;
				if (cardPageView >=  (pageCount + 1) && remainder > 0) {
					displayMax = remainder;
				}
				if (remainder > 0) {
					pageCount++;
				}
				for (int i = cardPageView * 4; i < (cardPageView * 4) + displayMax; i++) {
					if (cardPageView > 0) {
						g.drawPixmap(GameAssets.actionArrowLeft, 0, 211);
					}
					if (cardPageView < pageCount) {
						g.drawPixmap(GameAssets.actionArrowRight, 416, 211);
					}
					g.drawPixmap(infectionDiscard.getStackOfCards().get(i).getImage(), offset, 240, 0, 0, 167, 118, 95, 68);
					offset += 95;
				}
			}
			
		}
		
		//g.drawPixmap(GameAssets.scroll_Yaxis, 0, 0);
		//g.drawPixmap(GameAssets.scroll_Xaxis, 30, 0);
		//g.drawPixmap(GameAssets.scroll_Ball, 0, trackBall_Yaxis);
		//g.drawPixmap(GameAssets.scroll_Ball, trackBall_Xaxis, 0);
		
		if (actions == true && actionInProgress != true) {
			g.drawPixmap(GameAssets.actionbar, 0, 211 /*249*/);  // Brings up the action bar
			//g.drawSVG(GameAssets.svgAction, 0, 0);
			//g.drawSVG(GameAssets.svgAction1);
		}
		
		if (tempPlayer_moveToggle == true) {
			moveMap_toPosition (newPlayerX, newPlayerY);
		}
		
		//end of present
	}
	public int getPlayer_midPointX (int _PlayerX) {
		int newX = 0;
		if (trackX < _PlayerX) {
			newX = _PlayerX - moveBy_midPointX;
		}
		else if (trackX > _PlayerX) {
			newX = _PlayerX + moveBy_midPointX;
		}
		return newX;
	}
	public int getPlayer_midPointY (int _PlayerY) {
		int newY = 0;
		if (trackY < _PlayerY) {
			newY = _PlayerY - moveBy_midPointY;
		}
		else if (trackY > _PlayerY) {
			newY = _PlayerY + moveBy_midPointY;
		}
		return newY;
	}
		// to move the map according to player position
	// adding is left for X, up for Y
	// subtracting is right for x, down for Y
	public void moveMap_toPosition (int _PlayerX,int _PlayerY) {
		//Log.d("Distance of X", Integer.toString((trackX-_PlayerX)));
		//Log.d("Distance of Y", Integer.toString((trackY-_PlayerY)));
		if (trackX != _PlayerX) {
			// keeps the auto-scroll from going too far
			if (trackX >= -122 && _PlayerX >= -122) {
				trackX = -122;
				_PlayerX = -122;
			}
			// keeps the auto-scroll from going too far
			if (trackX <= -833 && _PlayerX <= -833) {
				trackX = -833;
				_PlayerX = -833;
			}
			if (trackX < _PlayerX) {
				if ((trackX-_PlayerX) >= -1500 && (trackX-_PlayerX) < -300) {
					trackX += 12;
				}
				else if ((trackX-_PlayerX) >= -300 && (trackX-_PlayerX) < -200) {
					trackX += 10;
				}
				else if ((trackX-_PlayerX) >= -200 && (trackX-_PlayerX) < -100) {
					trackX += 8;
				}
				else if ((trackX-_PlayerX) >= -100 && (trackX-_PlayerX) < -10) {
					trackX += 4;
				}
				else if ((trackX-_PlayerX) >= -10) {
					trackX += 1;
				}
			}
			else if (trackX > _PlayerX) {
				if ((trackX-_PlayerX) <= 1500 && (trackX-_PlayerX) > 300) {
					trackX -= 12;
				}
				else if ((trackX-_PlayerX) <= 300 && (trackX-_PlayerX) > 200) {
					trackX -= 10;
				}
				else if ((trackX-_PlayerX) <= 200 && (trackX-_PlayerX) > 100) {
					trackX -= 8;
				}
				else if ((trackX-_PlayerX) <= 100 && (trackX-_PlayerX) > 10) {
					trackX -= 4;
				}
				else if ((trackX-_PlayerX) <= 10) {
					trackX -= 1;
				}
			}
		}
		if (trackY != _PlayerY) {
			// keeps the auto-scroll from going too far
			if (trackY >= -35 && _PlayerY >= -35) {
				trackY = -35;
				_PlayerY = -35;
			}
			// keeps the auto-scroll from going too far
			if (trackY <= -562 && _PlayerY <= -562) {
				trackY = -562;
				_PlayerY = -562;
			}
			if (trackY < _PlayerY) {
				if ((trackY-_PlayerY) >= -1500 && (trackY-_PlayerY) < -300) {
					trackY += 12;
				}
				else if ((trackY-_PlayerY) >= -300 && (trackY-_PlayerY) < -200) {
					trackY += 10;
				}
				else if ((trackY-_PlayerY) >= -200 && (trackY-_PlayerY) < -100) {
					trackY += 8;
				}
				else if ((trackY-_PlayerY) >= -100 && (trackY-_PlayerY) < -10) {
					trackY += 4;
				}
				else if ((trackY-_PlayerY) >= -10) {
					trackY += 1;
				}
			}
			else if (trackY > _PlayerY) {
				if ((trackY-_PlayerY) <= 1500 && (trackY-_PlayerY) > 300) {
					trackY -= 12;
				}
				else if ((trackY-_PlayerY) <= 300 && (trackY-_PlayerY) > 200) {
					trackY -= 10;
				}
				else if ((trackY-_PlayerY) <= 200 && (trackY-_PlayerY) > 100) {
					trackY -= 8;
				}
				else if ((trackY-_PlayerY) <= 100 && (trackY-_PlayerY) > 10) {
					trackY -= 4;
				}
				else if ((trackY-_PlayerY) <= 10) {
					trackY -= 1;
				}
			}
		}
		if (_PlayerY == trackY && _PlayerX == trackX) {
			tempPlayer_moveToggle = false;
		}
	}
		
	public int moveScroll_withPlayerX (int mapCurrent_X) {
		float temp = (float)mapCurrent_X / (float)scrollX_limitHigh;
		int tempX = (int) (temp*trackBall_XaxisHigh);
		//Log.d("tempX Value", Integer.toString(tempX));
		if (tempX < trackBall_XaxisLow) {
			tempX = trackBall_XaxisLow;
		}
		if (tempX > trackBall_XaxisHigh) {
			tempX = trackBall_XaxisHigh;
		}
		return tempX;
	}

	public int moveScroll_withPlayerY (int mapCurrent_Y) {
		float temp = (float)mapCurrent_Y / (float)scrollY_limitHigh;
		int tempY = (int) (temp*trackBall_YaxisHigh);
		//Log.d("tempY Value", Integer.toString(tempY));
		if (tempY < trackBall_YaxisLow) {
			tempY = trackBall_YaxisLow;
		}
		if (tempY > trackBall_YaxisHigh) {
			tempY = trackBall_YaxisHigh;
		}
		return tempY;
	}
	
	
	//calculate the percentage to move map on x axis
	public int getXaxis_Percentage (int xCurrent) {
		float temp = (float)xCurrent/ (float)trackBall_XaxisHigh;
		//Log.d("Track X map", Float.toString(temp));
		int tempX = ((int) (temp*(float)scrollX_limitHigh));
		if (tempX >= 0) {
			return 0;
		}
		else {
			return tempX;
		}
	}
		//calculate the percentage to move map on y-axis
	public int getYaxis_Percentage (int yCurrent) {
		float temp = (float) yCurrent / (float) trackBall_YaxisHigh;
		//Log.d("Track X map", Float.toString(temp));
		int tempY = ((int)(temp*(float)scrollY_limitHigh));
		if (tempY >= 0) {
			return 0;
		}
		else {
			return tempY;
		}
	}
	
	public int colorCardCounter(String color) {
		int yellowCount = 0;
        int blackCount = 0;
        int blueCount = 0;
        int redCount = 0;
		if (color.compareTo("black") == 0) {
			for (int i = 0; i < playerArray[playerTurnTracker].getCards().size(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getColor().compareTo(color) == 0) {
					blackCount++;
				}
			}
			return blackCount;
		}
		if (color.compareTo("blue") == 0) {
			for (int i = 0; i < playerArray[playerTurnTracker].getCards().size(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getColor().compareTo(color) == 0) {
					blueCount++;
				}
			}
			return blueCount;
		}
		if (color.compareTo("red") == 0) {
			for (int i = 0; i < playerArray[playerTurnTracker].getCards().size(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getColor().compareTo(color) == 0) {
					redCount++;
				}
			}
			return redCount;
		}
		if (color.compareTo("yellow") == 0) {
			for (int i = 0; i < playerArray[playerTurnTracker].getCards().size(); i++) {
				if (playerArray[playerTurnTracker].getCards().get(i).getColor().compareTo(color) == 0) {
					yellowCount++;
				}
			}
			return yellowCount;
		}
		return -1;  // return -1 if it failed somehow
	}
	
	public String cureColor() {
		int yellowCount = colorCardCounter("yellow");
        int blackCount = colorCardCounter("black");
        int blueCount = colorCardCounter("blue");
        int redCount = colorCardCounter("red");
        if (playerArray[playerTurnTracker].getRole() == Roles.SCIENTIST) {
        	if (yellowCount >= 4) {
        		return "yellow";
        	}
        	if (blackCount >= 4) {
        		return "black";
        	}
        	if (blueCount >= 4) {
        		return "blue";
        	}
        	if (redCount >= 4) {
        		return "red";
        	}
        }
        else {
        	if (yellowCount >= 5) {
        		return "yellow";
        	}
        	if (blackCount >= 5) {
        		return "black";
        	}
        	if (blueCount >= 5) {
        		return "blue";
        	}
        	if (redCount >= 5) {
        		return "red";
        	}
        }
        return "failed";
	}
			
	@Override
	public void pause() {
		GameAssets.gameMap_BGM.pause();
	}

	@Override
	public void resume() {
		GameAssets.gameMap_BGM.play();
	}

	@Override
	public void dispose() {
		GameAssets.gameMap_BGM.dispose();
		GameAssets.mainMenu_Click.dispose();
    	System.exit(0);			
	}
}
