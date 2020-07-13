package com.alant7.game3d.engine.math;

import com.alant7.game3d.engine.world.Camera;

public class Vector3 {

    public double x, y, z;

    public static Vector3 Zero = new Vector3(0, 0, 0);

    public Vector3 (double x, double y, double z) {
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

    public Vector3 Subtract (Vector3 V) {
        this.x -= V.x;
        this.y -= V.y;
        this.z -= V.z;

        return this;
    }

    public Vector3 Divide (Vector3 V) {
        this.x /= V.x;
        this.y /= V.y;
        this.z /= V.z;

        return this;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;

    }

    @Override
    public boolean equals (Object obj) {

        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Vector3 v = (Vector3) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(v.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(v.y))
            return false;
        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(v.z))
            return false;

        return true;
    }

    public static double[] Project (Vector3 V) {
        return Calculator.CalculatePositionP(Camera.ViewFrom, Camera.ViewTo, V.x, V.y, V.z);
    }

    public String toString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }

    public Vector3 Copy () {
        return new Vector3 (x, y, z);
    }

}
