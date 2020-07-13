package com.alant7.game3d.engine.rendering.animation;

import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.world.GameObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectAnimation {

    public ArrayList<Keyframe> Keyframes = new ArrayList<>();

    public int CurrentFrameIndex = 0;
    public boolean Loop = false, Completed = false;

    public Vector3 TotalRotation = new Vector3();

    public Keyframe CurrentFrame() {
        return Keyframes.get(CurrentFrameIndex);
    }

    public ObjectAnimation (Keyframe... kfs) {
        Keyframes.addAll(Arrays.asList(kfs));
    }
    public ObjectAnimation () {}

    public void NextFrame() {

        CurrentFrameIndex++;
        if (CurrentFrameIndex >= Keyframes.size()) {
            CurrentFrameIndex = 0;
            Completed = true;
        }

        CurrentFrame().StartTime = System.currentTimeMillis();

    }

    public void Update() {
        long Time = System.currentTimeMillis();
        if (Time - CurrentFrame().StartTime >= CurrentFrame().Duration) {

            TotalRotation.Add(CurrentFrame().Rotation);
            while (TotalRotation.y > 6.28) TotalRotation.y -= 6.28;

            NextFrame();

        }
    }

    public Vector3 GetCurrentRotation() {

        Keyframe cf = CurrentFrame();
        double  x = cf.xrotinc * (System.currentTimeMillis() - cf.StartTime) + TotalRotation.x,
                y = cf.yrotinc * (System.currentTimeMillis() - cf.StartTime) + TotalRotation.y,
                z = cf.zrotinc * (System.currentTimeMillis() - cf.StartTime) + TotalRotation.z;

        return new Vector3 (x, y, z);

    }

    public void Restart() {

        TotalRotation.x = 0;
        TotalRotation.y = 0;
        TotalRotation.z = 0;

        CurrentFrameIndex = 0;
        Completed = false;

        if (Keyframes.size() > 0) Keyframes.get(0).StartTime = System.currentTimeMillis();

    }

}
