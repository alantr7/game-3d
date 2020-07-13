package com.alant7.game3d.engine.world;

import com.alant7.game3d.engine.math.Geometry;
import com.alant7.game3d.engine.rendering.LightSource;

import java.util.ArrayList;

public class World {

    public static ArrayList<LightSource> LightSources = new ArrayList<>();
    public static Camera Camera = new FPSCamera();

    public static double DAYTIME = 100;
    public static int SECTION_SIZE = 20;

    public static ArrayList<GameObject> Objects = new ArrayList<>();

    // Load GameObjects and world state from file
    public static void LoadChunk(int X, int Y) {



    }

    // Unload chunks that are not in range
    public static void UnloadChunks(int X, int Y, int Range) {
        for (int i = Objects.size() - 1; i >= 0; i--) {
            GameObject Object = Objects.get(i);
            double Distance = Math.sqrt(
                    Geometry.Square(Object.Section.x - X)
                    + Geometry.Square(Object.Section.y - Y)
            );

            if (Distance > Range)
                Object.ShouldBeRemoved = true;
        }
    }

    // Unload chunk
    public static void UnloadChunk(int X, int Y) {
        for (int i = Objects.size() - 1; i >= 0; i--) {
            if (Objects.get(i).Section.x == X && Objects.get(i).Section.y == Y) {
                Objects.get(i).ShouldBeRemoved = true;
            }
        }
    }

    // Save chunk
    public static void SaveChunk(int X, int Y) {

    }

    // Save GameObjects and world state to file
    public static void SaveWorld() {



    }

}
