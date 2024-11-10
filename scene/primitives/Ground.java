package scene.primitives;

import math.Vec3;

public class Ground implements Primitive{
    public float pos;
    public boolean upside;

    public Ground(float pos) {
        this.pos = pos;
        upside = false;
    }

    public Ground(float pos, boolean upside) {
        this.pos = pos;
        this.upside = upside;
    }

    @Override
    public float distance(Vec3 point) {
        return !upside ? point.y - pos : pos - point.y;
    }
}
