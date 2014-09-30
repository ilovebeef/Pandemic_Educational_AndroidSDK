/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_frameworkView;

import android.view.MotionEvent;

public abstract class GameScreen_View {
	protected final Game_interfaceView game;

    public GameScreen_View(Game_interfaceView _game) {
        game = _game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

	public boolean onTouchEvent(MotionEvent ev) {

		return false;
	}
}
