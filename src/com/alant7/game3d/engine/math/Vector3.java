package com.alant7.game3d.engine.math;

public class Vector3 {

    public int x, y, z;

    public Vector3 (int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3 () {
        this(0, 0, 0);
    }

    public Vector3 Add (Vector3 V) {
        this.x += V.x;
        this.y += V.y;
        this.z += V.z;

        return this;
    }

    public Vector3 Copy () {
        return new Vector3 (x, y, z);
    }

}
