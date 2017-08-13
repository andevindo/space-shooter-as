package com.andevindo.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created on   : 8/11/2017
 * Developed by : Hendrawan Adi Wijaya
 * Github       : https://github.com/andevindo
 * Website      : http://www.andevindo.com
 */

public class GameView extends SurfaceView implements Runnable {

    private Thread mGameThread;
    private volatile boolean mIsPlaying;
    private Player mPlayer;
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private ArrayList<Laser> mLasers;
    private ArrayList<Meteor> mMeteors;
    private ArrayList<Enemy> mEnemies;
    private ArrayList<Star> mStars;
    private int mScreenSizeX, mScreenSizeY;
    private int mCounter = 0;
    private SoundPlayer mSoundPlayer;
    private SharedPreferencesManager mSP;
    public static int SCORE = 0;
    private volatile boolean mIsGameOver;

    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);

        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        mSP = new SharedPreferencesManager(context);

        mSoundPlayer = new SoundPlayer(context);
        mPaint = new Paint();
        mSurfaceHolder = getHolder();

        reset();
    }

    void reset() {
        SCORE = 0;
        mPlayer = new Player(getContext(), mScreenSizeX, mScreenSizeY, mSoundPlayer);
        mLasers = new ArrayList<>();
        mMeteors = new ArrayList<>();
        mEnemies = new ArrayList<>();
        mStars = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mStars.add(new Star(getContext(), mScreenSizeX, mScreenSizeY, true));
        }
        mIsGameOver = false;
    }

    @Override
    public void run() {
        while (mIsPlaying) {
            if (!mIsGameOver) {
                update();
                draw();
                control();
            }
        }
        Log.d("GameThread", "Run stopped");
    }

    public void update() {
        mPlayer.update();
        if (mCounter % 200 == 0) {
            mPlayer.fire();
        }

        for (Meteor m : mMeteors) {
            m.update();

            if (Rect.intersects(m.getCollision(), mPlayer.getCollision())) {
                m.destroy();
                mIsGameOver = true;
                if (SCORE>=mSP.getHighScore()){
                    mSP.saveHighScore(SCORE);
                }
            }

            for (Laser l : mPlayer.getLasers()) {
                if (Rect.intersects(m.getCollision(), l.getCollision())) {
                    m.hit();
                    l.destroy();
                }
            }
        }

        boolean deleting = true;
        while (deleting) {
            if (mMeteors.size() != 0) {
                if (mMeteors.get(0).getY() > mScreenSizeY) {
                    mMeteors.remove(0);
                }
            }

            if (mMeteors.size() == 0 || mMeteors.get(0).getY() <= mScreenSizeY) {
                deleting = false;
            }
        }
        if (mCounter % 1000 == 0) {
            mMeteors.add(new Meteor(getContext(), mScreenSizeX, mScreenSizeY, mSoundPlayer));
        }

        for (Enemy e : mEnemies) {
            e.update();
            if (Rect.intersects(e.getCollision(), mPlayer.getCollision())) {
                e.destroy();
                mIsGameOver = true;
                if (SCORE>=mSP.getHighScore()){
                    mSP.saveHighScore(SCORE);
                }
            }

            for (Laser l : mPlayer.getLasers()) {
                if (Rect.intersects(e.getCollision(), l.getCollision())) {
                    e.hit();
                    l.destroy();
                }
            }
        }
        deleting = true;
        while (deleting) {
            if (mEnemies.size() != 0) {
                if (mEnemies.get(0).getY() > mScreenSizeY) {
                    mEnemies.remove(0);
                }
            }

            if (mEnemies.size() == 0 || mEnemies.get(0).getY() <= mScreenSizeY) {
                deleting = false;
            }
        }
        if (mCounter % 2000 == 0) {
            mEnemies.add(new Enemy(getContext(), mScreenSizeX, mScreenSizeY, mSoundPlayer));
        }

        for (Star s : mStars) {
            s.update();
        }
        deleting = true;
        while (deleting) {
            if (mStars.size() != 0) {
                if (mStars.get(0).getY() > mScreenSizeY) {
                    mStars.remove(0);
                }
            }

            if (mStars.size() == 0 || mStars.get(0).getY() <= mScreenSizeY) {
                deleting = false;
            }
        }

        if (mCounter % 250 == 0) {
            Random random = new Random();
            for (int i = 0; i < random.nextInt(3) + 1; i++) {
                mStars.add(new Star(getContext(), mScreenSizeX, mScreenSizeY, false));
            }

        }


    }

    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK);
            mCanvas.drawBitmap(mPlayer.getBitmap(), mPlayer.getX(), mPlayer.getY(), mPaint);
            for (Star s : mStars) {
                mCanvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(), mPaint);
            }
            for (Laser l : mPlayer.getLasers()) {
                mCanvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), mPaint);
            }
            for (Meteor m : mMeteors) {
                mCanvas.drawBitmap(m.getBitmap(), m.getX(), m.getY(), mPaint);
            }
            for (Enemy e : mEnemies) {
                mCanvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), mPaint);
            }
            drawScore();
            if (mIsGameOver) {
                drawGameOver();
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    void drawScore() {
        Paint score = new Paint();
        score.setTextSize(30);
        score.setColor(Color.WHITE);
        mCanvas.drawText("Score : " + SCORE, 100, 50, score);
    }

    void drawGameOver() {
        Paint gameOver = new Paint();
        gameOver.setTextSize(100);
        gameOver.setTextAlign(Paint.Align.CENTER);
        gameOver.setColor(Color.WHITE);
        mCanvas.drawText("GAME OVER", mScreenSizeX / 2, mScreenSizeY / 2, gameOver);
        Paint highScore = new Paint();
        highScore.setTextSize(50);
        highScore.setTextAlign(Paint.Align.CENTER);
        highScore.setColor(Color.WHITE);
        mCanvas.drawText("HighScore : " + mSP.getHighScore(), mScreenSizeX / 2, (mScreenSizeY / 2) + 60, highScore);
    }

    public void steerLeft(float speed) {
        mPlayer.steerLeft(speed);
    }

    public void steerRight(float speed) {
        mPlayer.steerRight(speed);
    }

    public void stay() {
        mPlayer.stay();
    }

    public void control() {
        try {
            if (mCounter == 10000) {
                mCounter = 0;
            }
            mGameThread.sleep(20);
            mCounter += 20;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        Log.d("GameThread", "Main");
        mIsPlaying = false;
        try {
            mGameThread.join();
            mSoundPlayer.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        mIsPlaying = true;
        mSoundPlayer.resume();
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mIsGameOver){
                    reset();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
