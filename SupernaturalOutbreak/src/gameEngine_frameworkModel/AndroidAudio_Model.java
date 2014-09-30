package gameEngine_frameworkModel;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import gameEngine_frameworkView.GameAudio_interfaceView;
import gameEngine_frameworkView.GameBGM_interfaceView;
import gameEngine_frameworkView.GameSFX_interfaceView;
import gameEngine_frameworkModel.AndroidGame_bgmModel;

public class AndroidAudio_Model implements GameAudio_interfaceView{
	AssetManager _asset;
	SoundPool _sfxPool;
	public AndroidAudio_Model (Activity _act) {
		_act.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		_asset = _act.getAssets();
		_sfxPool = new SoundPool (20, AudioManager.STREAM_MUSIC, 0);
	}
	@Override
	public GameBGM_interfaceView newMusic(String _fileName) {
		try {
			AssetFileDescriptor _assetDes = _asset.openFd(_fileName);
			return new AndroidGame_bgmModel (_assetDes); 
		}
		catch (IOException _e) {
			throw new RuntimeException ("File was not found");
		}
	}
	@Override
	public GameSFX_interfaceView newSound(String _fileName) {
		try {
			AssetFileDescriptor _assetDex = _asset.openFd(_fileName);
			int sfxID = _sfxPool.load(_assetDex, 1);
			return new AndroidSFX_Model (_sfxPool, sfxID);
		}
		catch (IOException _e) {
			throw new RuntimeException ("File was no found");
		}
	}
}
