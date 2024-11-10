package scene;

import math.Light;
import math.Vec3;
import scene.primitives.Primitive;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    public List<Primitive> objects;
    public List<Light> lights;

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
//            minValue = smin(minValue, objects.get(i).distance(point), 1f);
            minValue = Math.min(minValue, objects.get(i).distance(point));
        }

        return minValue;
    }
}
