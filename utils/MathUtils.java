package utils;

import math.Vec2;
import math.Vec3;

public class MathUtils {
    public static Vec2 UVFromNormal(Vec3 direction) {
        float theta = (float) Math.atan2(direction.z, direction.x);
        float phi = (float) Math.asin(-direction.y);

        float u = (float) (theta / Math.PI / 2f + 0.5f);
        float v = (float) (phi / Math.PI + 0.5f);

        return new Vec2(u, v).scale(0.99f);
    }

    public static int mod(int x, int y) {
        return x - y * (x / y);
    }

    public static float mod(float x, float y) {
        return (float) (x - y * Math.floor(x / y));
    }
}
