package gamePlayer_Controller;

import gameBoard_Master.City;
import gameBoard_Master.CuresDiscoveredController;
import gameCube_Master.CubeModel;
import gameDeck_Master.Card;
import gameDeck_Master.DeckController;
import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GamePixmap_interfaceView;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameGraphic_interfaceView.PixmapFormat;

import java.util.LinkedList;

public class Player {

    //   public final int MAX_ACTION = 4;
    private String name = ""; //player name
    private int xCord, yCord; //current location
    private LinkedList<Card> cards;
    private Roles currentRole; // = Roles.getRandomRole();  roles currently chosen in GameMap.java using random, non-repeating function.
    private City currentCity;
    // the card below is special and only for the contingency planner.  An event card stored here is never counted against the player's hand
    private Card contingencyPlannerEventSpecial;
    private GamePixmap_interfaceView headPhoto;
    private String roleName;
    private String roleName1; // for players with longer role names
    private GamePixmap_interfaceView pawnImage;
    private GameGraphic_interfaceView gameGraphic;
    private String abilities1;
    private String abilities2;
    private String abilities3;
    private int action;

    public Player(String name, int x, int y, int role, City atlanta, Game_interfaceView game) {
    	this.gameGraphic = game.getGraphics();
        roleAllocater(role);
        this.contingencyPlannerEventSpecial = null;
        this.currentCity = new City();
        this.currentCity = atlanta; // This is just for starting the game
        this.name = name;
        this.cards = new LinkedList<Card>();
        this.xCord = x;
        this.yCord = y;
        this.action = 0;
    }

    private void roleAllocater(int role) {
        switch (role) {
            case 0:
            	this.roleName = "Contingency";
            	this.roleName1 = "Planner";
                this.currentRole = Roles.PLANNER;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/ContingencyPlanner_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/contingencyplannerpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "SPECIAL: Take any Event Card from discard";
                this.abilities2 = "pile. Does not count against your hand";
                this.abilities3 = "limit. Must use before you can steal again.";
                break;
            case 1:
            	this.roleName = "Dispatcher";
                this.currentRole = Roles.DISPATCHER;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/Dispatcher_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/dispatcherpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "SPECIAL: Move any player to another player's";
                this.abilities2 = "location, including yourself. SPECIAL: Move";
                this.abilities3 = "any player as if it were your own.";
                break;
            case 2:
            	this.roleName = "Medic";
                this.currentRole = Roles.MEDIC;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/Medic_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/medicpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "Treat disease removes all cubes of a single";
                this.abilities2 = "color for 1 action.  Automatic removal of";
                this.abilities3 = "cubes of cured diseases at Medic's location";
                break;
            case 3:
            	this.roleName = "Operations";
            	this.roleName1 = "Expert";
                this.currentRole = Roles.OPERATIONS_EXPERT;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/OperationsExpert_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/opsexpertpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "SPECIAL: once per turn, move from research";
                this.abilities2 = "station to any city. Build a research";
                this.abilities3 = "station in current city for 1 action.";
                break;
            case 4:
            	this.roleName = "Quarantine";
            	this.roleName1 = "Specialist";
                this.currentRole = Roles.QUARANTINE_SPECIALIST;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/QuarantineSpecialist_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/quarantinespecialistpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "Prevents outbreaks and new cubes from";
                this.abilities2 = "being placed in your current city, and all";
                this.abilities3 = "cities connected to your current city.";
                break;
            case 5:
            	this.roleName = "Researcher";
                this.currentRole = Roles.RESEARCHER;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/Researcher_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/researcherpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "Give any City card from your hand to";
                this.abilities2 = "another player. Both giver and receiver";
                this.abilities3 = "must be in the same city.";
                break;
            case 6:
            	this.roleName = "Scientist";
                this.currentRole = Roles.SCIENTIST;
                this.headPhoto = gameGraphic.newPixmap("Role_Cards/Scientist_headphoto.png", PixmapFormat.ARGB4444);
                this.pawnImage = this.gameGraphic.newPixmap("Pawns/scientistpawn.png", PixmapFormat.ARGB4444);
                this.abilities1 = "Only needs 4 City cards of the same";
                this.abilities2 = "disease color in order to discover a";
                this.abilities3 = "cure for that disease.";
                break;
        }
    }
    
    public Roles getRole(){
    	return this.currentRole;
    }

    public LinkedList<Card> getCardList() {
        return this.cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setXCord(int x) {
    	this.setXCord(x);
    }

    public void setYCord(int y) {
        this.setYCord(y);
    }

    public int getXCord() {

        return xCord;
    }

    public int getYCord() {
        return yCord;
    }

    /**
    "Move to a city connected by a white line to the one you are in."
     */
    public boolean driveFerry(City toCity) {
    	
    	for(String city : currentCity.getConnections())
    		if (city.equalsIgnoreCase(toCity.getName())){
    			currentCity = toCity;
    			// card does not need to be consumed.  Uses 1 action.
    			 action++;  // no more infinite driving.
    			return true;
    		}
    	
    		// returned false if City not within driving/ferrying distance 
        return false;
    }

    /**
     * "Discard a city card to move to the city named on the card"
     */
    public boolean directFlight(City toCity) {
    	for (int i = 0; i < this.cards.size(); i++) {
    		// if the card is there, proceed with flight
    		if (this.cards.get(i).getFormalName().compareTo(toCity.getName()) == 0) {
    			// if card is not the current city player is in, proceed with flight
    			if (this.currentCity.getName().compareTo(toCity.getName()) != 0) {
    				this.currentCity = toCity;
    	        	// card does need to be consumed but doing it here will only leave it lost to the game
    	        	this.action++;
    	            return true;
    			}
    		}
    	}
        // if player does not have toCity card
        return false;
    }

    /**
    "Discard the City card matches the city you are in to move to any city"
     */
    public int charterFlight(City toCity) {
    	// if destination city and current city are the same, disallow charter flight.  Waste of a card.
		if (toCity.getName().compareTo(this.currentCity.getName()) != 0) {
			for (int i = 0; i < this.cards.size(); i++) {
    		
    			// if you have the card for the city, and it matches the city you are in, charter flight happens
    			if (this.cards.get(i).getFormalName().compareTo(this.currentCity.getName()) == 0){
        			this.currentCity = toCity;
        			// card does need to be consumed but doing it here will only leave it lost to the game
        			this.action++;
        			return i;
        		}
    		}
    		
    	}
        	// player can move to any city after removing card matches the current city
        return -1;
    }

    // used if the airlift event card is played
    public void airlift(City toCity){
    	currentCity = toCity;
    }
    
    /**
    "Move from a city with a research station to any other city that has a research station
     */
    public boolean shuttleFlight(City city, City [] researchCities) {

        	// player can move to city with research station
    	for (int i = 0; i < researchCities.length; i++) {
    		// if the current city has a research center, and the current city is not the destination city
    		if (currentCity.getResearchCenterStatus() == true && currentCity.getName().compareTo(city.getName()) != 0) {
    			// double checks to make sure the destination city does have a research station
    			if (city.getResearchCenterStatus() == true) {
    				currentCity = city;
                    // card does not need to be consumed.  Uses 1 action.
                    action++;
                    return true;
    			}
            }
    	}
        return false;
    }

    /*
        This method for dispatcher.
        "move any pawn, if its owner agrees, to any city containing another pawn".
     */
    
    public boolean movePlayerToACity(Player player, City toCity){
        if(currentRole != Roles.DISPATCHER)
            return false;

        //if(toCity.getPlayerStack().isEmpty())
        //   return false;
        player.setCurrentCity(toCity);
        action++; // takes an action, so increment dispatchers actions taken counter
        return true;
    }

    
    // dispatcher method.  A modified version of driveFerry
    public boolean dispatchDriveFerry(Player controlledPawn, City toCity){
    	// Note: "this" refers the implicit reference to the dispatcher 
    	
    	// only a dispatcher can call this function.
        if(this.currentRole != Roles.DISPATCHER) {
        	return false;
        }
            
        for(String city : controlledPawn.currentCity.getConnections()) {
        	if (city.equalsIgnoreCase(toCity.getName())){
    			controlledPawn.currentCity = toCity;
    			// card does not need to be consumed.  Uses 1 action.
    			this.action++;  // increments dispatcher's action counter, not the controlled pawn's action counter
    			return true;
    		}
        }
        
    	
    		// returned false if City not within driving/ferrying distance 
        return false;
    }

    // dispatcher method.  A modified version of directFlight
    public boolean dispatchDirect(Player controlledPawn, City toCity){
    	// Note: "this" refers the implicit reference to the dispatcher 
    	
    	// only a dispatcher can call this function.
    	if (this.currentRole != Roles.DISPATCHER) {
    		return false;
    	}
    	for (int i = 0; i < this.cards.size(); i++) {
    		// if the card is there in the dispatcher's hand, proceed with flight
    		if (this.cards.get(i).getFormalName().compareTo(toCity.getName()) == 0) {
    			// if card is not the current city that the controlled pawn is in, proceed with flight
    			if (controlledPawn.currentCity.getName().compareTo(toCity.getName()) != 0) {
    				controlledPawn.currentCity = toCity;
    	        	// card does need to be consumed but doing it here will only leave it lost to the game
    	        	this.action++; // increments dispatchers action count per game rules
    	            return true;
    			}
    		}
    	}
        // if player does not have toCity card
        return false;
    }
    
    // dispatcher method.  A modified version of charter flight
    public int dispatchCharter(Player controlledPawn, City toCity) {
    	// Note: "this" refers the implicit reference to the dispatcher 
    	
    	// only a dispatcher can call this function.
    	if (this.currentRole != Roles.DISPATCHER) {
    		return -1;
    	}
    	
    	// if destination city and current city are the same for the controlled pawn, then disallow charter flight.  Waste of a card.
    	if (toCity.getName().compareTo(controlledPawn.currentCity.getName()) != 0) {
    		for (int i = 0; i < this.cards.size(); i++) {
    	  		// if the dispatcher has the card for the city, and it matches the city the controlled pawn is in, charter flight happens
    	  		if (this.cards.get(i).getFormalName().compareTo(controlledPawn.currentCity.getName()) == 0){
    	        	controlledPawn.currentCity = toCity;
    	        	// card does need to be consumed but doing it here will only leave it lost to the game
    	        	this.action++;	// increments dispatchers action count per game rules
    	        	return i;
    	        }
    		}
    	}
    	// player can move to any city after removing the dispatcher's card that matches the current city
    	return -1;
    }
    
    // dispatcher method.  A modified version of shuttle flight
    public boolean dispatchShuttle(Player controlledPawn, City city, City [] researchCities) {
    	// Note: "this" refers the implicit reference to the dispatcher 
    	
    	// only a dispatcher can call this function.
    	if (this.currentRole != Roles.DISPATCHER) {
    		return false;
    	}
    	// player can move to city with research station
    	for (int i = 0; i < researchCities.length; i++) {
    		// if the current city occupied by the controlled pawn has a research center, and the current city is not the destination city
    		if (controlledPawn.currentCity.getResearchCenterStatus() == true && controlledPawn.currentCity.getName().compareTo(city.getName()) != 0) {
    			// double checks to make sure the destination city does have a research station
    			if (city.getResearchCenterStatus() == true) {
    				controlledPawn.currentCity = city;
                    // card does not need to be consumed.  Uses 1 action.
                    action++;	// increments dispatchers action count per game rules
                    return true;
    			}
            }
    	}
        return false;
    }


    public void addCard(Card aCard) {
        cards.add(aCard);
    }

    public void removeCard() {
        cards.removeLast();
        // action++;
    }

    public Card removeCard(City city) {
    	Card temp = null;
    	for (int i = 0; i < this.cards.size(); i++) {
    		if (this.cards.get(i).getFormalName().compareTo(city.getName()) == 0) {
    			temp = cards.remove(i);
    		}
    	}
    	return temp;
    }

    public Card removeCard(int index) {
        return this.cards.remove(index);

    }

    public Card findCard(int index) {
        return this.cards.get(index);
    }

    /**
     * Remove 1 disease cube from the current city
     * If current role is Medic, then remove all the cubes.
     */
    public CubeModel treatDisease(String color, CuresDiscoveredController curesDiscovered) {
    	// we want to return null if there are no cubes to cure.  I moved the medic role check into the below statements so that it 
    	// can not only benefit from the checking, but also know which cube is being removed and be able to give the cube back to supply
    	
    	if((color.compareTo("black") == 0) && (currentCity.getBlackCubeCount() > 0)){ 
    		// the cures discovered == true means basically if a cure is discovered, you're a quasi-medic as far as treating diseases go
    		if (currentCity.getBlackCubeCount() == 1 && (currentRole == Roles.MEDIC || curesDiscovered.getBlackCure() == true)) {
    			action++; // increment action by 1 for medic on the last cube removal.  It does use an action to treat uncured diseases, but this will only count it once
    		}
    		if (currentRole != Roles.MEDIC && curesDiscovered.getBlackCure() == false) {
    			action++;
    		}
    		return currentCity.removeCube(color);
    	}
    	else if((color.compareTo("blue") == 0) && (currentCity.getBlueCubeCount() > 0)){
    		if (currentCity.getBlueCubeCount() == 1 && (currentRole == Roles.MEDIC || curesDiscovered.getBlueCure() == true)) {
    			action++; // increment action by 1 for medic on the last cube removal.  It does use an action to treat uncured diseases, but this will only count it once
    		}
    		if (currentRole != Roles.MEDIC && curesDiscovered.getBlueCure() == false) {
    			action++;
    		}
    		return currentCity.removeCube(color);
    	}
    	else if((color.compareTo("red") == 0) && (currentCity.getRedCubeCount() > 0)){
    		if (currentCity.getRedCubeCount() == 1 && (currentRole == Roles.MEDIC || curesDiscovered.getRedCure() == true)) {
    			action++; // increment action by 1 for medic on the last cube removal.  It does use an action to treat uncured diseases, but this will only count it once
    		}
    		if (currentRole != Roles.MEDIC && curesDiscovered.getRedCure() == false) {
    			action++;
    		}
    		return currentCity.removeCube(color);
    	}
    	else if((color.compareTo("yellow") == 0) && (currentCity.getYellowCubeCount() > 0)){
    		if (currentCity.getYellowCubeCount() == 1 && (currentRole == Roles.MEDIC || curesDiscovered.getYellowCure() == true)) {
    			action++; // increment action by 1 for medic on the last cube removal.  It does use an action to treat uncured diseases, but this will only count it once
    		}
    		// if cures are discovered or are a medic, only take 1 action in the above conditional, otherwise non-medics and non-cured diseases take 1 action per cube treated.
    		if (currentRole != Roles.MEDIC && curesDiscovered.getYellowCure() == false) {
    			action++;
    		}
    		return currentCity.removeCube(color);
    	}
    	else
    		return null;

    }

    /*
    "Discard the City card that matches the city you are in to place a research
    station there. Take the research station from the pile next to the board. If all
    6 research stations have been built, take a research station from anywhere
    on the board."
     */
    public boolean buildStation(City city) {

    	// If you already have a research station in the city (ie atlanta), only proceed if it's not already built
    	if (city.getResearchCenterStatus() == false) {
    		if (this.currentRole == Roles.OPERATIONS_EXPERT) {
    			action++;
    			return true; // if the player is an ops. expert, they do not need a card to build a research station.
    		}
    		else {
    			for (int i = 0; i < cards.size(); i++) {
        			if (city.getName().compareTo(cards.get(i).getFormalName()) == 0) {
        				city.buildResearchStation();
        				action++;
        				return true;
        			}
        		}
    		}
    		
    	}
        return false;
    }


     /*
     "give/take the City card that matches the city you are in to another player"
      */
    public boolean giveCityCard(Player giver, Player recipient, Card city) {

        /*"As an action, the Researcher may give any City card from
           her hand to another player in the same city as her, without
           this card having to match her city."
        */
    	
    	// if player A and player B are not in the same city, disallow trading a city card
    	if (giver.getCurrentCity().getName().compareTo(recipient.getCurrentCity().getName()) != 0) {
    		return false;
    	}
    	
    	
    	// If the giver doesn't have the right city card, reject the trade
    	// If giver is a researcher, there is no need to verify that the city card matches the city the giver
    	// and receiver are in.
    	if (giver.getRole() == Roles.RESEARCHER) {
    		giver.getCards().remove(city);
	        recipient.getCards().add(city);
	        return true;
    	}
    	else {
    		for (int i = 0; i < giver.getTotalCards(); i++) {
        		if(giver.getCurrentCity().getName().compareTo(city.getFormalName()) == 0) {
        			giver.getCards().remove(city);
        	        recipient.getCards().add(city);
        	        return true;
        		}
        	}
    	}
    	
    	
    	return false;       
    }

    /*
    At any research station, discard 5 City cards of the same color from your
    hand to cure the disease of that color. Move the disease’s cure marker to its
    Cure Indicator.  Consumes 1 action if a cure is found
     */
    public boolean discoverCure(DeckController playerDiscard, CuresDiscoveredController curesDiscovered) {
        int yellowCount = 0;
        int blackCount = 0;
        int blueCount = 0;
        int redCount = 0;
        int minimum;
        if(this.currentRole == Roles.SCIENTIST) {
        	minimum = 4;
        }
        else {
        	minimum = 5;
        }
         if(!currentCity.getResearchCenterStatus())
             return false;

        //counting cards color
        for(Card aCard : cards){
            if(aCard.getColor().compareTo("yellow") == 0)
                yellowCount++;
            if(aCard.getColor().compareTo("black") == 0)
                blackCount++;
            if(aCard.getColor().compareTo("blue") == 0)
                blueCount++;
            if(aCard.getColor().compareTo("red") == 0)
                redCount++;
        }

        if( yellowCount < minimum && blackCount < minimum && blueCount < minimum && redCount < minimum)
               return false;


        //remove 5 cards of same color
        // Had to revise, caused LinkedList iterator crash
        // also check to make sure a cure wasn't already discovered, to stop a player
        // from wasting their cards.
        if( yellowCount >= minimum && curesDiscovered.getYellowCure() == false) {
        	for (int i = 0; i < cards.size(); i++) {
                if(cards.get(i).getColor().compareTo("yellow") == 0) {
                	playerDiscard.pushCard(cards.remove(i));
                	i--;  // the size shrunk by 1, so we decrement i to account for that
                }
        	}
        	curesDiscovered.setYellowCure(true);
        	this.action++;
        }            

        if( blackCount >= minimum && curesDiscovered.getBlackCure() == false) {
        	for (int i = 0; i < cards.size(); i++) {
                if(cards.get(i).getColor().compareTo("black") == 0) {
                	playerDiscard.pushCard(cards.remove(i));
                	i--;  // the size shrunk by 1, so we decrement i to account for that
                }
        	}
        	curesDiscovered.setBlackCure(true);
        	this.action++;
        }

        if( blueCount >= minimum && curesDiscovered.getBlueCure() == false) {
        	for (int i = 0; i < cards.size(); i++) {
                if(cards.get(i).getColor().compareTo("blue") == 0) {
                	playerDiscard.pushCard(cards.remove(i));
                	i--;  // the size shrunk by 1, so we decrement i to account for that
                }
        	}
        	curesDiscovered.setBlueCure(true);
        	this.action++;
        }

        if( redCount >= minimum && curesDiscovered.getRedCure() == false) {
        	for (int i = 0; i < cards.size(); i++) {
                if(cards.get(i).getColor().compareTo("red") == 0) {
                	playerDiscard.pushCard(cards.remove(i));
                	i--;  // the size shrunk by 1, so we decrement i to account for that
                }
        	}
        	curesDiscovered.setRedCure(true);
        	this.action++;
        }
        
        return true;
    }
    
    // use event card function, identifies the event card played for processing
    public int useEventCard(Card eventCard) {
    	
    	if (eventCard.getFileName().compareToIgnoreCase("airlift") == 0)
    		return 1;
    	else if (eventCard.getFileName().compareToIgnoreCase("forecast") == 0)
    		return 2;
    	else if (eventCard.getFileName().compareToIgnoreCase("governmentgrant") == 0)
    		return 3;
    	else if (eventCard.getFileName().compareToIgnoreCase("onequietnight") == 0)
    		return 4;
    	else if (eventCard.getFileName().compareToIgnoreCase("resilientpopulation") == 0)
    		return 5;
    	else
    		return 0;
    }
    
    public GamePixmap_interfaceView getHeadPhoto() {
    	return headPhoto;
    }

    public int getTotalCards() {
        return cards.size();
    }

    public GamePixmap_interfaceView getPawnImage() {
        return pawnImage;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
    public void manuallyIncreaseActionCount() {
    	this.action += 1;  // add one to the action count
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    public void setCards(LinkedList<Card> cards) {
        this.cards = cards;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }
    
    public Card getContingencyPlannerEventSpecial() {
    	return this.contingencyPlannerEventSpecial;
    }

	public Card removeContingencyPlannerEventSpecial() {
		Card temp = this.contingencyPlannerEventSpecial;
		// It's possible to write this so it just obliterates the card completely.  But I like to track where stuff goes, so I use purgatory to hang onto it
		this.contingencyPlannerEventSpecial = null;  // write null to card so we know there's nothing there
		return temp;
	}

	public void addContingencyPlannerEventSpecial(Card contingencyPlannerEventSpecial) {
		this.contingencyPlannerEventSpecial = contingencyPlannerEventSpecial;
	}
	
	public boolean checkForStoredContingencyPlannerCard() {
		// if the event card is null, there definitely isn't one stored there
		if (this.contingencyPlannerEventSpecial == null) {
			return false;
		}
		// If there is an event card already here, don't allow a new card to overwrite it.  It should be spent before allowing a new card to go in 
		if (this.contingencyPlannerEventSpecial.getType().compareTo("Event Card") == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getRoleName() {
		return roleName;
	}
	
	public String getRoleName1() {
		return this.roleName1;
	}

	public String getAbilities1() {
		return abilities1;
	}

	public String getAbilities2() {
		return abilities2;
	}

	public String getAbilities3() {
		return abilities3;
	}
}
