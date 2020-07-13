package com.alant7.game3d.engine.math.object;

import com.alant7.game3d.engine.framework.GameWindow;
import com.alant7.game3d.engine.framework.Graphics;
import com.alant7.game3d.engine.world.Camera;
import com.alant7.game3d.engine.math.Calculator;
import com.alant7.game3d.engine.math.Vector3;

import java.awt.*;

public class Shape3 {

	public java.awt.Color p;
	public Vector3[] Point;
	public double[] CalcPos, newX, newY;
	public Shape2 DrawablePolygon;
	
	public Shape3(Vector3[] Point, java.awt.Color p) {
		this.Point = Point;
		this.p = p;
	}

	public Shape2 Project() {
		newX = new double[Point.length];
		newY = new double[Point.length];

		boolean Visible = true;

		double MinX = 0, MaxX = 0;
		double MinY = 0, MaxY = 0;

		for(int i = 0; i < Point.length; i++) {

			if (Graphics.PROJECTED_VECTOR_CACHE.containsKey(Point[i])) {

				double[] p = Graphics.PROJECTED_VECTOR_CACHE.get(Point[i]);

				if (p == null) {
					Visible = false;
					continue;
				}

				newX[i] = p[0];
				newY[i] = p[1];

				if (p[0] < MinX) MinX = p[0];
				if (p[1] < MinY) MinY = p[1];

				if (p[0] > MaxX) MaxX = p[0];
				if (p[1] > MaxY) MaxY = p[1];

				continue;

			}

			CalcPos = Calculator.CalculatePositionP(Camera.ViewFrom, Camera.ViewTo, Point[i].z, Point[i].x, Point[i].y);
			newX[i] = (GameWindow.Instance.Graphics.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Camera.zoom;
			newY[i] = (GameWindow.Instance.Graphics.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Camera.zoom;

			if (newX[i] < MinX) MinX = newX[i];
			if (newY[i] < MinY) MinY = newY[i];

			if (newX[i] > MaxX) MaxX = newX[i];
			if (newY[i] > MaxY) MaxY = newY[i];

			Graphics.VECTORS_CALCULATED++;

			if (Calculator.t < 0) {
				Visible = false;
				Graphics.PROJECTED_VECTOR_CACHE.put (Point[i], null);

				continue;
			}

			Graphics.PROJECTED_VECTOR_CACHE.put (Point[i], new double[] {
					newX[i], newY[i]
			});

		}

		/*if (
				MinX > GameWindow.Instance.Graphics.getWidth()
				|| MaxX < 0
				|| MinY > GameWindow.Instance.Graphics.getHeight()
				|| MaxY < 0
		) Visible = false;*/

		DrawablePolygon = new Shape2(newX, newY, p, GetDist());
		if (!Visible) DrawablePolygon.Visible = false;

		return DrawablePolygon;
	}

	public double GetDist() {
		double total = 0;
		for(int i = 0; i < Point.length; i++)
			total += GetDistanceToP(i);
		return total / Point.length;
	}
	
	public double GetDistanceToP(int i) {
		return Math.sqrt((Camera.ViewFrom[0] - Point[i].z) * (Camera.ViewFrom[0] - Point[i].z) +
						 (Camera.ViewFrom[1] - Point[i].x) * (Camera.ViewFrom[1] - Point[i].x) +
						 (Camera.ViewFrom[2] - Point[i].y) * (Camera.ViewFrom[2] - Point[i].y));
	}

}
