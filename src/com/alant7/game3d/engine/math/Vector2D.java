package com.alant7.game3d.engine.math;

public class Vector2D {

    public double X, Y;

    public Vector2D(double X, double Y) {
        this.X = X;
        this.Y = Y;
    }

    public Vector2D() {
        this(0, 0);
    }

    public static Vector2D Rotate(Vector2D Point, Vector2D Origin, double AngleRad) {

        double x = Math.cos(AngleRad) * (Point.X - Origin.X) - Math.sin(AngleRad) * (Point.Y - Origin.Y) + Origin.X;
        double y = Math.sin(AngleRad) * (Point.X - Origin.X) + Math.cos(AngleRad) * (Point.Y - Origin.Y) + Origin.Y;

        return new Vector2D(x, y);

    }

}
