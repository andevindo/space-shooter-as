package com.andevindo.spaceshooter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mScore, mMeteor, mEnemy, mNullHighScore;
    private LinearLayout mHighScoreContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        mBack = findViewById(R.id.back);
        mScore = findViewById(R.id.score);
        mMeteor = findViewById(R.id.meteor);
        mEnemy = findViewById(R.id.enemy);
        mNullHighScore = findViewById(R.id.null_high_score);
        mHighScoreContainer = findViewById(R.id.high_score_container);
        mBack.setOnClickListener(this);

        loadHighScore();
    }

    void loadHighScore(){
        SharedPreferencesManager spm = new SharedPreferencesManager(this);
        if (spm.getHighScore()!=-1){
            mNullHighScore.setVisibility(TextView.GONE);
            mHighScoreContainer.setVisibility(LinearLayout.VISIBLE);
            mScore.setText(spm.getHighScore() + "");
            mMeteor.setText(spm.getMeteorDestroyed() + "");
            mEnemy.setText(spm.getEnemyDestroyed() + "");
        }else{
            mNullHighScore.setVisibility(TextView.VISIBLE);
            mHighScoreContainer.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
