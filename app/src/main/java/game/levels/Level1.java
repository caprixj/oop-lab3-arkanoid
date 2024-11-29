package game.levels;

import android.content.Context;
import game.objects.Brick;

public class Level1 extends Level {

    public Level1(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
        int brickWidth = screenX / 8;
        int brickHeight = screenY / 20;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                bricks.add(new Brick(row, col, brickWidth, brickHeight));
            }
        }
    }
}

