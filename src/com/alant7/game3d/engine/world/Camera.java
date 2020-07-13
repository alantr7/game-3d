package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.math.Calculator;
import com.alant7.game3d.engine.math.Vector;

public abstract class Camera {

    public static double[] ViewFrom = new double[] { 0, 0, 8 }, ViewTo = new double[] {0, 0, 0};
    public static double zoom = 1000, MinZoom = 100, MaxZoom = 2500, MovementSpeed = 0.5;
    public static double VertLook = -0.4, HorLook = 3, HorRotSpeed = 900, VertRotSpeed = 2200;

    public static Vector VerticalVector = new Vector (0, 0, 1);

    public abstract void Update();

    // FOR VERTICAL ROTATION:

    //      double difX = (NewMouseX - GameWindow.ScreenSize.getWidth() / 2);
    //		double difY = (NewMouseY - GameWindow.ScreenSize.getHeight() / 2);
    //
    //		difY *= 6 - Math.abs(VertLook) * 5;
    //
    //		Camera.Rotate (HorLook + difX / HorRotSpeed, VertLook - difY / VertRotSpeed);

    public static void Rotate (double Hor, double Vert) {
        HorLook  = Hor;
        VertLook = Vert;

        if(VertLook > 0.999) VertLook = 0.999;
        if(VertLook < -.999) VertLook = -.999;
    }

    public static double[] Forward() {
        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
        return new double[] {
                ViewVector.x, ViewVector.y, ViewVector.z
        };
    }

    public static double[] Back() {
        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
        return new double[] {
                -ViewVector.x, -ViewVector.y, -ViewVector.z
        };
    }

    public static double[] Left() {
        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
        Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);

        return new double[] {
                SideViewVector.x, SideViewVector.y, SideViewVector.z
        };
    }

    public static double[] Right() {
        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
        Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);

        return new double[] {
                -SideViewVector.x, -SideViewVector.y, -SideViewVector.z
        };
    }

    public static void Move(double x, double y, double z) {
        ViewFrom[0] = x;
        ViewFrom[1] = y;
        ViewFrom[2] = z;

        UpdateView();
    }

    public static void UpdateView() {
        double r = Math.sqrt(1 - (VertLook * VertLook));
        ViewTo[0] = ViewFrom[0] + r * Math.cos(HorLook);
        ViewTo[1] = ViewFrom[1] + r * Math.sin(HorLook);
        ViewTo[2] = ViewFrom[2] + VertLook;

        Calculator.Prepare();
    }

}
