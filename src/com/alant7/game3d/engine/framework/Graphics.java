package com.alant7.game3d.engine.framework;

import com.alant7.game3d.engine.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Graphics extends Canvas {

    void Render() {

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            try {
                this.createBufferStrategy(3);
            } catch (Exception e) {}
            return;
        }

        Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int i = 0; i < World.Objects.size(); i++)
            World.Objects.get(i).Render(g2d);

        bs.show();

    }

}
