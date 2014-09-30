/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkModel;

import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.widget.TextView;
import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GamePixmap_interfaceView;


//I added the extension to Activity here so that it could then now use build in stuff
public class AndroidGraphics_Model extends Activity implements GameGraphic_interfaceView {
 	AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect;
    Rect dstRect;
    
    public AndroidGraphics_Model() {
    }

    public AndroidGraphics_Model(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.srcRect = new Rect();
        this.dstRect = new Rect();
    }

    public GamePixmap_interfaceView newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;
        //options.inSampleSize = 8;
        options.inPurgeable = true;
        options.inInputShareable = true;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            //bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap_Model(bitmap, format);
    }

    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }
    
    public void drawPixmap(GamePixmap_interfaceView pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap_Model) pixmap).bitmap, srcRect, dstRect, null);
    }

    public void drawPixmap(GamePixmap_interfaceView pixmap, int xPos, int yPos, int srcX, int srcY,
    		int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
    	// srcX and srcY means where you want to start measuring.  If scaling a picture down, always start at 0, 0
    	// for both srcX and srcY
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + actualWidth - 1;
        srcRect.bottom = srcY + actualHeight - 1;

        dstRect.left = xPos;
        dstRect.top = yPos;
        dstRect.right = xPos + desiredWidth - 1;
        dstRect.bottom = yPos + desiredHeight - 1;

        canvas.drawBitmap(((AndroidPixmap_Model) pixmap).bitmap, srcRect, dstRect, null);
    }

    public void drawPixmap(GamePixmap_interfaceView pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap_Model)pixmap).bitmap, x, y, null);
    }

    public int getWidth() {
        return frameBuffer.getWidth();
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }
    //10/31/2013: added newText function, will return a text view when paseed a string
	@Override
	public void newText(String _Text) {
		TextView tempText = new TextView (this);
		tempText.setText(_Text);
	}
	//10/31/2013: added drawText to handle text that needs to be outputted onto the screen
	@Override
	public void drawText (String _Text, int x, int y) {
		canvas.drawText(_Text, x, y, paint);
	}
	
	@Override
	public void drawTextColor (String _Text, int x, int y, Paint color) {
		canvas.drawText(_Text, x, y, color);
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    finish();
	    System.exit(0);
	}

	
}
