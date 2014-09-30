/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkModel;

import gameEngine_frameworkView.GameTouch_ptemptView;
import gameEngine_frameworkView.GameInput_interfaceView.TouchEvent;
import gameEngine_frameworkView.GameTouch_ptemptView.PoolObjectFactory;
import java.util.ArrayList;
import java.util.List;
import android.view.MotionEvent;
import android.view.View;



public class AndroidSingle_Model implements AndroidTouch_Model {
    boolean isTouched;
    int touchX;
    int touchY;
    GameTouch_ptemptView<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;
    int len = 0;

    public AndroidSingle_Model(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            public TouchEvent createObject() {
                return new TouchEvent();
            }            
        };
        touchEventPool = new GameTouch_ptemptView<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public boolean onTouch(View v, MotionEvent event) {
        synchronized(this) {
            TouchEvent touchEvent = touchEventPool.newObject();
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                isTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                isTouched = true;
                break;
            case MotionEvent.ACTION_CANCEL:                
            case MotionEvent.ACTION_UP:
                touchEvent.type = TouchEvent.TOUCH_UP;
                isTouched = false;
                break;
            }
            
            touchEvent.x = touchX = (int)(event.getX() * scaleX);
            touchEvent.y = touchY = (int)(event.getY() * scaleY);
            touchEventsBuffer.add(touchEvent);                        
            return true;
        }
    }

    public boolean isTouchDown(int pointer) {
        synchronized(this) {
            if(pointer == 0)
                return isTouched;
            else
                return false;
        }
    }

    public int getTouchX(int pointer) {
        synchronized(this) {
            return touchX;
        }
    }

    public int getTouchY(int pointer) {
        synchronized(this) {
            return touchY;
        }
    }
    
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents = touchEventsBuffer;
            //touchEvents.addAll(touchEventsBuffer);
            for (int i = 0; i < touchEventsBuffer.size(); i++) {
            	if (touchEvents.size() < touchEventsBuffer.size()) {
            		touchEvents.add(i, touchEventsBuffer.get(i));
            	}
            	else {
            		touchEvents.set(i, touchEventsBuffer.get(i));
            	}            	
            }
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}
