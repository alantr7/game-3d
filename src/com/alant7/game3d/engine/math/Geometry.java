package com.alant7.game3d.engine.math;

import com.alant7.game3d.engine.math.object.Shape3;

import java.awt.*;

public class Geometry {

    public static Shape3[] Cuboid (Vector3 Start, Vector3 Size, java.awt.Color[] Textures) {

        Shape3[] shape = new Shape3[5];

        java.awt.Color[] Texture = new java.awt.Color[6];
        for (int i = 0; i < 6; i++) {
            if (i < Textures.length) {
                Texture[i] = Textures[i];
            } else Texture[i] = java.awt.Color.BLACK;
        }

        // TOP SIDE
        shape[0] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y + Size.y, Start.z + Size.z)
                }, Texture[0]
        );

        // FRONT
        shape[1] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y, Start.z + Size.z)
                }, Texture[1]
        );

        // LEFT
        shape[2] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y, Start.z)
                }, Texture[2]
        );

        // RIGHT
        shape[3] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z + Size.z)
                }, Texture[3]
        );

        // BEHIND
        shape[4] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x, Start.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z)
                }, Texture[4]
        );

        return shape;

    }

    public static double[] RotateAround (double Angle, double[] P, double[] O) {
        double X = Math.cos(Angle) * (P[0] - O[0]) - Math.sin(Angle) * (P[1] - O[1]) + O[0];
        double Y = Math.sin(Angle) * (P[0] - O[0]) + Math.cos(Angle) * (P[1] - O[1]) + O[1];

        return new double[] {
                X, Y
        };
    }

    public static double Distance (double x0, double y0, double z0, double x1, double y1, double z1) {

        return Math.sqrt(
                Square(x1 - x0) + Square(y1 - y0) + Square(z1 - z0)
        );

    }

    public static double Square (double d) {
        return d * d;
    }

}
