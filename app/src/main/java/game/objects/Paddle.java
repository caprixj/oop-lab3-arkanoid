package game.objects;

import android.graphics.RectF;

public class Paddle {

    private RectF rect;
    private float length;
    private float height;

    // x = far left coordinate
    private float x;

    // y = top coordinate
    private float y;

    // pixels per second
    private float paddleSpeed;

    // whether moving + direction
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = STOPPED;

    public Paddle(int screenX, int screenY){
        length = 130;
        height = 20;

        x = (float) screenX / 2;
        y = screenY - 20;

        rect = new RectF(x, y, x + length, y + height);

        paddleSpeed = 350;
    }

    public RectF getRect(){
        return rect;
    }

    public void setMovementState(int state){
        paddleMoving = state;
    }

    public void update(long fps){
        if (paddleMoving == LEFT) {
            x = x - paddleSpeed / fps;
        }

        if (paddleMoving == RIGHT) {
            x = x + paddleSpeed / fps;
        }

        rect.left = x;
        rect.right = x + length;
    }

}