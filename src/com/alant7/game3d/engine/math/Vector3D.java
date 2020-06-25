package com.alant7.game3d.engine.math;

public class Vector3D {

    public double X = 0, Y = 0, Z = 0;

    public Vector3D(int X, int Y, int Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }
    public Vector3D() {
        this(0, 0, 0);
    }

    public Vector3D Add(Vector3D v) {
        this.X += v.X;
        this.Y += v.Y;
        this.Z += v.Z;

        return this;
    }

    public Vector3D Multiply(Vector3D v) {
        this.X *= v.X;
        this.Y *= v.Y;
        this.Z *= v.Z;

        return this;
    }

    public Vector3D Rotate(Vector2D Origin, Vector2D v) {

        double AngleX = Math.toRadians(v.X);
        double AngleY = Math.toRadians(v.Y);

        Vector2D rx = Vector2D.Rotate(new Vector2D(X, Z), Origin, AngleX);
        Vector2D ry = Vector2D.Rotate(new Vector2D(rx.Y, Y), Origin, AngleY);

        X = rx.X;
        Y = ry.Y;
        Z = ry.X;

        return this;

    }

}
