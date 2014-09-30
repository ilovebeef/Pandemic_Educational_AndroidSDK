/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkModel;

import gameEngine_frameworkView.GameInput_interfaceView;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;



public class AndroidInput_Model implements GameInput_interfaceView {
	//AccelerometerHandler accelHandler;
    AndroidKey_Model keyHandler;
    AndroidTouch_Model touchHandler;

    public AndroidInput_Model(Context context, View view, float scaleX, float scaleY) {
        //accelHandler = new AccelerometerHandler(context);
        keyHandler = new AndroidKey_Model(view);
        	//another deprecation
        if (VERSION.SDK_INT < 5) 
            touchHandler = new AndroidSingle_Model(view, scaleX, scaleY);
        else
            touchHandler = new AndroidMult_Model(view, scaleX, scaleY);        
    }

    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }
    	//set to return 0 for now as the game will not need it
    public float getAccelX() {
        return 0;
    }

    public float getAccelY() {
        return 0;
    }

    public float getAccelZ() {
        return 0;
    }
	
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}
