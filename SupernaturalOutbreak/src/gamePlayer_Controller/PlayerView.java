package gamePlayer_Controller;

import gameBoard_Master.City;
import gameDeck_Master.Card;

import java.util.LinkedList;

public class PlayerView {
	public void printPlayerDetails(String name, Roles role, int x, int y, LinkedList<Card> cards,
								   City currentCity, int actionsUsed){
		System.out.println("Player Name: " + name);
		System.out.println("Player Role: " + role);						// <-- may need attention
		System.out.println("Player Location: X=" + x + " Y=" + y);
		System.out.println("Player City: " + currentCity.getName());	// <-- redundant to X-Y Coordinates?
		System.out.println("Actions used: " + actionsUsed);				// action count not yet accurate
		System.out.print("List of Cards: ");
		
		for(Card aCard : cards)
			System.out.println(aCard.getFormalName()); 
	}
}
