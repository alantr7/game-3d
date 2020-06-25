package com.alant7.game3d.game;

import com.alant7.game3d.engine.framework.GameWindow;
import com.alant7.game3d.engine.world.World;

public class Launcher {

    public static GameWindow Game;

    public static void main(String[] args) {

        Game = new GameWindow("test", 1280, 720);

        World.Objects.add(new TestObject());

    }

}
