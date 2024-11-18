package scene.primitives;

import math.Vec2;
import math.Vec3;

public interface Primitive {
    float distance(Vec3 point, float displacement);

    int getMaterial();

    Vec2 getUV(Vec3 point);

    Primitive clone();
}
