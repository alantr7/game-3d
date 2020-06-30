package com.alant7.game3d.engine.rendering;

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
                vs[i] = sh.Point[i].Copy().Add(Object.Position);
            }

            //sh.Rotate (Object.Rotation, Object.Origin);
            Shape2 Projected = new Shape3(vs, sh.c).Project();
            if (Projected.Visible()) Model.Polygons.add(Projected);

        }

        return Model;

    }

}
