package gameEngine_frameworkModel;

import android.media.SoundPool;
import gameEngine_frameworkView.GameSFX_interfaceView;

public class AndroidSFX_Model implements GameSFX_interfaceView {
	int _sfxID;
	SoundPool _sfxPool;
	public AndroidSFX_Model (SoundPool sfxPool, int sfxID) {
		_sfxID = sfxID;
		_sfxPool = sfxPool;
	}
	
	public void play(float _volControl) {
		_sfxPool.play(_sfxID, _volControl, _volControl, 0, 0, 1);
	}

	public void dispose() {
		_sfxPool.unload(_sfxID);
		
	}

}
