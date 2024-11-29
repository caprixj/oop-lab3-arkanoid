package com.example.breakoutgame.game.objects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import game.objects.Ball;

public class BallTest {

    private Ball ball;
    private final int screenX = 800;
    private final int screenY = 1200;

    @Before
    public void setUp() {
        ball = new Ball(screenX, screenY);
    }

    @Test
    public void testInitialPosition() {
        ball.reset(screenX, screenY);
        assertEquals((float) screenX / 2, ball.getRect().left, 0.01);
        assertEquals(screenY - 20, ball.getRect().top, 0.01);
    }

    @Test
    public void testReverseYVelocity() {
        float initialY = ball.yVelocity;
        ball.reverseYVelocity();
        assertEquals(-initialY, ball.yVelocity, 0.01);
    }

    @Test
    public void testReverseXVelocity() {
        float initialX = ball.xVelocity;
        ball.reverseXVelocity();
        assertEquals(-initialX, ball.xVelocity, 0.01);
    }

    @Test
    public void testClearObstacleY() {
        ball.clearObstacleY(100);
        assertEquals(100, ball.getRect().bottom, 0.01);
    }

    @Test
    public void testClearObstacleX() {
        ball.clearObstacleX(50);
        assertEquals(50, ball.getRect().left, 0.01);
    }
}

