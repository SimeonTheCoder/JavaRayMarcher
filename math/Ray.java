package math;

public class Ray {
    public Vec3 position;
    public Vec3 direction;

    public float distanceTravelled;

    public Ray() {

    }

    public Ray(Vec3 position) {
        this.position = position;
    }

    public static Ray fromVec2(Vec2 screenspacePos, float fov, Vec3 cameraPos) {
        Ray ray = new Ray(
                new Vec3(
                        screenspacePos.x,
                        screenspacePos.y,
                        fov
                ).normalized()
        );

        ray.direction = ray.position;
        ray.position = new Vec3(screenspacePos.x + cameraPos.x, screenspacePos.y + cameraPos.y, cameraPos.z);

        return ray;
    }
}
