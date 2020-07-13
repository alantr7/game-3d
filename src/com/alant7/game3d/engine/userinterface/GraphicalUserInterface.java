package com.alant7.game3d.engine.userinterface;

import com.alant7.game3d.engine.framework.GameWindow;

import java.util.ArrayList;

public class GraphicalUserInterface {

    private static ArrayList<GuiComponent> Components = new ArrayList<>();

    public static void AddComponent(GuiComponent Component) {
        Components.add(Component);

        Component.OnScreenResize(GameWindow.ScreenSize.width, GameWindow.ScreenSize.height);

    }

    public static ArrayList<GuiComponent> GetComponents() {
        return Components;
    }

}
