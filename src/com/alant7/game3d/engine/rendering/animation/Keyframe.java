package com.alant7.game3d.engine.rendering.animation;

import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;

public class Keyframe {

    public Vector3 Rotation, Location;

    public long StartTime = 0;

    // FRAME DURATION - IN MILLISECONDS
    public long Duration;

    public double xinc, yinc, zinc, xrotinc, yrotinc, zrotinc;

    public Keyframe (Vector3 Location, Vector3 Rotation, long Duration) {
        xinc = Location.x / Duration;
        yinc = Location.y / Duration;
        zinc = Location.z / Duration;

        xrotinc = Rotation.x / Duration;
        yrotinc = Rotation.y / Duration;
        zrotinc = Rotation.z / Duration;

        this.Location = Location;
        this.Rotation = Rotation;

        this.Duration = Duration;
    }

    public Vector3 GetCurrentLocation() {

        double  x = xinc * (System.currentTimeMillis() - StartTime),
                y = yinc * (System.currentTimeMillis() - StartTime),
                z = zinc * (System.currentTimeMillis() - StartTime);

        return new Vector3 (x, y, z);

    }

}
