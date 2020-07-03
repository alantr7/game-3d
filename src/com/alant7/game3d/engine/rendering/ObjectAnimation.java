package com.alant7.game3d.engine.rendering;

import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.world.GameObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectAnimation {

    public ArrayList<Keyframe> Keyframes = new ArrayList<>();

    public int CurrentFrameIndex = 0;

    public Vector2 TotalRotation = new Vector2();

    public Keyframe CurrentFrame() {
        return Keyframes.get(CurrentFrameIndex);
    }

    public ObjectAnimation (Keyframe... kfs) {
        Keyframes.addAll(Arrays.asList(kfs));
    }
    public ObjectAnimation () {}

    public void NextFrame() {

        CurrentFrameIndex++;
        if (CurrentFrameIndex >= Keyframes.size()) CurrentFrameIndex = 0;

        CurrentFrame().StartTime = System.currentTimeMillis();

    }

    public Keyframe GetPreviousFrame() {
        int FrameIndex = CurrentFrameIndex - 1;
        if (FrameIndex < 0) FrameIndex = Keyframes.size() - 1;

        if (FrameIndex < 0) return CurrentFrame();

        return Keyframes.get(FrameIndex);
    }

    public void Update(GameObject Object) {

        long Time = System.currentTimeMillis();
        if (Time - CurrentFrame().StartTime >= CurrentFrame().Duration) {

            TotalRotation.Add(CurrentFrame().Rotation);
            while (TotalRotation.y > 6.28) TotalRotation.y -= 6.28;

            NextFrame();

        }
    }

    public Vector2 GetCurrentRotation() {

        Keyframe cf = CurrentFrame();
        System.out.println (String.format("ROTATION | X Increase: %s, Y Increase: %s, Rotation.x: %s, Rotation.y: %s, Duration: %s", cf.xrotinc, cf.yrotinc, cf.Rotation.x, cf.Rotation.y, cf.Duration));

        double  x = cf.xrotinc * (System.currentTimeMillis() - cf.StartTime),
                y = cf.yrotinc * (System.currentTimeMillis() - cf.StartTime) + TotalRotation.y;

        return new Vector2 (x, y);

    }

}
