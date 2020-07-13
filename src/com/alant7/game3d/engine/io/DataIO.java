package com.alant7.game3d.engine.io;

import sun.reflect.Reflection;

import java.io.File;
import java.io.FileOutputStream;

public class DataIO {

    public static void Save(File File, Saveable Save) {

        try {

            FileOutputStream fos = new FileOutputStream(File);
            fos.write(Save.ConvertToSave().getBytes());

        } catch (Exception e) {

        }

    }

    public static void Load(File File, Class c) {

        try {
            Object obj = c.newInstance();
            if (obj instanceof Saveable) return;

            Saveable s = (Saveable)obj;
            s.Create("");
        } catch (Exception e) {}

    }

}
