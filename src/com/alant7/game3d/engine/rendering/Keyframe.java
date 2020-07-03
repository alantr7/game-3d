package com.alant7.game3d.engine.rendering;

import com.alant7.game3d.engine.math.Vector2;
import com.alant7.game3d.engine.math.Vector3;

public class Keyframe {

    public Vector2 Rotation = new Vector2();
    public Vector3 Location = new Vector3();

    public long StartTime = 0;

    // FRAME DURATION - IN MILLISECONDS
    public long Duration;

    public double xinc, yinc, zinc, xrotinc, yrotinc;

    public Keyframe (Vector3 Location, Vector2 Rotation, long Duration) {
        xinc = Location.x / Duration;
        yinc = Location.y / Duration;
        zinc = Location.z / Duration;

        xrotinc = Rotation.x / Duration;
        yrotinc = Rotation.y / Duration;

        this.Location = Location;
        this.Rotation = Rotation;

        this.Duration = Duration;
    }

    public Vector3 GetCurrentLocation() {

        System.out.println (String.format("LOCATION | X Increase: %s, Location.x: %s, Duration: %s", xinc, Location.x, Duration));

        double  x = xinc * (System.currentTimeMillis() - StartTime),
                y = yinc * (System.currentTimeMillis() - StartTime),
                z = zinc * (System.currentTimeMillis() - StartTime);

        return new Vector3 (x, y, z);

    }

}
