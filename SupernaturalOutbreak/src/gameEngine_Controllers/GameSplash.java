/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import gameEngine_Controllers.main.R;
import java.io.InputStream;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class GameSplash extends Activity {
	
		//Will create a little class inside here to handle
	class loadSplash extends View {
		Bitmap splashScreen;
		int screenWidth;
		int screenHeight;
		
			//any class the extends View, must have a default Context Construct
		public loadSplash(Context _context) {
			super(_context.getApplicationContext());
			
			/*************************************************************************************
			 * This whole block was a conflict of interest, display.getSize requires a minimal api
			 * level of 13, which is 3.2, but we need 2.2, I changed the method to make it so it 
			 * it will worth with lvl 8 2.2
			//creates a _display object to handle the dynamic scaling
			Display _display = getWindowManager().getDefaultDisplay();
			//Because the display class's getHeight/Width is deprecated, we will need to use point
			Point _screenSize = new Point ();
			
			_display.getSize(_screenSize)
			screenWidth = _screenSize.x;
			screenHeight = _screenSize.y;
			**************************************************************************************/
				//a trick to dynamically resize the screen to make it fit multiple devices
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);

			screenHeight = metrics.heightPixels;
			screenWidth = metrics.widthPixels;
			GameAssets.screenHeight = metrics.heightPixels;
			GameAssets.screenWidth = metrics.widthPixels;
			try {
					//Loads the asset manager, so it can handle file names
				AssetManager _assetManager = _context.getAssets();
					//Loads an input stream with the name of file
				InputStream _inputStream = _assetManager.open("Menu_Images/splash_logo.png");
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;
				options.inInputShareable = true;
					//defines splashScrren bitmap
				splashScreen = BitmapFactory.decodeStream(_inputStream, null, options);
				splashScreen = Bitmap.createScaledBitmap(splashScreen,screenWidth, screenHeight, true);
					//closes the stream
				_inputStream.close();
			} catch (Exception _E) {
				_E.printStackTrace();
			} finally {
				//does nothing
			}
		}
			//defines this activity's onDraw, belongs to where the Extend View and Context is
	    protected void onDraw (Canvas _canvas) {
	    		//debug to see the the width and height of current screen
	    	Log.d("splash", "Width: " + screenWidth + "_____Height" +screenHeight);
	    	Log.d("GameAssets:", "W:" + ((GameAssets.screenWidth/2)-20) + "********H:" + (GameAssets.screenHeight/2-20) );
	    		//scaling function call needs to be in the onDraw function
	    	_canvas.drawBitmap (splashScreen, 0, 0, null);
	    }
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			//default
		super.onCreate(savedInstanceState);
		setContentView(new loadSplash (this));
			//creates a thread to run the splash
		Thread gameSplash_Thread = new Thread() {
				//thread will run this function
			public void run() {
				try{			
					sleep(2000);
				} catch (Exception _E) {
					_E.printStackTrace();
				} finally{
						//when the thread hits this line, it'll call the gameMaster class, which should be our main
					startActivity(new Intent(getApplicationContext(), GameMaster.class));
					finish();
				}
			}
		};			
		gameSplash_Thread.start();	
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    finish();
	}
}