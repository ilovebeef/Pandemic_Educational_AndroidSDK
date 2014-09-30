package gameBoard_Master;

import java.util.Stack;

public class WorldView {
	public void printWorldDetails(City[] worldCities, Stack<City> researchStations){
		System.out.print("List of the World's cities: ");
		
		for(int i = 0; i < worldCities.length; i++){
			System.out.println(worldCities[i].getName());			
		}
		
		System.out.println("Cities with Research Stations: ");
		
		for(int i = 0; i < researchStations.size(); i++){
			System.out.println(researchStations.get(i).getName());
		}
	}
}
