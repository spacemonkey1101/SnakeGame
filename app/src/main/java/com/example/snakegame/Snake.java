package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Snake {
    // segments or body part of the snake as it eats the apple
    private ArrayList<Point> segmentLocations;
    // How big is each individual segment of the snake?
    private int mSegmentSize;

    // furthest point holrizontally and vertically of the snake
    private Point mMoveRange;

    // center of the screen
    private int halfwaypoint;

    private enum Heading {
        UP, DOWN , LEFT ,RIGHT;
    }

    //start by heading RIGHT
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;
    Snake(Context context, Point pointSnake, int ss) {

        segmentLocations = new ArrayList<>();

        mSegmentSize = ss;
        mMoveRange = pointSnake;
        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        mBitmapHeadLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        mBitmapHeadUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        mBitmapHeadDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);

        // Modify the bitmaps to face the snake head in the correct direction
        mBitmapHeadRight = Bitmap.createScaledBitmap(mBitmapHeadRight, ss, ss, false);
        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap.createBitmap(mBitmapHeadRight, 0, 0, ss, ss, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap.createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Matrix operations are cumulative so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap.createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Create and scale the body
        mBitmapBody = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.body);

        mBitmapBody = Bitmap.createScaledBitmap(mBitmapBody,
                        ss, ss, false);

        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfwaypoint = pointSnake.x * ss / 2;
    }

    // Get the snake ready for a new game
    void reset(int w, int h) {

        // Reset the heading to the default RIGHT
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(w / 2, h / 2));
    }
    void move() {
        // Move the snake body first
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next segment
            // going forwards towards the head
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }
        // Get the existing head position
        Point p = segmentLocations.get(0);

        // Move the snake head appropriately
        switch (heading) {
            case UP:
                p.y--;
                break;

            case RIGHT:
                p.x++;
                break;

            case DOWN:
                p.y++;
                break;

            case LEFT:
                p.x--;
                break;
        }
        // Insert the adjusted point back into position 0
        segmentLocations.set(0, p);
    }

    boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        // Hit any of the screen edges
        if (segmentLocations.get(0).x == -1 ||
                segmentLocations.get(0).x > mMoveRange.x ||
                segmentLocations.get(0).y == -1 ||
                segmentLocations.get(0).y > mMoveRange.y) {

            dead = true;
        }

        // Eaten itself?
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            // Have any of the sections collided with the head
            if (segmentLocations.get(0).x ==
                    segmentLocations.get(i).x &&
                    segmentLocations.get(0).y ==
                            segmentLocations.get(i).y) {

                dead = true;
            }
        }
        return dead;
    }
    boolean eatenApple(Point l) {
        //l represents coordinates of an apple
        //if snake head is on the apple
        if (segmentLocations.get(0).x == l.x &&
                segmentLocations.get(0).y == l.y) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the segment in front of it
            segmentLocations.add(new Point(-10, -10));
            return true;
        }
        return false;
    }
    void draw(Canvas canvas, Paint paint) {
        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // Draw the head
            switch (heading) {
                case RIGHT:
                    canvas.drawBitmap(mBitmapHeadRight, segmentLocations.get(0).x * mSegmentSize,
                            segmentLocations.get(0).y * mSegmentSize, paint);
                    break;

                case LEFT:
                    canvas.drawBitmap(mBitmapHeadLeft, segmentLocations.get(0).x * mSegmentSize,
                            segmentLocations.get(0).y * mSegmentSize, paint);
                    break;

                case UP:
                    canvas.drawBitmap(mBitmapHeadUp, segmentLocations.get(0).x * mSegmentSize,
                            segmentLocations.get(0).y * mSegmentSize, paint);
                    break;

                case DOWN:
                    canvas.drawBitmap(mBitmapHeadDown, segmentLocations.get(0).x * mSegmentSize,
                            segmentLocations.get(0).y * mSegmentSize, paint);
                    break;
            }
            // Draw the snake body one block at a time
            for (int i = 1; i < segmentLocations.size(); i++) {
                canvas.drawBitmap(mBitmapBody, segmentLocations.get(i).x
                                * mSegmentSize, segmentLocations.get(i).y *mSegmentSize, paint);
            }


            }

        }
    // Handle changing direction
    void switchHeading(MotionEvent motionEvent) {

        // Is the tap on the right hand side?
        if (motionEvent.getX() >= halfwaypoint) {
            switch (heading) {
                // Rotate right
                case UP:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.UP;
                    break;

            }
        } else {
            // Rotate left
            switch (heading) {
                case UP:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.UP;
                    break;
            }
        }
    }


}

