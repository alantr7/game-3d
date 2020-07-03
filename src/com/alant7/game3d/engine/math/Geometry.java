package com.alant7.game3d.engine.math;

import com.alant7.game3d.engine.math.object.Shape3;
import java.awt.Color;

public class Geometry {

    public static Shape3[] Cuboid (Vector3 Start, Vector3 Size, Color[] Colors) {

        Shape3[] shape = new Shape3[5];
        int LastColor = 0;

        Color[] Color = new Color[6];
        for (int i = 0; i < 6; i++) {
            if (i < Colors.length) {
                Color[i] = Colors[i];
            } else Color[i] = java.awt.Color.BLACK;
        }

        // TOP SIDE
        shape[0] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y + Size.y, Start.z + Size.z)
                }, Color[0]
        );

        // FRONT
        shape[1] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y, Start.z + Size.z)
                }, Color[1]
        );

        // LEFT
        shape[2] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y, Start.z + Size.z),
                        new Vector3(Start.x, Start.y, Start.z)
                }, Color[2]
        );

        // RIGHT
        shape[3] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z + Size.z),
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z + Size.z)
                }, Color[3]
        );

        // BEHIND
        shape[4] = new Shape3(
                new Vector3[] {
                        new Vector3(Start.x + Size.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x, Start.y + Size.y, Start.z),
                        new Vector3(Start.x, Start.y, Start.z),
                        new Vector3(Start.x + Size.x, Start.y, Start.z)
                }, Color[4]
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

}
