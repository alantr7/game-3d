package com.alant7.game3d.engine.math.object;

import javafx.scene.shape.Polygon;

import java.awt.*;

public class Shape2 {

	public int[] x, y;

	public Paint p;

	boolean Visible;
	public double Distance;

	public Shape2(double[] x, double[] y, Paint p, double Distance) {

		this.x = new int[x.length]; this.y = new int[x.length];

		for (int i = 0; i < x.length; i++) {
			this.x[i] = (int)x[i];
			this.y[i] = (int)y[i];
		}

		this.p = p;
		double Result = (y[1] - y[0]) * (x[2] - x[1])
				- (x[1] - x[0]) * (y[2] - y[1]);

		Visible = Result > 0;
		this.Distance = Distance;
	}

	public boolean Visible() {
		return this.Visible;
	}
}
