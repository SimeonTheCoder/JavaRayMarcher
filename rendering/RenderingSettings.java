package rendering;

public class RenderingSettings {
    public static int DENOISE_KERNEL_SIZE = 9;
    public static int THREAD_COUNT = 36;

    public static int RESOLUTION_SCALING = 16;
    public static int BOUNCES = 3;

    public static float EXPOSURE = 1f;

    public static int LIGHT_SAMPLES = 4;
    public static int REFLECTION_SAMPLES = 4;
    public static float ROUGHNESS = 0.5f;

    public static final int ITERATIONS = 1000;

    public static float COLLISION_THRESHOLD = 0.00001f;
    public static float OUT_OF_BOUNDS = 1000f;
}
