package com.alant7.game3d.engine.math.object;

import java.awt.Color;
import java.awt.Polygon;

public class Shape2 {

	public Polygon Polygon;
	public Color c;

	boolean Visible;
	public double Distance;

	public Shape2(double[] x, double[] y, Color c, double Distance) {
		Polygon = new Polygon();
		for(int i = 0; i < x.length; i++)
			Polygon.addPoint((int)x[i], (int)y[i]);

		this.c = c;

		double Result = (y[1] - y[0]) * (x[2] - x[1])
				- (x[1] - x[0]) * (y[2] - y[1]);

		Visible = Result > 0;
		this.Distance = Distance;
	}

	public boolean Visible() {
		return this.Visible;
	}
}
