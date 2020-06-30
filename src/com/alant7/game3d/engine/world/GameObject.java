package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.rendering.ObjectModel;
import com.alant7.game3d.engine.rendering.RenderModel;

public abstract class GameObject {

    public Vector3 Position = new Vector3(), Velocity = new Vector3();
    public Vector2 Rotation = new Vector2(), Origin = new Vector2();

    public ObjectModel Model = new ObjectModel();
    public RenderModel RenderModel = new RenderModel();

    public abstract void Update();

    public void DefaultPositionUpdate() {
        Position.Add(Velocity);
    }

    public void UpdateRenderModel () {
        RenderModel = com.alant7.game3d.engine.rendering.RenderModel.Create(this);
    }

    public void SetModel (ObjectModel Model) {

        this.Model = Model;
        UpdateRenderModel();

    }

    public void Rotate (Vector2 Rotation, Vector2 Origin) {
        this.Rotation = Rotation;
        this.Origin = Origin;
    }

}
