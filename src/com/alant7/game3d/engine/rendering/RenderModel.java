package com.alant7.game3d.engine.rendering;

import com.alant7.game3d.engine.math.Geometry;
import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.math.object.Shape2;
import com.alant7.game3d.engine.math.object.Shape3;
import com.alant7.game3d.engine.world.GameObject;

import java.util.ArrayList;

public class RenderModel {

    public ArrayList<Shape2> Polygons = new ArrayList<>();

    public static RenderModel Create (GameObject Object) {

        RenderModel Model = new RenderModel();

        for (Shape3 sh : Object.Model.Shapes) {

            Vector3[] vs = new Vector3[sh.Point.length];

            for (int i = 0; i < sh.Point.length; i++) {

                Vector3 V = sh.Point[i].Copy().Add(Object.AnimatedPosition);

                double[] Rot = Geometry.RotateAround (Object.AnimatedRotation.x, new double[] {
                        V.x, V.z
                }, new double[] {
                        Object.AnimatedPosition.x + 2.5, Object.AnimatedPosition.z + 2.5
                });

                double[] Rot2 = Geometry.RotateAround (Object.AnimatedRotation.y, new double[] {
                        Rot[1], V.y
                }, new double[] {
                        Object.AnimatedPosition.z + 2.5, Object.AnimatedPosition.y + 2.5
                });

                V.x = Rot[0];
                V.y = Rot2[1];
                V.z = Rot2[0];

                vs[i] = V;
            }

            //sh.Rotate (Object.Rotation, Object.Origin);
            Shape3 sh3 = new Shape3(vs, sh.c);
            Shape2 Projected = sh3.Project();
            if (Projected.Visible()) Model.Polygons.add(Projected);

        }

        return Model;

    }

}
