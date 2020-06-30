package com.alant7.game3d.engine.math.object;

import com.alant7.game3d.engine.math.Vector;

public class Plane {

	public Vector V1, V2, NV;
	public double[] P;

	public Plane(Vector VE1, Vector VE2, double[] Z) {
		P = Z;
		
		V1 = VE1;
		
		V2 = VE2;
		
		NV = V1.CrossProduct(V2);
	}
}
