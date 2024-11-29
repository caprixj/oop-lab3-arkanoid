package com.example.breakoutgame.game.objects;

import static org.junit.Assert.*;

import android.graphics.RectF;

import org.junit.Before;
import org.junit.Test;

import game.objects.Brick;

public class BrickTest {

    private Brick brick;

    @Before
    public void setUp() {
        brick = new Brick(1, 1, 100, 50);
    }

    @Test
    public void testInitialVisibility() {
        assertTrue(brick.getVisibility());
    }

    @Test
    public void testSetInvisible() {
        brick.setInvisible();
        assertFalse(brick.getVisibility());
    }

    @Test
    public void testGetRect() {
        RectF rect = brick.getRect();
        assertNotNull(rect);
    }
}

