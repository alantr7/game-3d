package com.alant7.game3d.engine.math;

public class Face {

    public Vector3D[] Points = new Vector3D[3];
    public Vector3D Color = new Vector3D();

    public Face(Vector3D p1, Vector3D p2, Vector3D p3, Vector3D color) {
        Points[0] = p1;
        Points[1] = p2;
        Points[2] = p3;
        this.Color = color;
    }
    public Face(Vector3D p1, Vector3D p2, Vector3D p3) {
        this(p1, p2, p3, new Vector3D());
    }

}
