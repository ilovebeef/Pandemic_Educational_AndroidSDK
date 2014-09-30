/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkModel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidRenderView_Model extends SurfaceView implements Runnable {
	AndroidGame_Model androidGame_Master;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;
	
	public AndroidRenderView_Model (AndroidGame_Model _androidGame_Master, Bitmap _frameBuffer) {
		super (_androidGame_Master);
		androidGame_Master = _androidGame_Master;
		frameBuffer = _frameBuffer;
		surfaceHolder = getHolder();
	}
	
	public void resume () {
		running = true;
		renderThread = new Thread (this);
		renderThread.start();
	}
	
	public void run () {
		Rect tempRect = new Rect();
		long startDelta = System.nanoTime();
		while (running) {
			if (!surfaceHolder.getSurface().isValid()) {
				continue;
			}
			 
            float deltaTime = (System.nanoTime()-startDelta) / 1000000000.0f;
            startDelta = System.nanoTime();

            androidGame_Master.getCurrentScreen().update(deltaTime);
            androidGame_Master.getCurrentScreen().present(deltaTime);
            
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.getClipBounds(tempRect);
            canvas.drawBitmap(frameBuffer, null, tempRect, null);
            surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void pause () {
		running = false;
		while (true) {
			try {
				renderThread.join();
				return;
			} catch (InterruptedException _e) {
				
			}
		}
	}
}
