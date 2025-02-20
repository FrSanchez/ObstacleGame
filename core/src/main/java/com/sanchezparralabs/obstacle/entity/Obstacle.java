package com.sanchezparralabs.obstacle.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class Obstacle extends GameObjectBase {
    private static final float BOUNCE_RADIUS = 0.3f; // world units
    private static final float SIZE = 2 * BOUNCE_RADIUS; // world units

    private float ySpeed = 0.1f;
    private boolean hit;

    public Obstacle() {
        super(BOUNCE_RADIUS);
    }

    public void update() {
        setY(getY() - ySpeed);
    }

    public float getWidth() {
        return SIZE;
    }

    public boolean isPlayerColliding(Player player) {
        Circle playerBounds = player.getBounds();
        boolean overlaps =  Intersector.overlaps(playerBounds, getBounds());
        hit = overlaps;
        return overlaps;
    }

    public boolean isNotHit() {
        return !hit;
    }
}
