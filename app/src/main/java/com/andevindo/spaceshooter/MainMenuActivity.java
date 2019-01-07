package com.andevindo.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mPlay, mHighScore, mExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Membuat tampilan menjadi full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Membuat tampilan selalu menyala jika activity aktif
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mPlay = findViewById(R.id.play);
        mHighScore = findViewById(R.id.high_score);
        mExit = findViewById(R.id.exit);

        mPlay.setOnClickListener(this);
        mHighScore.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.high_score:
                startActivity(new Intent(this, HighScoreActivity.class));
                break;
            case R.id.exit:
                finish();
                break;
        }
    }
}
