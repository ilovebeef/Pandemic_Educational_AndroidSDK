/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkView;
	//import the enum type PixmapFormat that we have created from gameGraphic_Interface
import gameEngine_frameworkView.GameGraphic_interfaceView.PixmapFormat;


public interface GamePixmap_interfaceView {
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}
