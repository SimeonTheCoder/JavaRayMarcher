package rendering;

public class RenderingSettings {
    public static int DENOISE_KERNEL_SIZE = 9;
    public static int THREAD_COUNT = 36;

    public static int RESOLUTION_SCALING = 16;
    public static int BOUNCES = 4;

    public static float EXPOSURE = 1f;

    public static int LIGHT_SAMPLES = 1;
    public static int REFLECTION_SAMPLES = 2;

    public static final int ITERATIONS = 500;

    public static float COLLISION_THRESHOLD = 0.001f;
    public static float OUT_OF_BOUNDS = 1000f;
}
