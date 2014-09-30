package gameCube_Master;

import gameEngine_frameworkView.GamePixmap_interfaceView;

public class CubeModel {
	private String color;
	private GamePixmap_interfaceView image;
	private GamePixmap_interfaceView bigImage;
	
	public void setColor(String color){
		this.color = color;
	}
	public String getColor(){
		return color;
	}
	public GamePixmap_interfaceView getImage() {
		return image;
	}
	public void setImage(GamePixmap_interfaceView image) {
		this.image = image;
	}
    public GamePixmap_interfaceView getBigImage() {
        return bigImage;
    }
    public void setBigImage(GamePixmap_interfaceView bigImage) {
        this.bigImage = bigImage;
    }
}
