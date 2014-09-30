package gameBoard_Master;

import java.util.Stack;

public class WorldController {
	private World model;
	private WorldView view;
	
	public WorldController(World model, WorldView view) {
		this.model = model;
        this.view = view;
    }
	
	public City[] getWorldCities(){
		return model.getCities();
	}
	
    public City findCity(String name){
    	return model.findCity(name);
    }
    
    public boolean buildResearchStation(City city){
    		// returns true if success
    		// fails if 6 already exist or city already has a station
    	return model.addStation(city);
    }
    
    public boolean transferResearchStation(City fromCity, City toCity){
    		// true if successful
    		// fails if fromCity has no station
    	return model.transferStation(fromCity, toCity);
    }
    
    public Stack<City> getResearchStations(){
		return model.getResearchStations();
	}
    
    public void updateView() {
        view.printWorldDetails(model.getCities(), model.getResearchStations());
    }
    public int getNumberOfResearchStations() {
    	return model.getNumberResearchStations();
    }
}
