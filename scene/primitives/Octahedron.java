package scene.primitives;

import math.Vec3;

public class Octahedron implements Primitive{
    private Vec3 pos;
    private float size;

    public Octahedron(Vec3 pos, float size) {
        this.pos = pos;
        this.size = size;
    }

    @Override
    public float distance(Vec3 point) {
            point = point.sub(pos);

            point = new Vec3(Math.abs(point.x), Math.abs(point.y), Math.abs(point.z));
            return (point.x+point.y+point.z-size)*0.57735027f;
    }
}
