package gamePlayer_Controller;

import gameBoard_Master.City;
import gameBoard_Master.CuresDiscoveredController;
import gameCube_Master.CubeModel;
import gameDeck_Master.Card;
import gameEngine_frameworkView.GamePixmap_interfaceView;
import java.util.LinkedList;


public class PlayerController {
	private Player model;
	private PlayerView view;
	
	public PlayerController(Player model, PlayerView view) {
		this.model = model;
        this.view = view;
    }
	
	//************
	// Mutators
	//************
	public void setName(String name) {
		model.setName(name);
	}
	
    public void setXCord(int x) {
    	model.setXCord(x);
    }

    public void setYCord(int y) {
        model.setYCord(y);
    }
    
    public void addCard(Card newCard){
    	model.addCard(newCard);
    }

    public Card removeCard(int index) {			// replace with Card parameter??
        return model.removeCard(index);
    }
    
    public void setCards(LinkedList<Card> cards) {
        model.setCards(cards);
    }
    
    public void setAction(int action) {
        model.setAction(action);
    }
    
    public boolean driveFerry(City toCity){
    		// returns false if City too far
    	return model.driveFerry(toCity);
    }
    
    public boolean directFlight(City toCity){
    		// returns false if player lacks toCity Card
    	return model.directFlight(toCity);
    }
    
    public int charterFlight(City toCity){
    		// returns false if player lacks Card of player's current City 
    	return model.charterFlight(toCity);
    }
    
    public boolean shuttleFlight(City fromCity, City[] researchCityArray){
    		// returns false if fromCity or toCity has no research station
    	return model.shuttleFlight(fromCity, researchCityArray);
    }
    
    public CubeModel treatDisease(String color, CuresDiscoveredController curesDiscovered){
    	return model.treatDisease(color, curesDiscovered);
    }
	
	//************
	// Observers
	//************
    
	public String getName() {
        return model.getName();
    }
	
	public Roles getRole(){
		return model.getRole();
	}
	
    public int getXCord() {
        return model.getXCord();
    }

    public int getYCord() {
        return model.getYCord();
    }
    
	public LinkedList<Card> getCardList() {
        return model.getCardList();
    }

	public Card findCard(int index){
		return model.findCard(index);
	}
	
	public int getTotalCards(){
		return model.getTotalCards();
	}
	
	public City getCity(){
		return model.getCurrentCity();
	}
    
    public GamePixmap_interfaceView getPawnImage() {
        return model.getPawnImage();
    }

    public void updateView() {
    	view.printPlayerDetails(model.getName(), model.getRole(), model.getXCord(), model.getYCord(),
    							model.getCardList(), model.getCurrentCity(), model.getAction());
    }
}
