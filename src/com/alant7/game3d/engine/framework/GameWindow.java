package com.alant7.game3d.engine.framework;

import com.alant7.game3d.engine.math.Vector2D;
import com.alant7.game3d.engine.world.GameObject;
import com.alant7.game3d.engine.world.World;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow implements KeyListener {

    public Graphics Graphics;

    public static GameWindow INSTANCE;

    public JFrame Frame;

    public GameWindow(String Title, int Width, int Height) {
        Frame = new JFrame();
        Frame.setSize(Width, Height);
        Frame.setTitle(Title);

        Graphics = new Graphics();
        Frame.add(Graphics);

        Frame.setVisible(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new Thread(this::GameLoop).start();

        Graphics.addKeyListener(this);

        INSTANCE = this;
    }

    private void GameLoop() {

        while (true) {

            Graphics.Render();

            try {
                Thread.sleep(16);
            } catch (Exception e) {}

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                for (int i = 0; i < World.Objects.size(); i++)
                    World.Objects.get(i).Rotate(new Vector2D(), new Vector2D(1, 0));
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
