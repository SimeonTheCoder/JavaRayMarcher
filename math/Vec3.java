package math;

public class Vec3 {
    public float x, y, z;

    public Vec3() {

    }

    public Vec3 (float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 (float r) {
        this.x = r;
        this.y = r;
        this.z = r;
    }

    public Vec3 add (Vec3 other) {
        return new Vec3(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    public Vec3 sub (Vec3 other) {
        return new Vec3(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public Vec3 scale (Vec3 other) {
        return new Vec3(
                this.x * other.x,
                this.y * other.y,
                this.z * other.z
        );
    }

    public Vec3 scale (float amount) {
        return new Vec3(
                this.x * amount,
                this.y * amount,
                this.z * amount
        );
    }

    public Vec3 inverse() {
        return new Vec3(
                -this.x, -this.y, -this.z
        );
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vec3 normalized() {
        return this.scale(1f / this.length());
    }

    public float dot (Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3 cross (Vec3 other) {
        return new Vec3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public static Vec3 lerp (Vec3 a, Vec3 b, float t) {
        return new Vec3(
                a.x * t + b.x * (1f - t),
                a.y * t + b.y * (1f - t),
                a.z * t + b.z * (1f - t)
        );
    }
}
