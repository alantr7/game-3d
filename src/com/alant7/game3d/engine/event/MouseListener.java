package com.alant7.game3d.engine.event;

import java.util.ArrayList;

public interface MouseListener {

    ArrayList<MouseListener> LISTENERS = new ArrayList<>();

    void LeftClick  (int x, int y);
    void RightClick (int x, int y);

}
