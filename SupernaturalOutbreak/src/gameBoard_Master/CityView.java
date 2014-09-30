/*
 * Programmer: David Bloss
 */

package gameBoard_Master;

public class CityView {
	public void printCityDetails(String cityName, String cityColor, String[] connectedCities,
								 int blackCubeCount, int blueCubeCount, int redCubeCount, 
								 int yellowCubeCount, int xCoordinate, int yCoordinate, 
								 boolean researchCenter) 
	{
		System.out.println("Current City's name: " + cityName);
		System.out.println(cityName + " is part of the " + cityColor + " country.");
		System.out.println(cityName + " is located at X: " + xCoordinate + " Y: " + yCoordinate);
		System.out.println(cityName + " currently has " + blackCubeCount + " black Cubes.");
		System.out.println(cityName + " currently has " + blueCubeCount + " blue Cubes.");
		System.out.println(cityName + " currently has " + redCubeCount + " red Cubes.");
		System.out.println(cityName + " currently has " + yellowCubeCount + " yellow Cubes.");
		System.out.println(cityName + " is connected to: ");
		
		for(int i = 0; i < connectedCities.length; i++){
			System.out.println("\t" + connectedCities[i]);
		}
		
		if(researchCenter = true)
			System.out.println(cityName + " has a research center.");
		else
			System.out.println(cityName + " does not have a research center.");
	}
}
