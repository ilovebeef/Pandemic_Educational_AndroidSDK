/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkModel;

import gameEngine_frameworkView.GamePixmap_interfaceView;
import gameEngine_frameworkView.GameGraphic_interfaceView.PixmapFormat;
import android.graphics.Bitmap;




public class AndroidPixmap_Model implements GamePixmap_interfaceView {
    Bitmap bitmap;
    PixmapFormat format;
    
    public AndroidPixmap_Model(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public PixmapFormat getFormat() {
        return format;
    }

    public void dispose() {
        bitmap.recycle();
    }      
}

