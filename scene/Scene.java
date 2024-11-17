package scene;

import materials.Material;
import math.Light;
import math.Vec2;
import math.Vec3;
import rendering.RenderingSettings;
import scene.primitives.Primitive;
import utils.MathUtils;

import java.awt.image.BufferedImage;

public class Scene {
    public Primitive[] objects;
    public Light[] lights;

    public BufferedImage hdri;

    public Scene(Primitive[] objects, Light[] lights) {
        this.objects = objects;
        this.lights = lights;
    }

    float smin( float a, float b, float k )
    {
        k *= 6.0;
        float h = (float) (Math.max( k-Math.abs(a-b), 0.0 )/k);
        return (float) (Math.min(a,b) - h*h*h*k*(1.0/6.0));
    }

    public float getMaxDistance (Vec3 point, Material[] materials) {
        float minValue = objects[0].distance(
                point,
                materials[
                        objects[0].getMaterial()
                ].sampleTextureGrayscale(
                        Material.DISPLACEMENT,
                        objects[0].getUV(point)
                )
        );

        for (int i = 1; i < objects.length; i ++) {
//            minValue = smin(minValue, objects.get(i).distance(point), 0.2f);
//            minValue = smin(
//                minValue,
//                objects[i].distance(
//                    point,
//                    objects[i].distance(
//                        point,
//                        materials[objects[i].getMaterial()].sampleTextureGrayscale(
//                                1, objects[i].getUV(point)
//                        )
//                    )
//                ),0.3f
//            );
            minValue = Math.min(
                minValue,
                objects[i].distance(
                    point,
                    objects[i].distance(
                        point,
                        materials[objects[i].getMaterial()].sampleTextureGrayscale(
                                Material.DISPLACEMENT, objects[i].getUV(point)
                        )
                    )
                )
            );
        }

        return minValue;
    }

    public float getMaxDistanceApprox (Vec3 point) {
        float minValue = objects[0].distance(
                point,0
        );

        for (int i = 1; i < objects.length; i ++) {
//            minValue = smin(minValue, objects.get(i).distance(point), 0.2f);
//            minValue = smin(
//                    minValue,
//                    objects[i].distance(
//                            point, 1
//                    ), 0.3f
//            );
            minValue = Math.min(
                    minValue,
                    objects[i].distance(
                            point, 0
                    )
            );
        }

        return minValue - 1f;
    }

    public Primitive closestObject (Vec3 point) {
        for (Primitive obj : objects) {
            if (RenderingSettings.COLLISION_THRESHOLD * 10 > obj.distance(point, 0))
                return obj;
        }

        return null;
    }

    public Primitive closestObject (Vec3 point, float displacementTexture) {
        Primitive closestObject = null;
        float minDistance = 10f;

        for (Primitive obj : objects) {
            if (minDistance > obj.distance(point, displacementTexture)) {
                minDistance = obj.distance(point, displacementTexture);
                closestObject = obj;
            }
        }

        return closestObject;
    }

    public int closestMaterial (Vec3 point, float displacementTexture) {
        return closestObject(point).getMaterial();
    }

    public Vec2 closestUV (Vec3 point, float displacementTexture) {
        return closestObject(point).getUV(point);
    }

    public Vec3 world (Vec3 direction) {
        Vec2 uv = MathUtils.UVFromNormal(direction);

        int rgb = hdri.getRGB((int) (uv.x * hdri.getWidth()), (int) (uv.y * hdri.getHeight()));

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        Vec3 frag = new Vec3(red / 255f, green / 255f, blue / 255f);

//        return new Vec3(0);
//        return frag.scale(1f / Math.max(0, 1f - (frag.length() - 0.9f))).scale(frag.length()).scale(9);
//        return frag.sub(new Vec3(0.2f)).scale(frag.length()).scale(frag.length()).scale(frag.length()).scale(3);
//        return frag.scale(3);
        return frag;
//        return Vec3.lerp(
//                new Vec3(0, 0, 0),
//                new Vec3(1, 1, 1),
//                (float) Math.min(1, Math.pow((Math.abs(direction.y - 0.5f) * 2f), 5))
//        );
    }
}
