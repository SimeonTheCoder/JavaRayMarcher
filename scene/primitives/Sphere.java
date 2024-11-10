package scene.primitives;

import math.Vec3;

public class Sphere implements Primitive{
    public Vec3 pos;
    public float radius;

    public Sphere(Vec3 origin, float radius) {
        this.pos = origin;
        this.radius = radius;
    }

    @Override
    public float distance(Vec3 point) {
        return pos.sub(point).length() - radius;
    }
}
