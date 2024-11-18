package scene.primitives;

import math.Vec2;
import math.Vec3;
import utils.MathUtils;

public class Sphere implements Primitive {
    public Vec3 pos;
    public float radius;

    public int material;

    public Sphere(Vec3 origin, float radius) {
        this.pos = origin;
        this.radius = radius;
    }

    public Sphere(Vec3 origin, float radius, int material) {
        this.pos = origin;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public float distance(Vec3 point, float displacementTexture) {
//        return pos.sub(point).length() - radius - material.sampleTexture(1, MathUtils.UVFromNormal(point.sub(pos).normalized())).x + 0.5f;
        return pos.sub(point).length() - radius + displacementTexture * 0.3f;
//        return pos.sub(point).length() - radius;
    }

    @Override
    public int getMaterial() {
        return this.material;
    }

    @Override
    public Vec2 getUV (Vec3 point) {
        return MathUtils.UVFromNormal(point.sub(pos).normalized());
    }

    @Override
    public Primitive clone() {
        return new Sphere (
                this.pos.clone(), this.radius, this.material
        );
    }

    public Sphere setMaterial (int material) {
        this.material = material;
        return this;
    }
}
