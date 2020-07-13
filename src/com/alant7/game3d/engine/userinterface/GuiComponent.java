package com.alant7.game3d.engine.userinterface;

import java.awt.*;

public abstract class GuiComponent {

    public abstract void Render (Graphics2D g);
    public abstract void OnScreenResize(int Width, int Height);

}
