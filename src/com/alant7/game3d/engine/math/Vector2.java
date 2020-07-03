package com.alant7.game3d.engine.math;

public class Vector2 {

    public double x, y;
    public Vector2 (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 () {
        this(0, 0);
    }

    public Vector2 Add(Vector2 V) {
        this.x += V.x;
        this.y += V.y;

        return this;
    }

    public Vector2 Subtract (Vector2 V) {
        this.x -= V.x;
        this.y -= V.y;

        return this;
    }

    public Vector2 Copy() {
        return this;
    }

}
