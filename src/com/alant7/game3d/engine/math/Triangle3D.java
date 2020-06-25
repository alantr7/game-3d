package com.alant7.game3d.engine.math;

public class Triangle3D {

    public final Vector3D[] V = new Vector3D[3];

    public Triangle3D(Vector3D v1, Vector3D v2, Vector3D v3) {
        V[0] = v1;
        V[1] = v2;
        V[2] = v3;
    }

}
