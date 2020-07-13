package com.alant7.game3d.engine.event;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public interface KeyboardListener {

    ArrayList<KeyboardListener> LISTENERS = new ArrayList<>();
    ArrayList<Integer> KEYS_DOWN = new ArrayList<>(5);

    void KeyPressed     (int Key);
    void KeyReleased    (int Key);

}
