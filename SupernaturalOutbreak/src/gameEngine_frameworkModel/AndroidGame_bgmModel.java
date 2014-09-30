package gameEngine_frameworkModel;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import gameEngine_frameworkView.GameBGM_interfaceView;

public class AndroidGame_bgmModel implements GameBGM_interfaceView, OnCompletionListener {
	protected MediaPlayer _mPlayer;
	protected boolean _mPlayer_Toggle = false;
	
	public AndroidGame_bgmModel (AssetFileDescriptor _assetDep) {
		_mPlayer = new MediaPlayer();
		try {
			_mPlayer.setDataSource(_assetDep.getFileDescriptor(), _assetDep.getStartOffset(), _assetDep.getLength());
			_mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			_mPlayer.prepare();
			_mPlayer_Toggle = true;
			_mPlayer.setOnCompletionListener(this);
		}
		catch (IOException _e){
			throw new RuntimeException ("File was not found");
		}
	}
		//release music from memory if it is no longer needed
	public void dispose() {
			//checks if it is looping
		if (_mPlayer.isLooping()) {
				//stops
			_mPlayer.stop();
		}
			//releases the music
		_mPlayer.release();
	}
		//returns true if file is looping
	public boolean isLooping() {
		return _mPlayer.isLooping();
	}
		//returns true if file is being used
	public boolean isPlaying() {
		return _mPlayer.isPlaying();
	}
		//returns protected boolean member 
	public boolean isStopped () {
		return !_mPlayer_Toggle;
	}
		//if player is currently playing music, pauses it
	public void pause() {
		if (_mPlayer.isPlaying()) {
			_mPlayer.pause();
		}
	}
	
	public void play() {
			//if a file is currently playing, does nothing
		if (_mPlayer.isPlaying()) {
			return;
		}
		try {
			synchronized (this) {
				if (!_mPlayer_Toggle) {
					_mPlayer.prepare();
				}
				_mPlayer.start();
			}
		}
		catch (IOException _e) {
			throw new RuntimeException ("Error!");
		}
	}
	
	public void setLooping(boolean isLooping) {
		_mPlayer.setLooping (isLooping);
	}
	
	public void setVolume(float _vol) {
		_mPlayer.setVolume(_vol, _vol);
	}
	
	public void stop() {
		_mPlayer.stop();
		synchronized (this) {
			_mPlayer_Toggle = false;
		}
	}
	
	public void onCompletion(MediaPlayer _player) {
		synchronized (this) {
			_mPlayer_Toggle = false;
		}
	}
	
}
