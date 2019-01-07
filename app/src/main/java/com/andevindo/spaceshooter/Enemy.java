package com.andevindo.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import static com.andevindo.spaceshooter.GameView.ENEMY_DESTROYED;
import static com.andevindo.spaceshooter.GameView.SCORE;

/**
 * Created on   : 8/12/2017
 * Developed by : Hendrawan Adi Wijaya
 * Github       : https://github.com/andevindo
 * Website      : http://www.andevindo.com
 */

public class Enemy {

    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private Rect mCollision;
    private int mScreenSizeX;
    private int mScreenSizeY;
    private int mEnemies[];
    private int mMaxX;
    private int mMaxY;
    private int mHP;
    private int mSpeed;
    private boolean mIsTurnRight;
    private SoundPlayer mSoundPlayer;

    public Enemy(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer) {
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        mSoundPlayer = soundPlayer;

        mHP = 5;

        mEnemies = new int[]{R.drawable.enemy_red_1, R.drawable.enemy_red_2, R.drawable.enemy_red_3};
        Random random = new Random();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), mEnemies[random.nextInt(3)]);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 3/5, mBitmap.getHeight() * 3/5, false);

        mSpeed = random.nextInt(3) + 1;

        mMaxX = screenSizeX - mBitmap.getWidth();
        mMaxY = screenSizeY - mBitmap.getHeight();

        mX = random.nextInt(mMaxX);
        mY = 0 - mBitmap.getHeight();

        if (mX<mMaxX){
            mIsTurnRight = true;
        }else{
            mIsTurnRight = false;
        }

        mCollision = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }

    public void update(){
        mY += 7 * mSpeed;

        if (mX<=0){
            mIsTurnRight = true;
        }else if (mX>=mScreenSizeX-mBitmap.getWidth()){
            mIsTurnRight = false;
        }

        if (mIsTurnRight){
            mX += 7 * mSpeed;
        }else{
            mX -= 7 * mSpeed;
        }

        mCollision.left = mX;
        mCollision.top = mY;
        mCollision.right = mX + mBitmap.getWidth();
        mCollision.bottom = mY + mBitmap.getHeight();
    }

    public Rect getCollision() {
        return mCollision;
    }

    public void hit(){
        if (--mHP ==0){
            SCORE += 50;
            ENEMY_DESTROYED++;
            destroy();
        }else{
            mSoundPlayer.playExplode();
        }
    }

    public void destroy(){
        mY = mScreenSizeY + 1;
        mSoundPlayer.playCrash();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }
}
