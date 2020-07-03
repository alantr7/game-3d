package com.alant7.game3d.engine.math.object;

import com.alant7.game3d.engine.framework.GameWindow;
import com.alant7.game3d.engine.framework.Graphics;
import com.alant7.game3d.engine.math.Calculator;
import com.alant7.game3d.engine.math.Vector3;

import java.awt.Color;

public class Shape3 {

	public Color c;
	public Vector3[] Point;
	public double[] CalcPos, newX, newY;
	public Shape2 DrawablePolygon;
	
	public Shape3(Vector3[] Point, Color c) {
		this.Point = Point;
		this.c = c;
	}

	public Shape2 Project() {
		newX = new double[Point.length];
		newY = new double[Point.length];

		boolean Visible = true;

		for(int i = 0; i < Point.length; i++) {

			if (Graphics.PROJECTED_VECTOR_CACHE.containsKey(Point[i])) {

				double[] p = Graphics.PROJECTED_VECTOR_CACHE.get(Point[i]);

				if (p == null) {
					Visible = false;
					continue;
				}

				newX[i] = p[0];
				newY[i] = p[1];

				continue;

			}

			CalcPos = Calculator.CalculatePositionP(Graphics.ViewFrom, Graphics.ViewTo, Point[i].z, Point[i].x, Point[i].y);
			newX[i] = (GameWindow.ScreenSize.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Graphics.zoom;
			newY[i] = (GameWindow.ScreenSize.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Graphics.zoom;

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

		DrawablePolygon = new Shape2(newX, newY, c, GetDist());
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
		return Math.sqrt((Graphics.ViewFrom[0] - Point[i].z) * (Graphics.ViewFrom[0] - Point[i].z) +
						 (Graphics.ViewFrom[1] - Point[i].x) * (Graphics.ViewFrom[1] - Point[i].x) +
						 (Graphics.ViewFrom[2] - Point[i].y) * (Graphics.ViewFrom[2] - Point[i].y));
	}

}
