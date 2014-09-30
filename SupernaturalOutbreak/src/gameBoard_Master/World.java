package gameBoard_Master;

import java.util.Stack;

public class World {
	
	private final int TOTAL_CITIES = 48; 		// all cities in Pandemic
	private City[] masterList;
	private Stack<City> researchCities; 		// max of 6 Research Stations per game
	
	public World(){
		 this.masterList = new City[TOTAL_CITIES];
		 this.researchCities = new Stack<City>();
		 buildCities(blackCities, blueCities, redCities, yellowCities);
	}

		//  top left: x=0 y=0 
		//  bottom right: x=1068 y=752
	private int[] blackXCoordinates = {497, 637, 561, 781, 757, 583, 696, 823, 643, 722, 647, 702};
	private	int[] blackYCoordinates = {280, 243, 292, 383, 231, 217, 263, 269, 146, 335, 324, 185};
		// all 'x' used as end of line in buildCities()
	String[] blackCities = {"Algiers","Cairo","Istanbul","Madrid","Paris","x",
							"Baghdad","Cairo","Istanbul","Karachi","Riyadh","Tehran","x",	
							"Cairo","Algiers","Baghdad","Istanbul","Riyadh","Khartoum","x",	
							"Chennai","Delhi","Kolkata","Mumbai","Bangkok","Jakarta","x",	
							"Delhi","Chennai","Karachi","Kolkata","Mumbai","Tehran","x",	
							"Istanbul","Algiers","Baghdad","Cairo","Moscow","Milan","St.Petersburg","x", 
							"Karachi","Baghdad","Delhi","Mumbai","Riyadh","Tehran","x",	
							"Kolkata","Chennai","Delhi","Bangkok","Hong Kong","x",	
							"Moscow","Istanbul","Tehran","St.Petersburg","x",	
							"Mumbai","Chennai","Delhi","Karachi","x",	
							"Riyadh","Baghdad","Cairo","Karachi","x",
							"Tehran","Baghdad","Delhi","Karachi","Moscow","x",}; // end blackCities
	
	private int[] blueXCoordinates = {158, 130, 511, 423, 423, 549, 287, 484, 583, 41,  204, 259};
	private int[] blueYCoordinates = {251, 183, 124, 136, 221, 167, 183, 186, 117, 203, 184, 243};
		// all 'x' used as end of line in buildCities()
	String[] blueCities = {"Atlanta","Chicago","Washington","Miami","x",
   		   				   "Chicago","Atlanta","San Francisco","Toronto","Los Angeles","Mexico City","x",
   		   				   "Essen","London","Milan","Paris","St.Petersburg","x",
   		   				   "London","Essen","Madrid","New York","Paris","x",
   		   				   "Madrid","London","New York","Paris","Algiers","Sao Paulo","x",
   		   				   "Milan","Essen","Paris","Istanbul","x",
   		   				   "New York","London","Madrid","Toronto","Washington","x",
   		   				   "Paris","Essen","London","Madrid","Milan","Algiers","x",
   		   				   "St.Petersburg","Essen","Istanbul","Moscow","x",
   		   				   "San Francisco","Chicago","Manila","Tokyo","Los Angeles","x",
   		   				   "Toronto","Chicago","New York","Washington","x",
   		   				   "Washington","Atlanta","New York","Toronto","Miami","x",}; // end blueCities
	
	private int[] redXCoordinates = {840, 871, 906, 884, 852, 973, 1022, 950, 876, 1024, 967, 1022};
	private int[] redYCoordinates = {358, 171, 390, 307, 448, 403, 274,  172, 238, 560,  305, 206 };
		// all 'x' used as end of line in buildCities()
	String[] redCities = {"Bangkok","Ho Chi Minh City","Hong Kong","Jakarta","Chennai","Kolkata","x",
						  "Beijing","Seoul","Shanghai","x",
						  "Ho Chi Minh City","Bangkok","Hong Kong","Jakarta","Manila","x",
						  "Hong Kong","Bangkok","Ho Chi Minh City","Manila","Shanghai","Taipei","Kolkata","x", 
						  "Jakarta","Bangkok","Ho Chi Minh City","Sydney","Chennai","x",
						  "Manila","Ho Chi Minh City","Hong Kong","Sydney","Taipei","San Francisco","x",
						  "Osaka","Taipei","Tokyo","x",
						  "Seoul","Beijing","Shanghai","Tokyo","x",
						  "Shanghai","Beijing","Hong Kong","Seoul","Taipei","Tokyo","x",
						  "Sydney","Jakarta","Manila","Los Angeles","x",
						  "Taipei","Hong Kong","Manila","Osaka","Shanghai","x",
						  "Tokyo","Osaka","Seoul","Shanghai","San Francisco","x",}; // end redCities
	
	private int[] yellowXCoordinates = {207, 278, 569, 594, 531, 491, 155, 52,  122, 225, 198, 321};
	private int[] yellowYCoordinates = {386, 569, 510, 371, 443, 370, 468, 288, 315, 311, 581, 503};
		// all 'x' used as end of line in buildCities()
	String[] yellowCities = {"Bogota","Buenos Aires","Lima","Mexico City","Miami","Sao Paulo", "x",
							 "Buenos Aires","Bogota","Sao Paulo", "x",
							 "Johannesburg","Khartoum","Kinshasa", "x",
							 "Khartoum","Johannesburg","Kinshasa","Lagos","Cairo", "x",
							 "Kinshasa","Johannesburg","Khartoum","Lagos", "x",
							 "Lagos","Khartoum","Kinshasa","Sao Paulo", "x",
							 "Lima","Bogota","Mexico City","Santiago", "x",
							 "Los Angeles","Mexico City","San Francisco","Sydney","Chicago", "x",
							 "Mexico City","Bogota","Lima","Los Angeles","Miami","Chicago", "x",
							 "Miami","Bogota","Mexico City","Atlanta","Washington", "x",
							 "Santiago","Lima", "x",
							 "Sao Paulo","Bogota","Buenos Aires","Lagos","Madrid","x"}; // end yellowCities
	
	private void buildCities(String[] blackCities, String[] blueCities, 
							   String[] redCities, String[] yellowCities){
		
		int index = 0, counter;  // index tracks position in color array, counter tracks position in connections array
		String cityName;		// name of first city on each row as displayed in declarations above
			
			// Black Cities made here
		for (int i = 0; i < 12; i++){
			counter = 0;
			String[] connections = new String[10]; // create a new string every run to not copy over stuff from previous loops
			cityName = blackCities[index++];
			while(blackCities[index].compareTo("x") != 0) { // if blackCity shows x, then leave while loop.
				connections[counter++] = blackCities[index++];
			}
			index++;	//to skip reading the 'x'
			City tempCity = new City(cityName, connections, "black", 
									 (this.blackXCoordinates[i] + 30), (this.blackYCoordinates[i] + 30));
			masterList[i] = tempCity;
		}
			// Blue Cities made here
		index = 0;  // reset index each for loop, so that it starts from the beginning of the color array
		for (int i = 12; i < 24; i++){
			counter = 0;
			String[] connections = new String[10]; // create a new string every run to not copy over stuff from previous loops
			cityName = blueCities[index++];
			while(blueCities[index].compareTo("x") != 0){ // if blueCity shows x, then leave while loop.
				connections[counter++] = blueCities[index++];
			}
			index++;	//to skip reading the 'x'
			City tempCity = new City(cityName, connections, "blue",
									 (this.blueXCoordinates[i%12] + 30), (this.blueYCoordinates[i%12] + 30));
			masterList[i] = tempCity;
		}
			// Atlanta given a Research Station here
		masterList[12].buildResearchStation();
		this.researchCities.push(masterList[12]);
			// Red Cities made here
		index = 0;
		for (int i = 24; i < 36; i++){ 
			counter = 0;
			String[] connections = new String[10]; // create a new string every run to not copy over stuff from previous loops
			cityName = redCities[index++];
			while(redCities[index].compareTo("x") != 0){ // if redCity shows x, then leave while loop.
				connections[counter++] = redCities[index++];
			}
			index++;	//to skip reading the 'x'
			City tempCity = new City(cityName, connections, "red", 
									 (this.redXCoordinates[i%12] + 30), (this.redYCoordinates[i%12] + 30));
			masterList[i] = tempCity;
		}
			// Yellow Cities made here
		index = 0;
		for (int i = 36; i < 48; i++){
			counter = 0;
			String[] connections = new String[10]; // create a new string every run to not copy over stuff from previous loops
			cityName = yellowCities[index++];
			while(yellowCities[index].compareTo("x") != 0){ // if yellowCity shows x, then leave while loop.
				connections[counter++] = yellowCities[index++];
			}
			index++;	//to skip reading the 'x' on the next loop iteration
			City tempCity = new City(cityName, connections, "yellow", 
									 (this.yellowXCoordinates[i%12] + 30), (this.yellowYCoordinates[i%12] + 30));
			masterList[i] = tempCity;
		}
	}
	
	public City[] getCities(){
		return this.masterList;
	}
	
	public City findCity(String name){
		for (int i = 0; i < masterList.length; i++){
			if (name.compareTo(masterList[i].getName()) == 0) {
				return this.masterList[i];
			}
		}
		String[] conn = new String[1];
		conn[0] = "Now in failVille";
		City failCity = new City("Fail City", conn, "green", 953, 16);
		return failCity;
	}
	
	public boolean addStation(City city){
			// max of 6 Research Stations per game
			// checks if City already has a Research Station
		if((this.researchCities.size() < 6) && (this.researchCities.contains(city) == false)){
			this.researchCities.push(city);
			city.buildResearchStation();
			return true;
		}
			// alerts caller that building research station failed
		return false;
	}
	
	public boolean transferStation(City fromCity, City toCity){
		
		if (this.researchCities.removeElement(fromCity)){
			this.researchCities.push(toCity);
			return true;
		}
			// false if "fromCity" does not have a station to transfer
		return false;
	}
	
	public Stack<City> getResearchStations(){
		return this.researchCities;
	}
	public int getNumberResearchStations(){
		return this.researchCities.size();
	}
	
}
