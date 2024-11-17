package math;

public class Vec2 {
    public float x, y;

    public Vec2() {

    }

    public Vec2 (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add (math.Vec2 other) {
        return new math.Vec2(
                this.x + other.x,
                this.y + other.y
        );
    }

    public Vec2 sub (math.Vec2 other) {
        return new math.Vec2(
                this.x - other.x,
                this.y - other.y
        );
    }

    public Vec2 scale (math.Vec2 other) {
        return new math.Vec2(
                this.x * other.x,
                this.y * other.y
        );
    }

    public Vec2 scaleInPlace (math.Vec2 other) {
        this.x *= other.x;
        this.y *= other.y;

        return this;
    }

    public Vec2 scale (float amount) {
        return new math.Vec2(
                this.x * amount,
                this.y * amount
        );
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vec2 normalized() {
        return this.scale(1f / this.length());
    }
}
