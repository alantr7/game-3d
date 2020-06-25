package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.framework.GameWindow;
import com.alant7.game3d.engine.math.Triangle3D;
import com.alant7.game3d.engine.math.Vector2D;
import com.alant7.game3d.engine.math.Vector3D;

import java.awt.*;

public abstract class GameObject {

    public Vector3D Position = new Vector3D(), Velocity = new Vector3D();
    public Vector2D Rotation = new Vector2D();
    private ObjectModel DefaultModel = new ObjectModel();
    private RenderModel RenderModel = new RenderModel();

    public GameObject() {}

    public void SetModel(ObjectModel Model) {
        DefaultModel = Model;
        RenderModel = RenderModel.Create(DefaultModel, new Vector2D(), new Vector2D(0, 0));
    }

    public void DefaultRender(Graphics2D Graphics) {

        Graphics.setColor(Color.BLACK);

        for (Polygon p : RenderModel.Polygons) {
            Polygon p2 = new Polygon();
            for (int i = 0; i < 3; i++) {
                p2.addPoint(p.xpoints[i] + 640, p.ypoints[i] + 320);
            }

            Graphics.fillPolygon(p2);

        }

    }

    public abstract void Render(Graphics2D Graphics);
    public abstract void Update();

    public void Rotate(Vector2D Origin, Vector2D Rotation) {
        this.Rotation = Rotation;
        RenderModel = RenderModel.Create(DefaultModel, Origin, Rotation);
    }

}
