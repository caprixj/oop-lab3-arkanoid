package game.levels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import game.objects.Ball;
import game.objects.Brick;
import game.objects.Paddle;

public abstract class Level {

    protected Paddle paddle;
    protected Ball ball;
    protected List<Brick> bricks;

    protected int screenX;
    protected int screenY;

    public Level(Context context) {
        screenX = context.getResources().getDisplayMetrics().widthPixels;
        screenY = context.getResources().getDisplayMetrics().heightPixels;

        paddle = new Paddle(screenX, screenY);
        ball = new Ball(screenX, screenY);
        bricks = new ArrayList<>();
        initialize();
    }

    protected abstract void initialize();

    public void reset() {
        ball.reset(screenX, screenY);
        bricks.clear();
        initialize();
    }

    public void update(long fps) {
        paddle.update(fps);
        ball.update(fps);

        for (Brick brick : new ArrayList<>(bricks)) {
            if (brick.getVisibility() && brick.getRect().intersect(ball.getRect())) {
                brick.setInvisible();
                ball.reverseYVelocity();
                bricks.remove(brick);
            }
        }

        if (ball.getRect().bottom > screenY || ball.getRect().top < 0) {
            ball.reverseYVelocity();
        }

        if (ball.getRect().left < 0 || ball.getRect().right > screenX) {
            ball.reverseXVelocity();
        }
    }

    public void draw(Canvas canvas, Paint paint) {
//        paddle.draw(canvas, paint);
//        ball.draw(canvas, paint);
//
//        for (Brick brick : bricks) {
//            if (brick.getVisibility()) {
//                paint.setColor(Color.argb(255, 249, 129, 0));
//                canvas.drawRect(brick.getRect(), paint);
//            }
//        }
    }

    public boolean isComplete() {
        return bricks.isEmpty();
    }

    public void handleInput(MotionEvent motionEvent) {
//        if (motionEvent.getX() > screenX / 2) {
//            paddle.setMovementState(Paddle.RIGHT);
//        } else {
//            paddle.setMovementState(Paddle.LEFT);
//        }
    }
}

