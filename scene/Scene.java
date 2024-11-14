package scene;

import math.Light;
import math.Vec3;
import scene.primitives.Primitive;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    public List<Primitive> objects;
    public List<Light> lights;

    public BufferedImage hdri;

    public Scene() {
        this.objects = new ArrayList<>();
        this.lights = new ArrayList<>();
    }

    float smin( float a, float b, float k )
    {
        k *= 6.0;
        float h = (float) (Math.max( k-Math.abs(a-b), 0.0 )/k);
        return (float) (Math.min(a,b) - h*h*h*k*(1.0/6.0));
    }

    public float getMaxDistance (Vec3 point) {
        float minValue = objects.get(0).distance(point);

        for (int i = 1; i < objects.size(); i ++) {
//            minValue = smin(minValue, objects.get(i).distance(point), 0.2f);
            minValue = Math.min(minValue, objects.get(i).distance(point));
        }

        return minValue;
    }

    public Vec3 world (Vec3 direction) {
        float theta = (float) Math.atan2(direction.z, direction.x);
        float phi = (float) Math.asin(-direction.y);

        float u = (float) (theta / Math.PI / 2f + 0.5f);
        float v = (float) (phi / Math.PI + 0.5f);

        u *= 0.99f;
        v *= 0.99f;

        int rgb = hdri.getRGB((int) (u * hdri.getWidth()), (int) (v * hdri.getHeight()));

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        Vec3 frag = new Vec3(red / 255f, green / 255f, blue / 255f);

        return frag.scale(2);
//        return Vec3.lerp(
//                new Vec3(0, 0, 0),
//                new Vec3(1, 1, 1),
//                (float) Math.min(1, Math.pow((Math.abs(direction.y - 0.5f) * 2f), 5))
//        );
    }
}
