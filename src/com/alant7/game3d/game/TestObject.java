package com.alant7.game3d.game;

import com.alant7.game3d.engine.math.Face;
import com.alant7.game3d.engine.math.Triangle3D;
import com.alant7.game3d.engine.math.Vector2D;
import com.alant7.game3d.engine.math.Vector3D;
import com.alant7.game3d.engine.world.GameObject;
import com.alant7.game3d.engine.world.ObjectModel;

import java.awt.*;

public class TestObject extends GameObject {

    public TestObject() {
        SetModel(new ObjectModel(
                new Triangle3D(
                        new Vector3D(1, 1, 1),
                        new Vector3D(100, 1, 1),
                        new Vector3D(100, 1, 100)
                )
        ));
    }

    @Override
    public void Render(Graphics2D Graphics) {
        super.DefaultRender(Graphics);
    }

    @Override
    public void Update() {

    }
}
