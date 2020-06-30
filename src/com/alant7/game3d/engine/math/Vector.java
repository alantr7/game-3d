package com.alant7.game3d.engine.math;

public class Vector {

	public double x = 0;
	public double y = 0;
	public double z = 0;

	public Vector(double x, double y, double z) {
		double Length = Math.sqrt(x*x + y*y + z*z);
		if(Length>0)
		{
			this.x = x/Length;
			this.y = y/Length;
			this.z = z/Length;
		}
	}

	public Vector() {
		this (0, 0, 0);
	}
	
	public Vector CrossProduct(Vector V) {
		return new Vector(
				y * V.z - z * V.y,
				z * V.x - x * V.z,
				x * V.y - y * V.x);
	}

}
