package rendering;

import math.Vec3;
import scene.Scene;
import math.Ray;

import java.util.Random;

public class Renderer {
    private float[] randomValues;
    int currRandom = 0;

    public Scene scene;

    public void generateRandomValues() {
        Random random = new Random();

        randomValues = new float[3000];

        for (int i = 0; i < randomValues.length; i ++ ) {
            randomValues[i] = random.nextFloat();
        }
    }

    public Renderer(Scene scene) {
        generateRandomValues();

        this.scene = scene;
    }

    public Renderer clone() {
        return new Renderer(this.scene);
    }

    public int mod(int x, int y) {
        return x - y * (x / y);
    }

    private float random() {
        return randomValues[mod(currRandom++, 3000)];
    }

    private Vec3 normal(Ray ray, float maxDistance) {
        float d1 = scene.getMaxDistance(ray.position.add(new Vec3(-0.01f, 0, 0))) - maxDistance;
        float d2 = scene.getMaxDistance(ray.position.add(new Vec3(0, -0.01f, 0))) - maxDistance;
        float d3 = scene.getMaxDistance(ray.position.add(new Vec3(0, 0, -0.01f))) - maxDistance;

        return new Vec3(d1, d2, d3).normalized();
    }

    private Vec3 randomOffset(float radius) {
        float randX = (random() - 0.5f) * 2f;
        float randY = (random() - 0.5f) * 2f;
        float randZ = (random() - 0.5f) * 2f;

        return new Vec3(randX, randY, randZ).normalized().scale(radius);
    }

    public Ray render(Ray ray, int currBounce, float quality) {
        Vec3 viewDir = new Vec3(ray.direction.x, ray.direction.y, ray.direction.z);
        Vec3 frag = new Vec3();

        for (int currIteration = 0; currIteration < RenderingSettings.ITERATIONS; currIteration ++) {
//            ray.position = new Vec3(mod(ray.position.x, 8), mod(ray.position.y, 8), mod(ray.position.z, 8));

            float maxDistance = scene.getMaxDistance(ray.position);

            if (maxDistance < RenderingSettings.COLLISION_THRESHOLD) {
                float originalDistance = ray.distanceTravelled;

                Vec3 normal = normal(ray, maxDistance);

                Vec3 originalPos = ray.position;

                for (int k = 0; k < scene.lights.size() * RenderingSettings.LIGHT_SAMPLES; k ++) {
                    float distanceToLight;

                    Vec3 lightColor;
                    float lightIntensity;

                    distanceToLight = originalPos.sub(scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).pos).length();

                    lightColor = scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).color;
                    lightIntensity = scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).intensity;

                    Vec3 randOffset = randomOffset(scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).radius);

                    ray.direction = originalPos.sub(scene.lights.get(k / RenderingSettings.LIGHT_SAMPLES).pos.add(randOffset)).inverse();
                    ray.position = originalPos.add(ray.direction.scale(0.5f));

                    ray.distanceTravelled = 0;

                    for (int j = 0; j < RenderingSettings.ITERATIONS * 1 * quality; j++) {
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

                        if (maxDis < RenderingSettings.COLLISION_THRESHOLD) {
//                            ray.color = scene.world(ray.position.normalized());
                            break;
                        }
                        if (ray.distanceTravelled > RenderingSettings.OUT_OF_BOUNDS) {
                            ray.color = scene.world(ray.position.normalized()).scale(5);
                            break;
                        }

                        ray.position = ray.position.add(
                                ray.direction.scale(maxDis)
                        );

                        ray.distanceTravelled += maxDis;
                    }
                }

                frag = frag.scale(0.2f / RenderingSettings.LIGHT_SAMPLES);

                float F0 = 0.04f;

                if(currBounce != -1) {
                    for (int k = 0; k < RenderingSettings.REFLECTION_SAMPLES * quality; k ++) {
                        Ray reflectionRay = new Ray();

                        float fresnel = (float) (F0 + (1 - F0) * Math.pow(1 - viewDir.dot(normal), 5));

                        Vec3 randOffset = randomOffset(RenderingSettings.ROUGHNESS);

                        reflectionRay.direction = normal.inverse().add(randOffset).normalized();
                        reflectionRay.position = originalPos.add(reflectionRay.direction);

                        Ray result = render(reflectionRay, currBounce - 1, quality);

//                        frag = new Vec3(fresnel);

                        frag = frag.add(
                                result.color.scale(1f / RenderingSettings.REFLECTION_SAMPLES * quality * (1 + fresnel * 1f))
                        );
                    }

                    frag.add(new Vec3(0.2f));
                }

                ray.color = new Vec3(
                        Math.max(0, frag.x/2f),
                        Math.max(0, frag.y/2f),
                        Math.max(0, frag.z/2f)
                );

                ray.distanceTravelled = originalDistance;

                if (currBounce != RenderingSettings.BOUNCES - 1) {
                    ray.color.scale(1f / ray.distanceTravelled / ray.distanceTravelled);
                }

                return ray;
            }

            if (ray.distanceTravelled > RenderingSettings.OUT_OF_BOUNDS) {
                ray.color = scene.world(ray.position.normalized());
                ray.distanceTravelled = 1;
                return ray;
            }

            ray.position = ray.position.add(
                    ray.direction.scale(maxDistance)
            );

            ray.distanceTravelled += maxDistance;
        }

        ray.color = scene.world(ray.position.normalized());
        return ray;
    }
}
