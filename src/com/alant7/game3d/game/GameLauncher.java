package com.alant7.game3d.game;

import com.alant7.game3d.engine.framework.GameWindow;

public class GameLauncher {

    public static void main(String[] args) {

        new GameWindow().WhenReady(GameController::new).Create();

    }

}
