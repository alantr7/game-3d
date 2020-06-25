package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.math.Face;
import com.alant7.game3d.engine.math.Triangle3D;
import com.alant7.game3d.engine.math.Vector2D;
import com.alant7.game3d.engine.math.Vector3D;

import java.util.ArrayList;

public class ObjectModel {

    public ArrayList<Triangle3D> Triangles = new ArrayList<>();

    public ObjectModel(Triangle3D... triangles) {
        for (Triangle3D t : triangles) Triangles.add(t);
    }

}
