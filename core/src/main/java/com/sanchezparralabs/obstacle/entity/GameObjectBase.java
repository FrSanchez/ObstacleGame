package com.sanchezparralabs.obstacle.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public abstract class GameObjectBase {

    private float x = 0;
    private float y = 0;
    private final Circle bounds;

    public GameObjectBase(float boundRadius) {
        bounds = new Circle(x, y, boundRadius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
    }


    protected void updateBounds() {
        bounds.setPosition(x, y);
    }

    protected Circle getBounds() {
        return bounds;
    }
}
