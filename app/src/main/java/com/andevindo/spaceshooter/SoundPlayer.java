package com.andevindo.spaceshooter;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

/**
 * Created on   : 8/12/2017
 * Developed by : Hendrawan Adi Wijaya
 * Github       : https://github.com/andevindo
 * Website      : http://www.andevindo.com
 */

public class SoundPlayer implements Runnable {

    private Thread mSoundThread;
    private volatile boolean mIsPlaying;
    private SoundPool mSoundPool;
    private int mExplodeId, mLaserId, mCrashId;
    private boolean mIsLaserPlaying, mIsExplodePlaying, mIsCrashPlaying;

    public SoundPlayer(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        mExplodeId = mSoundPool.load(context, R.raw.rock_explode_1, 1);
        mLaserId = mSoundPool.load(context, R.raw.laser_1, 1);
        mCrashId = mSoundPool.load(context, R.raw.rock_explode_2, 1);
    }

    @Override
    public void run() {
        while (mIsPlaying){
            if (mIsLaserPlaying){
                mSoundPool.play(mLaserId, 1, 1, 1, 0, 1f);
                mIsLaserPlaying = false;
            }

            if (mIsExplodePlaying){
                mSoundPool.play(mExplodeId, 1, 1, 1, 0, 1);
                mIsExplodePlaying = false;
            }

            if (mIsCrashPlaying){
                mSoundPool.play(mCrashId, 1, 1, 1, 0, 1);
                mIsCrashPlaying = false;
            }
        }
    }

    public void playCrash(){
        mIsCrashPlaying = true;
    }

    public void playLaser(){
        mIsLaserPlaying = true;
    }

    public void playExplode(){
        mIsExplodePlaying = true;
    }

    public void resume(){
        mIsPlaying = true;
        mSoundThread = new Thread(this);
        mSoundThread.start();
    }

    public void pause() throws InterruptedException {
        Log.d("GameThread", "Sound");
        mIsPlaying = false;
        mSoundThread.join();
    }
}
