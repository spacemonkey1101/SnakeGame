package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Apple {
    //location of the apple on the grid
    private Point mLocation = new Point();

    //range of values to spawn the apple
    private Point mSpawnRange;
    //size of apple in pixel
    private int mSize;

    //apple image
    private Bitmap mBitmapApple;

    Apple(Context context, Point point , int i){
        mSpawnRange = point;
        mSize = i;

        mLocation.x = -10; //hide apple until game starts
        mBitmapApple = BitmapFactory.decodeResource(context.getResources() , R.drawable.apple);

        //resize the apple
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple , i,i,false);
    }
    // This is called every time an apple is eaten
    void spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        mLocation.x = random.nextInt(mSpawnRange.x) + 1;
        mLocation.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return mLocation;
    }

    // Draw the apple
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapApple,
               mLocation.x * mSize, mLocation.y * mSize, paint);

    }
}
