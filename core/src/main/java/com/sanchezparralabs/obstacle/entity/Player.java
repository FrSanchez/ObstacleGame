package com.sanchezparralabs.obstacle.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sanchezparralabs.obstacle.config.GameConfig;

public class Player extends GameObjectBase {
    private static final float BOUNCE_RADIUS = 0.4f; // world units
    private static final float SIZE = 2 * BOUNCE_RADIUS; // world units

    public Player() {
        super(BOUNCE_RADIUS);
    }

    public void update() {
        float xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        setX(getX() + xSpeed);
        updateBounds();
    }

    public float getWidth() {
        return SIZE;
    }

}
