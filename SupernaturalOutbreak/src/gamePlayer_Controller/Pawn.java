package gamePlayer_Controller;


public class Pawn {
    private int xCord;
    private int yCord;
    private String pawnNum; // 1-7 pawns

    public Pawn(int xCord, int yCord, String pawnNum) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.pawnNum = pawnNum;
    }
    
    public void setXCoordinate(int x){
    	this.xCord = x;
    }
    
    public void setYCoordinate(int y){
    	this.yCord = y;
    }
    
    public int getXCoordinate(){
    	return this.xCord;
    }
    
    public int getYCoordinate(){
    	return this.yCord;
    }
    
    public String getPawnNum(){
    	return this.pawnNum;
    }
}
