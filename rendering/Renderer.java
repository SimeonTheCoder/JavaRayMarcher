package rendering;

import math.Vec3;
import scene.Scene;
import math.Ray;

import java.util.Random;

public class Renderer {


    public Scene scene;

    public Renderer(Scene scene) {
        this.scene = scene;
    }

    public static float mod(float x, float y) {
        return x - y * (float)Math.floor(x / y);
    }

    public Vec3 render(Ray ray, int frame, float type) {
        Random random = new Random();

//        frame = 1;

        float lightX = (float) Math.cos(frame / 10f);
        float lightY = (float) Math.sin(frame / 10f);

        Vec3 lightDir = new Vec3(lightX, 0.3f, lightY);
        Vec3 originalDir = ray.direction;

        Vec3 frag = new Vec3();

        for (int currIteration = 0; currIteration < RenderingSettings.ITERATIONS; currIteration ++) {
//            ray.position = new Vec3(mod(ray.position.x, 8), mod(ray.position.y, 8), mod(ray.position.z, 8));

            float maxDistance = scene.getMaxDistance(ray.position);

            if (maxDistance < RenderingSettings.COLLISION_THRESHOLD) {
                float d1 = scene.getMaxDistance(ray.position.add(new Vec3(-0.01f, 0, 0))) - maxDistance;
                float d2 = scene.getMaxDistance(ray.position.add(new Vec3(0, -0.01f, 0))) - maxDistance;
                float d3 = scene.getMaxDistance(ray.position.add(new Vec3(0, 0, -0.01f))) - maxDistance;

                Vec3 normal = new Vec3(d1, d2, d3).normalized();

                Vec3 originalPos = ray.position;

                for (int k = 0; k < scene.lights.size() * RenderingSettings.LIGHT_SAMPLES; k ++) {
                    float distanceToLight;

                    Vec3 lightColor;
                    float lightIntensity;

                    distanceToLight = originalPos.sub(scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).pos).length();

                    lightColor = scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).color;
                    lightIntensity = scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).intensity;

                    float randX = (float) ((random.nextFloat() - 0.5) * 2 * scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).radius);
                    float randY = (float) ((random.nextFloat() - 0.5) * 2 * scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).radius);
                    float randZ = (float) ((random.nextFloat() - 0.5) * 2 * scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).radius);

                    ray.direction = originalPos.sub(scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).pos.add(new Vec3(randX, randY, randZ))).inverse();
//                    ray.direction = lightDir.normalized();
                    ray.position = originalPos.add(ray.direction.scale(0.5f));

                    ray.distanceTravelled = 0;

                    for (int j = 0; j < RenderingSettings.ITERATIONS * 1 * type; j++) {
//                    ray.position = new Vec3(mod(ray.position.x, 8), mod(ray.position.y, 8), mod(ray.position.z, 8));
                        float maxDis = scene.getMaxDistance(ray.position);

                        if (maxDis > distanceToLight - ray.distanceTravelled) {
                            frag = frag.add(
                                    lightColor
                                            .scale(lightIntensity)
                                            .scale(1f / distanceToLight / distanceToLight)
                                            .scale(Math.max(0, normal.dot(ray.direction.inverse())))
                            );
                            break;
                        }

                        if (maxDis < RenderingSettings.COLLISION_THRESHOLD) break;

                        if (ray.distanceTravelled > RenderingSettings.OUT_OF_BOUNDS) {
//                            frag = frag.add(WORLD);
                            break;
                        }

                        ray.position = ray.position.add(
                                ray.direction.scale(maxDis)
//                                ray.direction.scale(0.03f)
                        );

//                        ray.distanceTravelled += 0.03f;
                        ray.distanceTravelled += maxDis;
                    }
                }

                frag = frag.scale(0.2f / RenderingSettings.LIGHT_SAMPLES);

                if(frame != -1) {
                    for (int k = 0; k < RenderingSettings.LIGHT_SAMPLES / 4 * type; k ++) {
                        Ray reflectionRay = new Ray();

                        float randX = (float) ((random.nextFloat() - 0.5) * 2 * RenderingSettings.ROUGHNESS);
                        float randY = (float) ((random.nextFloat() - 0.5) * 2 * RenderingSettings.ROUGHNESS);
                        float randZ = (float) ((random.nextFloat() - 0.5) * 2 * RenderingSettings.ROUGHNESS);

                        reflectionRay.direction = normal.inverse().add(new Vec3(randX, randY, randZ)).normalized();
                        reflectionRay.position = originalPos.add(reflectionRay.direction);

//                    frag = frag.add(render(reflectionRay, frame - 1).scale(-normal.dot(originalDir)));
                        frag = frag.add(render(reflectionRay, frame - 1, type).scale(1f / RenderingSettings.LIGHT_SAMPLES * 4 * type));
                    }
                }

//                return normal;

                return new Vec3(
                        Math.max(0, frag.x/2f),
                        Math.max(0, frag.y/2f),
                        Math.max(0, frag.z/2f)
                );
            }

            if (ray.distanceTravelled > RenderingSettings.OUT_OF_BOUNDS) return RenderingSettings.WORLD;

            ray.position = ray.position.add(
                    ray.direction.scale(maxDistance)
            );

            ray.distanceTravelled += maxDistance;
        }

        return RenderingSettings.WORLD;
    }
}
