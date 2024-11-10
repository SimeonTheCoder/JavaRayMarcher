import math.Light;
import math.Vec3;
import rendering.Renderer;
import rendering.Window;
import scene.Scene;
import scene.primitives.Ground;
import scene.primitives.Sphere;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //Create scene
        Scene scene = new Scene();

        Random random = new Random();

//        scene.objects.add(
//                new Sphere(
//                        new Vec3(4, 5, 6),
//                        1.5f
//                )
//        );

//        scene.objects.add(
//                new Sphere(
//                        new Vec3(4, 5, 6),
//                        1.5f
//                )
//        );
//
//        scene.objects.add(
//                new Sphere(
//                        new Vec3(4, 5, 6),
//                        1.5f
//                )
//        );
//
//        scene.objects.add(
//                new Sphere(
//                        new Vec3(-2, 1, 10),
//                        1.5f
//                )
//        );
//
//        scene.objects.add(
//                new Sphere(
//                        new Vec3(2, 1, 10),
//                        1.5f
//                )
//        );
//
//        scene.objects.add(
//                new Sphere(
//                        new Vec3(0, 3, 10),
//                        1.5f
//                )
//        );

//        scene.lights.add(
//                new Light(
//                        new Vec3(-3f, 40f, 10f),
//                        50f,
//                        new Vec3(1, 1, 1),
//                        .5f
//                )
//        );
//
        scene.lights.add(
                new Light(
                        new Vec3(-3f, 4f, 10f),
                        50f,
                        new Vec3(1, 0, 0),
                        .5f
                )
        );

        scene.lights.add(
                new Light(
                        new Vec3(3f, 4f, 10f),
                        5f,
                        new Vec3(0, 0, 1),
                        .5f
                )
        );

        scene.lights.add(
                new Light(
                        new Vec3(0f, 3f, 12f),
                        5f,
                        new Vec3(0, 1, 0),
                        .5f
                )
        );

        scene.lights.add(
                new Light(
                        new Vec3(0f, 3f, 8f),
                        5f,
                        new Vec3(1, 0, 1),
                        .5f
                )
        );

//        scene.lights.add(
//                new Light(
//                        new Vec3(0, 1f, 10),
//                        0.5f,
//                        new Vec3(0, 1, 0)
//                )
//        );

        random.setSeed(42);

        for(int i = 0; i < 20; i ++) {
            scene.objects.add(
                new Sphere(
                        new Vec3(random.nextFloat() * 20 - 4, random.nextFloat() * 5, random.nextFloat() * 20 + 10),
                        random.nextFloat() * 3f
                )
            );
        }

        //Populate the scene with primitive objects
//        scene.objects.add(
//                new Sphere(
//                        new Vec3(0, 2, 10),
//                        1.5f
//                )
//        );

        scene.objects.add(
                new Ground(0)
        );

//        scene.objects.add(
//                new Ground(6, true)
//        );

//        scene.objects.add(
//                new Sphere(
//                        new Vec3(5, 5, 5),
//                        1.5f
//                )
//        );
//
//        scene.objects.add(
//                new Sphere(
//                        new Vec3(4, 4, 4),
//                        1f
//                )
//        );

//        scene.objects.add(
//                new Sphere(
//                        new Vec3(5, 6, 6),
//                        1f
//                )
//        );

        //Create window
        Window window = new Window(
                "Test",
                2560, 1440,
                new Renderer(scene)
        );
    }
}
