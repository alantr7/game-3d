package com.alant7.game3d.engine.rendering;

import com.alant7.game3d.engine.math.object.Shape3;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectModel {

    public ArrayList<Shape3> Shapes = new ArrayList<>();

    public ObjectModel () {}

    public ObjectModel (Shape3[]... shapes) {

        for (Shape3[] shs : shapes) Shapes.addAll(Arrays.asList(shs));

    }

}
