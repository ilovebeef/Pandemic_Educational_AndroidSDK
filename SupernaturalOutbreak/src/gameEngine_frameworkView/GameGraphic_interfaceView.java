/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkView;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Picture;

public interface GameGraphic_interfaceView {
	public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }
	public GamePixmap_interfaceView newPixmap (String fileName, PixmapFormat format);

    public void clear(int color);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawPixmap(GamePixmap_interfaceView _newPixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);
    
    public void drawPixmap(GamePixmap_interfaceView _newPixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight, int oldX, int oldY);

    public void drawPixmap(GamePixmap_interfaceView _newPixmap, int x, int y);

    public int getWidth();

    public int getHeight();
    
    //10/31/2013: added newText function
    public void newText (String _Text);
    
    //10/31/2013: added drawText to handle text that needs to be outputted onto the screen
    public void drawText (String _Text, int x, int y);
    
    public void drawTextColor(String _Text, int x, int y, Paint color);
}
