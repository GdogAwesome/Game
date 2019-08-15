package com.example.bradmobile.testtexture.Utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;


public class Audio {
    AudioAttributes attrs;
    SoundPool sp;
    MediaPlayer mp;


    public Audio(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setAudioAttributes(attrs)
                    .build();
        }else{
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC,0);

        }

    }

    /**
     *
     * @param context
     * @param resId
     *
     * @return
     */
    public int loadSFX(Context context, int resId){

       return sp.load(context, resId, 1); //uses 1 priority to keep compatibility
    }
    public int playSFX(int trackId, int priority, int loop){
        return sp.play(trackId, .05f, .05f, priority, loop, 1.0f);
    }

    public void loadTrack(Context context, int resId){

        mp = MediaPlayer.create(context, resId);
    }
    public void startMusic(){
        mp.start();
    }
    public void pauseMusic(){
        mp.pause();
    }
    public void releaseMP(){
        mp.release();
    }
    public void releaseSoundPool(){
        sp.release();
    }
}
