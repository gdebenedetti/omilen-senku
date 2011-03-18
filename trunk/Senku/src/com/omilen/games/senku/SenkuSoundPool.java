/**
 * Codigo basado en : rbgrn/Freshman at 
 * http://www.anddev.org/using_soundpool_instead_of_mediaplayer-t3115.html
 * */
package com.omilen.games.senku;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SenkuSoundPool {
	
	//public static final int SOUND_MOVE = 1;
	public static final int SOUND_EAT = 2;
	public static final int SOUND_SELECT = 3;
	public static final int SOUND_DONT = 4;
	public static final int SOUND_WIN = 5;
	public static final int SOUND_GAMEOVER = 6;

	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private Context mContext;
	private boolean soundOn = true;
	
	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public SenkuSoundPool(Context context){
		this.mContext = context;
		initSounds();
	}
	
	private void initSounds() {
	     soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();
	     //soundPoolMap.put(SOUND_MOVE, soundPool.load(mContext, R.raw.move, 1));
	     soundPoolMap.put(SOUND_EAT, soundPool.load(mContext, R.raw.eat, 1));
	     soundPoolMap.put(SOUND_SELECT, soundPool.load(mContext, R.raw.select, 1));
	     soundPoolMap.put(SOUND_DONT, soundPool.load(mContext, R.raw.dont, 1));
	     soundPoolMap.put(SOUND_WIN, soundPool.load(mContext, R.raw.gamewin, 1));
	     soundPoolMap.put(SOUND_GAMEOVER, soundPool.load(mContext, R.raw.gameover, 1));
	}
	          
	public void playSound(int sound) {
		 if(!soundOn) return;		 
		 AudioManager mgr = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	     int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	     soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
		 
	     
	}
	
}
