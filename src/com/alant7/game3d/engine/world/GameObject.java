package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.framework.Graphics;
import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.rendering.animation.AnimationManager;
import com.alant7.game3d.engine.rendering.animation.Keyframe;
import com.alant7.game3d.engine.rendering.animation.ObjectAnimation;
import com.alant7.game3d.engine.rendering.ObjectModel;
import com.alant7.game3d.engine.rendering.RenderModel;

import java.awt.*;
import java.util.ArrayList;

public abstract class GameObject {

    // Variables used for calculating position, velocity, rotation and animations
    private Vector3 Position = new Vector3();
    public Vector3 Velocity = new Vector3(), Rotation = new Vector3(), Origin = new Vector3(), AnimationOrigin = new Vector3();

    // Current world section that GameObject is in. Used for determining if Object should be
    // removed from loaded objects or kept in memory. Variable is only updated if object moved
    public Vector2 Section = new Vector2();

    // Variables used for displaying GameObject
    public ObjectModel Model = new ObjectModel();
    public RenderModel RenderModel = new RenderModel();

    // Variables used for calculating position and rotation combined with animations
    public Vector3 AnimatedPosition = new Vector3(), AnimatedRotation = new Vector3();
    public Vector3 CalculatedPosition = new Vector3(), CalculatedRotation = new Vector3();

    // Variables used for calculating collisions
    public double[] BoundOffset, BoundSize;
    public int BOUND_TYPE = 0;

    // Random ID
    public long ObjectID = System.currentTimeMillis();

    // Possible bounding types
    public static final int BOUNDING_3D = 1, BOUNDING_2D = 2;

    public double LIGHT_MULTIPLIER = 1;

    // Variable used for rendering shadows
    public boolean HAS_SHADOW = false;

    // Variable used for deleting object
    public boolean ShouldBeRemoved = false;

    // Children GameObjects that are updated whenever this object is
    public ArrayList<GameObject> Children = new ArrayList<>();

    // Current animation
    public ObjectAnimation Animation;

    // Void that is called every frame - Before render function
    public abstract void Update();

    // Method that is called by Graphics class if there is animation active
    public void DefaultAnimationUpdate(Vector3 CurrentLocation, Vector3 CurrentRotation) {

        if (Animation == null) return;
        Animation.Update();

        AnimatedPosition = CurrentLocation;
        AnimatedRotation = CurrentRotation;

        SetPosition(Position);
        SetRotation(Rotation, Origin);

        Graphics.SHOULD_UPDATE_SHADOWS = true;

    }

    // Default method that updates position - Used often
    public void DefaultPositionUpdate() {

        if (Velocity.x == 0 && Velocity.y == 0 && Velocity.z == 0) return;

        Graphics.SHOULD_UPDATE_SHADOWS = true;

        boolean CollidesX = false, CollidesZ = false;

        outer: for (int i = 0; i < World.Objects.size(); i++) {

            GameObject Object = World.Objects.get(i);
            if (Object.equals(this)) continue;

            switch (Object.BOUND_TYPE) {

                case BOUNDING_2D:

                    if (!CollidesX && Position.x + Velocity.x + BoundOffset[0] + BoundSize[0] >= Object.Position.x + Object.BoundOffset[0] &&
                        Position.z + BoundOffset[1] + BoundSize[1] >= Object.Position.z + Object.BoundOffset[1] &&

                        Position.x + Velocity.x + BoundOffset[0] < Object.Position.x + Object.BoundOffset[0] + Object.BoundSize[0] &&
                        Position.z + BoundOffset[1] < Object.Position.z + Object.BoundOffset[1] + Object.BoundSize[1]) {

                        CollidesX = true;
                        if (CollidesZ) break outer;

                        continue;

                    }

                    if (!CollidesZ && Position.x + BoundOffset[0] + BoundSize[0] >= Object.Position.x + Object.BoundOffset[0] &&
                            Position.z + Velocity.z + BoundOffset[1] + BoundSize[1] >= Object.Position.z + Object.BoundOffset[1] &&

                            Position.x + BoundOffset[0] < Object.Position.x + Object.BoundOffset[0] + Object.BoundSize[0] &&
                            Position.z + Velocity.z + BoundOffset[1] < Object.Position.z + Object.BoundOffset[1] + Object.BoundSize[1]) {

                        CollidesZ = true;
                        if (CollidesX) break outer;

                    }

                    break;
                case BOUNDING_3D:

                    break;

            }

        }

        if (!CollidesX) Position.x += Velocity.x;
        Position.y += Velocity.y;
        if (!CollidesZ) Position.z += Velocity.z;

        Velocity = new Vector3(0, 0, 0);
        SetPosition (Position);

    }

    // Update calculated position that is used for rendering
    private void RecalculatePositionRotation() {
        CalculatedPosition.x = Position.x + AnimatedPosition.x;
        CalculatedPosition.y = Position.y + AnimatedPosition.y;
        CalculatedPosition.z = Position.z + AnimatedPosition.z;

        CalculatedRotation.x = Rotation.x + AnimatedRotation.x;
        CalculatedRotation.y = Rotation.y + AnimatedRotation.y;
        CalculatedRotation.z = Rotation.z + AnimatedRotation.z;

        UpdateWorldSection();
    }

    // Update current world section. Used for calculating what objects should be unloaded
    public void UpdateWorldSection() {

        Section.x = (int)Math.floor(Position.x / World.SECTION_SIZE);
        Section.y = (int)Math.floor(Position.z / World.SECTION_SIZE);

    }

    // Set bounds for later use when calculating collision
    public void SetBounds(int Type, double[] Offset, double[] Size) {
        this.BOUND_TYPE = Type;
        this.BoundOffset = Offset;
        this.BoundSize = Size;
    }

    // Update render model with new variables
    public void UpdateRenderModel () {
        RenderModel = com.alant7.game3d.engine.rendering.RenderModel.Create(this);
    }

    // Change or set Object's model
    public void SetModel (ObjectModel Model) {

        this.Model = Model;
        UpdateRenderModel();

    }

    // Rotate GameObject - Rotation degrees are in Radians
    public void SetRotation (Vector3 Rotation, Vector3 Origin) {
        this.Rotation = Rotation;
        this.Origin = Origin;

        RecalculatePositionRotation();

        for (int i = 0; i < Children.size(); i++) Children.get(i).SetRotation(Rotation, Origin);
    }

    // Change position of this GameObject
    public void SetPosition (Vector3 Position) {
        this.Position = Position;

        RecalculatePositionRotation();

        for (int i = 0; i < Children.size(); i++) Children.get(i).SetPosition(this.Position);
    }

    public Vector3 GetPosition() {
        return this.Position;
    }

    // Check if X and Y coordinates are inside GameObject's projection
    public boolean Contains(int x, int y) {

        RenderModel m = RenderModel.Create(this);
        for (int i = 0; i < m.Polygons.size(); i++) {

            if (new Polygon(m.Polygons.get(i).x, m.Polygons.get(i).y, m.Polygons.get(i).x.length).contains(x, y)) {
                return true;
            }

        }

        return false;

    }

    // Reset animation variables and set it to loop or not then start it
    public void PlayAnimation(ObjectAnimation Animation, boolean Loop) {

        Animation.Restart();
        Animation.Loop = Loop;

        this.Animation = Animation;
        AnimationManager.ObjectAnimations.add(this);

    }

    // Stop animation and remove it from active list
    public void StopAnimation() {
        AnimationManager.ObjectAnimations.remove(this);
        Animation = null;

        AnimatedPosition.x = 0;
        AnimatedPosition.y = 0;
        AnimatedPosition.z = 0;

        AnimatedRotation.x = 0;
        AnimatedRotation.y = 0;
        AnimatedRotation.z = 0;
    }

    // Remove object from the game
    public void Remove() {

    }

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != getClass()) return false;
        GameObject obj = (GameObject)object;
        return obj.ObjectID == ObjectID;
    }

}
