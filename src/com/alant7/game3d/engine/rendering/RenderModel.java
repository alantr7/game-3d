package com.alant7.game3d.engine.rendering;

import com.alant7.game3d.engine.framework.Graphics;
import com.alant7.game3d.engine.math.Geometry;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.math.object.Shape2;
import com.alant7.game3d.engine.math.object.Shape3;
import com.alant7.game3d.engine.world.GameObject;

import java.util.ArrayList;

import static com.alant7.game3d.engine.world.World.Camera;

public class RenderModel {

    public ArrayList<Shape2> Polygons = new ArrayList<>();

    public static RenderModel Create (GameObject Object) {

        RenderModel Model = new RenderModel();
        Vector3 Origin = Object.Origin;

        for (Shape3 sh : Object.Model.Shapes) {

            Vector3[] vs = new Vector3[sh.Point.length];
            for (int i = 0; i < sh.Point.length; i++) {

                Vector3 V = sh.Point[i].Copy().Add(Object.CalculatedPosition);
                double[] Rot, Rot2, Rot3;

                if (!Object.AnimatedRotation.equals(Vector3.Zero)) {
                    Rot = Geometry.RotateAround(Object.AnimatedRotation.x, new double[]{
                            V.x, V.z
                    }, new double[]{
                            Object.CalculatedPosition.x + Object.AnimationOrigin.x, Object.CalculatedPosition.z + Object.AnimationOrigin.z
                    });

                    // X - Y
                    Rot2 = Geometry.RotateAround(Object.AnimatedRotation.y, new double[]{Rot[0], V.y}, new double[]{
                            Object.CalculatedPosition.x + Object.AnimationOrigin.x, Object.CalculatedPosition.y + Object.AnimationOrigin.y
                    });

                    // Z - Y
                    Rot3 = Geometry.RotateAround(Object.AnimatedRotation.z, new double[]{Rot[1], Rot2[1]}, new double[]{
                            Object.CalculatedPosition.z + Object.AnimationOrigin.z, Object.CalculatedPosition.y + Object.AnimationOrigin.y
                    });

                    V.x = Rot2[0];
                    V.y = Rot3[1];
                    V.z = Rot3[0];
                }
                if (!Object.Rotation.equals(Vector3.Zero)) {
                    Rot = Geometry.RotateAround(Object.Rotation.x, new double[]{V.x, V.z}, new double[]{
                            Object.CalculatedPosition.x + Origin.x, Object.CalculatedPosition.z + Origin.z
                    });

                    // X - Y
                    Rot2 = Geometry.RotateAround(Object.Rotation.y, new double[]{Rot[0], V.y}, new double[]{
                            Object.CalculatedPosition.x + Origin.x, Object.CalculatedPosition.y + Origin.y
                    });

                    // Z - Y
                    Rot3 = Geometry.RotateAround(Object.Rotation.z, new double[]{Rot[1], Rot2[1]}, new double[]{
                            Object.CalculatedPosition.z + Origin.z, Object.CalculatedPosition.y + Origin.y
                    });

                    V.x = Rot2[0];
                    V.y = Rot3[1];
                    V.z = Rot3[0];
                }

                vs[i] = V;
            }

            //sh.Rotate (Object.Rotation, Object.Origin);
            Shape3 sh3 = new Shape3(vs, new java.awt.Color((int)(sh.p.getRed() * Object.LIGHT_MULTIPLIER), (int)(sh.p.getGreen()  * Object.LIGHT_MULTIPLIER), (int)(sh.p.getBlue() * Object.LIGHT_MULTIPLIER)));
            Shape2 Projected = sh3.Project();
            if (Projected.Visible()) Model.Polygons.add(Projected);

        }

        return Model;

    }

}
