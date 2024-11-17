package scene.primitives;

import math.Vec2;
import math.Vec3;

public class Ground implements Primitive {
    public float pos;
    public boolean upside;

    public int material;

    public Ground(float pos) {
        this.pos = pos;
        upside = false;
    }

    public Ground(float pos, boolean upside) {
        this.pos = pos;
        this.upside = upside;
    }

    @Override
    public float distance(Vec3 point, float displacementTexture) {
//        return (!upside ? point.y - pos : pos - point.y) - material.sampleTexture(1, getUV(point)).x * 2f + 1f;
        return (!upside ? point.y - pos : pos - point.y) + displacementTexture * 1f;
//        return !upside ? point.y - pos : pos - point.y;
    }

    @Override
    public int getMaterial() {
        return this.material;
    }

    public Ground setMaterial (int material) {
        this.material = material;
        return this;
    }

    @Override
    public Vec2 getUV(Vec3 point) {
        return new Vec2(point.x / 20f, point.z / 20f);
//        return new Vec2(MathUtils.mod(point.x, 1f), MathUtils.mod(point.z, 1f));
    }
}
