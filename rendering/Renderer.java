package rendering;

import materials.Material;
import math.Vec3;
import scene.Scene;
import math.Ray;
import scene.primitives.Primitive;

import java.util.Random;

public class Renderer {
    private float[] randomValues;
    int currRandom = 0;

    private static final float SAMPLE_NORM = 0.2f / RenderingSettings.LIGHT_SAMPLES;
    private static final float REFLECTIVITY = 0.04f;

    public Material[] materials;

    public Scene scene;

    public void generateRandomValues() {
        Random random = new Random();

        randomValues = new float[3000];

        for (int i = 0; i < randomValues.length; i ++ )
            randomValues[i] = random.nextFloat();
    }

    public Renderer(Scene scene, Material[] materials) {
        generateRandomValues();
        this.materials = materials;

        this.scene = scene;
    }

    public Renderer clone() {
        Material[] materialsCloned = new Material[materials.length];

        for (int mIndex = 0; mIndex < materials.length; mIndex ++)
            materialsCloned[mIndex] = materials[mIndex].clone();

        return new Renderer(this.scene.clone(), materialsCloned);
    }

    public int mod(int x, int y) {
        return x - y * (x / y);
    }

    private float random() {
        return randomValues[mod(currRandom++, 3000)];
    }

    private Vec3 normal(Ray ray, float maxDistance) {
        float d1 = scene.getMaxDistance(ray.position.add(new Vec3(-0.01f, 0, 0)), materials) - maxDistance;
        float d2 = scene.getMaxDistance(ray.position.add(new Vec3(0, -0.01f, 0)), materials) - maxDistance;
        float d3 = scene.getMaxDistance(ray.position.add(new Vec3(0, 0, -0.01f)), materials) - maxDistance;

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
//            ray.position = new Vec3(MathUtils.mod(ray.position.x, 10), MathUtils.mod(ray.position.y, 10), MathUtils.mod(ray.position.z, 10));

            float maxDistance = scene.getMaxDistanceApprox(ray.position);

            if (maxDistance < RenderingSettings.COLLISION_THRESHOLD) {
                maxDistance = scene.getMaxDistance(ray.position, materials);

                if (maxDistance > RenderingSettings.COLLISION_THRESHOLD) {
                    ray.position.addInPlace(ray.direction.scale(maxDistance));
                    ray.distanceTravelled += maxDistance;

                    continue;
                }

                float originalDistance = ray.distanceTravelled;

                Vec3 normal = normal(ray, maxDistance);

                Vec3 originalPos = ray.position;

                Primitive source = scene.closestObject(ray.position);

                for (int k = 0; k < scene.lights.length * RenderingSettings.LIGHT_SAMPLES; k ++) {
                    float distanceToLight = originalPos.sub(scene.lights[k / RenderingSettings.LIGHT_SAMPLES].pos).length();

                    Vec3 lightColor = scene.lights[k / RenderingSettings.LIGHT_SAMPLES].color;
                    float lightIntensity = scene.lights[k / RenderingSettings.LIGHT_SAMPLES].intensity;

                    Vec3 randOffset = randomOffset(scene.lights[k / RenderingSettings.LIGHT_SAMPLES].radius);

                    ray.direction = originalPos.sub(scene.lights[k / RenderingSettings.LIGHT_SAMPLES].pos.add(randOffset)).inverse();
                    ray.position = originalPos.add(ray.direction.scale(0.03f));

                    ray.distanceTravelled = 0;

                    for (int j = 0; j < RenderingSettings.ITERATIONS * 1 * quality; j++) {
//                    ray.position = new Vec3(MathUtils.mod(ray.position.x, 10), MathUtils.mod(ray.position.y, 10), MathUtils.mod(ray.position.z, 10));
                        float maxDis = scene.getMaxDistance(ray.position, materials);

                        if (maxDis > distanceToLight - ray.distanceTravelled && scene.closestObject(ray.position) != source) {
                            Primitive closest = scene.closestObject(originalPos);

                            Material closestMat = materials[closest.getMaterial()];

                            frag = frag.add(
                                    lightColor
                                            .scale(lightIntensity)
                                            .scale(1f / distanceToLight / distanceToLight)
                                            .scale(Math.max(Material.ALBEDO, normal.dot(ray.direction.inverse())))
                                            .scale(closestMat.sampleTexture(Material.ALBEDO, closest.getUV(originalPos)))
                            );
                            break;
                        }

                        if (maxDis < RenderingSettings.COLLISION_THRESHOLD && scene.closestObject(ray.position) != source) break;

                        if (ray.distanceTravelled > RenderingSettings.OUT_OF_BOUNDS) {
                            ray.color = scene.world(ray.position.normalized()).scaleInPlace(1);
                            break;
                        }

                        ray.position.addInPlace(ray.direction.scale(maxDis));
                        ray.distanceTravelled += maxDis;
                    }
                }

                frag.scaleInPlace(SAMPLE_NORM);

                if(currBounce != -1) {
                    Primitive closest = scene.closestObject(originalPos);
                    Material closestMat = materials[closest.getMaterial()];

                    for (int k = 0; k < RenderingSettings.REFLECTION_SAMPLES * quality; k ++) {
                        Ray reflectionRay = new Ray();

                        float fresnel = (float) (REFLECTIVITY + (1 - REFLECTIVITY) * Math.pow(1 - viewDir.dot(normal), 5));

//                        Vec3 randOffset = randomOffset(0);
                        Vec3 randOffset = randomOffset(closestMat.sampleTextureGrayscale(Material.ROUGHNESS, closest.getUV(originalPos)));

                        reflectionRay.direction = normal.inverse().add(randOffset).normalized();
                        reflectionRay.position = originalPos.add(reflectionRay.direction).scaleInPlace(1f);

                        Ray result = render(reflectionRay, currBounce - 1, quality * 0.8f);

                        frag = frag.add(
                                result.color.scaleInPlace(1f / RenderingSettings.REFLECTION_SAMPLES * quality * (1f + fresnel))
                                        .scaleInPlace(closestMat.sampleTexture(Material.ALBEDO, closest.getUV(originalPos)))
//                                        .scaleInPlace(Math.min(1, 1f / (result.distanceTravelled) / (result.distanceTravelled)))
                        );
                    }
                }

                ray.color = frag;

                ray.distanceTravelled = originalDistance;

//                ray.color.scaleInPlace(currBounce == RenderingSettings.BOUNCES - 1 ? 1 : 1f / ray.distanceTravelled / ray.distanceTravelled);

                return ray;
            }

            if (ray.distanceTravelled > RenderingSettings.OUT_OF_BOUNDS) {
                ray.color = scene.world(ray.position.normalized());
                ray.distanceTravelled = 1;
                return ray;
            }

            ray.position.addInPlace(
                    ray.direction.scale(maxDistance)
            );

            ray.distanceTravelled += maxDistance;
        }

        ray.color = scene.world(ray.position.normalized());
        return ray;
    }
}
