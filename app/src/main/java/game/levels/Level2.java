package game.levels;

import android.content.Context;
import game.objects.Brick;

public class Level2 extends Level {

    public Level2(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
        int brickWidth = screenX / 10;
        int brickHeight = screenY / 15;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                if ((row + col) % 2 == 0) { // Patterned brick placement
                    bricks.add(new Brick(row, col, brickWidth, brickHeight));
                }
            }
        }
    }
}

