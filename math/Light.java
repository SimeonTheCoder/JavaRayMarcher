package math;

public class Light {
    public Vec3 pos;
    public float intensity;
    public Vec3 color;
    public float radius;

    public Light(Vec3 pos, float intensity, Vec3 color, float radius) {
        this.pos = pos;
        this.intensity = intensity;
        this.color = color;
        this.radius = radius;
    }

    public Light clone () {
        return new Light (
            this.pos.clone(),
            this.intensity,
            this.color.clone(),
            this.radius
        );
    }
}
