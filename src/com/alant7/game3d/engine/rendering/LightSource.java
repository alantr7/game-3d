package com.alant7.game3d.engine.rendering;

import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.math.object.Shape3;
import com.alant7.game3d.engine.world.GameObject;
import com.alant7.game3d.engine.world.World;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;

public abstract class LightSource {

    public Vector3 Position = new Vector3();
    public int Power = 5;

    public Vector3 Size = new Vector3(1, 0, 1);

    public java.awt.Color Color = java.awt.Color.BLACK;

    public ArrayList<Shape3> Shadows = new ArrayList<>();

    public abstract void UpdateShadows();

    public void DefaultUpdateShadows() {



    }

    public static final double EPSILON = 0.0000001;
    public static boolean LineIntersectsTriangle(Point3d LineStart,
                                                 Vector3d Target,
                                                 Point3d[] Triangle) {

        Point3d v0 = Triangle[0], v1 = Triangle[1], v2 = Triangle[2];

        Vector3d e1 = new Vector3d(), e2 = new Vector3d();
        e1.sub(v1, v0);
        e2.sub(v2, v0);

        double a, f, u, v;

        Vector3d h = new Vector3d(), s = new Vector3d(), q = new Vector3d();
        h.cross(Target, e2);

        a = e1.dot(h);

        if (a > -EPSILON && a < EPSILON) return false;

        f = 1.0 / a;
        s.sub(LineStart, v0);

        u = f * s.dot(h);
        if (u < 0.0 || u > 1.0) return false;

        q.cross(s, e1);
        v = f * Target.dot(q);

        if (v < 0.0 || u + v > 1.0) return false;

        return true;

    }

}
