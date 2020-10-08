package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    SnakeGame mSnakeGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        mSnakeGame = new SnakeGame(this,point);

        setContentView(mSnakeGame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSnakeGame.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnakeGame.pause();
    }
}