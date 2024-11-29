package com.example.breakoutgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BreakoutGame extends Activity {

    BreakoutView breakoutView;

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

        // game is paused at the start
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        private long timeThisFrame;

        public BreakoutView(Context context) {
            super(context);

            ourHolder = getHolder();
            paint = new Paint();
        }

        @Override
        public void run() {
            while (playing) {
                // counting fps

                long startFrameTime = System.currentTimeMillis();

                if (!paused){
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
            // not yet implemented
        }

        public void draw() {
            // make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                // draw the background color
                canvas.drawColor(Color.argb(255,  26, 128, 182));

                // brush color for drawing
                paint.setColor(Color.argb(255,  255, 255, 255));

                // draw the paddle

                // draw the ball

                // draw the bricks

                // draw the HUD

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

                    break;

                // player removed finger from the screen
                case MotionEvent.ACTION_UP:

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