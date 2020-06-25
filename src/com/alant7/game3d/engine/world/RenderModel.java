package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.math.Triangle3D;
import com.alant7.game3d.engine.math.Vector2D;
import com.alant7.game3d.engine.math.Vector3D;

import java.awt.Polygon;
import java.util.ArrayList;

public class RenderModel {

    ArrayList<Polygon> Polygons = new ArrayList<>();

    public static RenderModel Create (ObjectModel Model, Vector2D Origin, Vector2D Rotation) {

        RenderModel R = new RenderModel();

        for (Triangle3D t : Model.Triangles) {

            Polygon Polygon = new Polygon();
            int[][] points = new int[3][2];

            for (int i = 0; i < t.V.length; i++) {

                Vector3D v = t.V[i].Rotate(Origin, Rotation);

                double X = v.X;
                double Y = v.Z - v.Y;

                points[i][0] = (int)X;
                points[i][1] = (int)Y;

                Polygon.addPoint((int)X, (int)Y);

                System.out.println(String.format("X: %s, Y: %s", X, Y));

            }

            R.Polygons.add(Polygon);

        }

        return R;

    }

    public Vector2D GetDimensions() {

        int MinX = 0, MaxX = 0, MinY = 0, MaxY = 0;

        for (int i = 0; i < Polygons.size(); i++) {

            Polygon poly = Polygons.get(i);

            for (int j = 0; j < poly.npoints; j++) {

                if (poly.xpoints[j] < MinX) MinX = poly.xpoints[j];
                if (poly.xpoints[j] > MaxX) MaxX = poly.xpoints[j];

                if (poly.ypoints[j] > MinY) MinY = poly.ypoints[j];
                if (poly.ypoints[j] > MaxY) MaxY = poly.ypoints[j];

            }

        }

        return new Vector2D(MaxX - MinX, MaxY - MinY);

    }

}
