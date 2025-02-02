package com.example.breakoutgame;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.objects.Ball;
import game.objects.Brick;
import game.objects.Paddle;

public class BreakoutGame extends Activity {

    BreakoutView breakoutView;

    int screenX;
    int screenY;

    Paddle paddle;
    Ball ball;

    Brick[] bricks = new Brick[100];
    int numBricks = 0;

    int score = 0;
    int lives = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);

    }

    class BreakoutView extends SurfaceView implements Runnable {

        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        private long timeThisFrame;

        public BreakoutView(Context context) {
            super(context);

            ourHolder = getHolder();
            paint = new Paint();

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);

            createBricksAndRestart();
        }

        public void createBricksAndRestart(){
            // set the ball back to the start
            ball.reset(screenX, screenY);

            // create the brick wall
            int brickWidth = screenX / 8;
            int brickHeight = screenY / 10;

            numBricks = 0;

            for (int column = 0; column < 8; column ++) {
                for (int row = 0; row < 3; row ++) {
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;
                }
            }

            // if game over reset scores and lives
            if (lives == 0) {
                score = 0;
                lives = 3;
            }
        }

        @Override
        public void run() {
            while (playing) {
                // counting fps
                long startFrameTime = System.currentTimeMillis();

                if (!paused) {
                    update();
                }

                draw();

                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

        public void update() {
            paddle.update(fps);
            ball.update(fps);

            // detect ball-brick collision
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                        bricks[i].setInvisible();
                        ball.reverseYVelocity();
                        score += 10;
                    }
                }
            }

            // detect ball-paddle collision
            if (RectF.intersects(paddle.getRect(), ball.getRect())) {
                ball.setRandomXVelocity();
                ball.reverseYVelocity();
                ball.clearObstacleY(paddle.getRect().top - 2);
            }

            // bounce the ball back if it hits the bottom of the screen
            if (ball.getRect().bottom > screenY) {
                ball.reverseYVelocity();
                ball.clearObstacleY(screenY - 2);

                lives--;

                if (lives == 0) {
                    paused = true;
                    createBricksAndRestart();
                }
            }

            // bounce the ball if it hits the top of the screen
            if (ball.getRect().top < 0) {
                ball.reverseYVelocity();
                ball.clearObstacleY(12);
            }

            // detect left wall collision
            if (ball.getRect().left < 0) {
                ball.reverseXVelocity();
                ball.clearObstacleX(2);
            }

            // detect right wall collision
            if (ball.getRect().right > screenX - 10) {
                ball.reverseXVelocity();
                ball.clearObstacleX(screenX - 22);
            }

            // detect the brick wall is broken
            if (score == numBricks * 10) {
                paused = true;
                createBricksAndRestart();
            }
        }

        public void draw() {
            // make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                // draw the background color
                canvas.drawColor(Color.argb(255,  30, 30, 30));

                // brush color for drawing
                paint.setColor(Color.argb(255,  255, 255, 255));

                // draw the paddle
                canvas.drawRect(paddle.getRect(), paint);

                // draw the ball
                canvas.drawRect(ball.getRect(), paint);

                // draw the bricks
                paint.setColor(Color.argb(255,  249, 129, 0));

                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                }

                // draw the HUD
                paint.setColor(Color.argb(255,  255, 255, 255));

                // draw the score and lives
                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

                // whether the brick wall is broken
                if (score == numBricks * 10) {
                    paint.setTextSize(90);
                    canvas.drawText("YOU HAVE WON!", 10, (float) screenY / 2, paint);
                }

                // whether the player has lost
                if (lives <= 0) {
                    paint.setTextSize(90);
                    canvas.drawText("YOU HAVE LOST!", 10, (float) screenY / 2, paint);
                }

                // draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        // if BreakoutGame Activity is paused or stopped
        // then we shutdown our thread
        public void pause() {
            playing = false;

            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("error", "joining thread");
            }

        }

        // if BreakoutGame Activity is started
        // then start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // player touched the screen
                case MotionEvent.ACTION_DOWN:

                    paused = false;

                    if (motionEvent.getX() > (float) screenX / 2) {
                        paddle.setMovementState(paddle.RIGHT);
                    }
                    else {
                        paddle.setMovementState(paddle.LEFT);
                    }

                    break;

                // player removed finger from the screen
                case MotionEvent.ACTION_UP:

                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }

            return true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        breakoutView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        breakoutView.pause();
    }

}