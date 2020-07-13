package com.alant7.game3d.engine.event;

import java.util.ArrayList;

public interface MouseMotionListener {

    ArrayList<MouseMotionListener> LISTENERS = new ArrayList<>();

    void MouseMoved (int MoveX, int MoveY);

}
