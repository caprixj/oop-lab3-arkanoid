package game.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class GameObject {

    protected RectF rect;
    protected float xVelocity;
    protected float yVelocity;

    public GameObject(float x, float y, float width, float height) {
        rect = new RectF(x, y, x + width, y + height);
        xVelocity = 0;
        yVelocity = 0;
    }

    public RectF getRect() {
        return rect;
    }

    public void update(long fps) {
        rect.offset(xVelocity / fps, yVelocity / fps);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }
}
