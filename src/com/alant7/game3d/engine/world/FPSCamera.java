package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.event.KeyboardListener;
import com.alant7.game3d.engine.event.MouseMotionListener;
import com.alant7.game3d.engine.framework.GameWindow;
import com.alant7.game3d.engine.framework.Graphics;
import com.alant7.game3d.engine.math.Vector;

import java.awt.event.KeyEvent;

public class FPSCamera extends Camera implements KeyboardListener, MouseMotionListener {

    public FPSCamera() {
        KeyboardListener.LISTENERS.add(this);
        MouseMotionListener.LISTENERS.add(this);
    }

    @Override
    public void Update() {

    }

    @Override
    public void MouseMoved(int MoveX, int MoveY) {

        System.out.println (String.format("MOUSE MOVEMENT: %s, %s", MoveX, MoveY));
        int DifX = Graphics.MouseX - (int)GameWindow.ScreenSize.getWidth() / 2;
        int DifY = Graphics.MouseY - (int)GameWindow.ScreenSize.getHeight() / 2;

        DifY *= 6 - Math.abs(VertLook) * 5;

        Camera.Rotate (HorLook + DifX / HorRotSpeed, VertLook - DifY / VertRotSpeed);
        Graphics.CenterMouse();

        UpdateView();

    }

    @Override
    public void KeyPressed(int Key) {

        double[] Movement;

        switch (Key) {
            case KeyEvent.VK_W:
                Movement = Forward();
                break;
            case KeyEvent.VK_A:
                Movement = Left();
                break;
            case KeyEvent.VK_S:
                Movement = Back();
                break;
            case KeyEvent.VK_D:
                Movement = Right();
                break;
            default:
                Movement = new double[] {0, 0, 0};
                break;
        }

        Vector MoveVector = new Vector(Movement[0], Movement[1], Movement[2]);
        Camera.Move(ViewFrom[0] + MoveVector.x * MovementSpeed, ViewFrom[1] + MoveVector.y * MovementSpeed, ViewFrom[2] + MoveVector.z * MovementSpeed);
    }

    @Override
    public void KeyReleased(int Key) {

    }
}
