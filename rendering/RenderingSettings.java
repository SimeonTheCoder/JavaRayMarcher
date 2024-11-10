package rendering;

import math.Vec3;

public class RenderingSettings {
    public static int THREAD_COUNT = 36;

    public static int RESOLUTION_SCALING = 16;
    public static int BOUNCES = 3;

    public static float EXPOSURE = 1f;

    public static int LIGHT_SAMPLES = 16;
    public static float ROUGHNESS = 1;

    public static final int ITERATIONS = 1000;

    public static float COLLISION_THRESHOLD = 0.00001f;
    public static float OUT_OF_BOUNDS = 1000f;

    public static final Vec3 WORLD = new Vec3(0f);
}
