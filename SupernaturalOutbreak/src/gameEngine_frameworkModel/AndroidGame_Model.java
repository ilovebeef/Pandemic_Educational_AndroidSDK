/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkModel;

	//imports interface form other package

import gameEngine_frameworkView.GameAudio_interfaceView;
import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameInput_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public abstract class AndroidGame_Model extends Activity implements Game_interfaceView {
	AndroidRenderView_Model renderView_Master;
	GameGraphic_interfaceView graphicInferface;
	GameAudio_interfaceView audioInterface;
	GameInput_interfaceView inputInterface;
	GameScreen_View screenClass;
	// WakeLock wakeLock_Obj;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        **/
        DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        
        // updated getWidth and getHeight to non-deprecated version using metrics
		float scaleX = (float) frameBufferWidth 
                / metrics.widthPixels/*getWindowManager().getDefaultDisplay().getWidth()*/;
        float scaleY = (float) frameBufferHeight
                / metrics.heightPixels/*getWindowManager().getDefaultDisplay().getHeight()*/;
        
        renderView_Master = new AndroidRenderView_Model(this, frameBuffer);
        graphicInferface = new AndroidGraphics_Model(getAssets(), frameBuffer); 
        audioInterface = new AndroidAudio_Model (this);
        inputInterface = new AndroidInput_Model (this, renderView_Master, scaleX, scaleY);
        screenClass = getStartScreen();
        setContentView (renderView_Master);
        
        fullWakeLock(this, true);
        // kept old code in case some issue was discovered
        //PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //wakeLock_Obj = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	// Function to replace deprecated FULL_WAKE_LOCK
	public void fullWakeLock(Activity activity, boolean keepBright) {
		if(keepBright == true) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} 
		else {
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	@Override
    public void onResume() {
        super.onResume();
        //wakeLock_Obj.acquire();
        screenClass.resume();
        renderView_Master.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //wakeLock_Obj.release();
        renderView_Master.pause();
        screenClass.pause();

        if (isFinishing())
            screenClass.dispose();
    }
    
    public GameInput_interfaceView getInput() {
        return inputInterface;
    }
    
    public GameGraphic_interfaceView getGraphics() {
        return graphicInferface;
    }

    public GameAudio_interfaceView getAudio() {
        return audioInterface;
    }
    
    /*
    public GameBGM_interfaceView getBGM () {
    	return null;
    }
    */

    public void setScreen(GameScreen_View _screen) {
        if (_screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screenClass.pause();
        this.screenClass.dispose();
        _screen.resume();
        _screen.update(0);
        this.screenClass = _screen;
    }
    /**
    public void setScreen_Activity () {
    	this.screenClass.dispose();
    	this.view = view;
    }**/

    public GameScreen_View getCurrentScreen() {
        return screenClass;
    }
}
