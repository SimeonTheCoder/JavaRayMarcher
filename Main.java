import materials.Material;
import materials.factory.MaterialFactory;
import math.Light;
import math.Vec3;
import rendering.Renderer;
import rendering.Window;
import scene.Scene;
import scene.primitives.Ground;
import scene.primitives.Primitive;
import scene.primitives.Sphere;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        Primitive[] objects = new Primitive[21];

        Random random = new Random();

        random.setSeed(42);

        for(int i = 0; i < 20; i ++) {
            objects[i] = new Sphere (
                            new Vec3(
                                    random.nextFloat() * 20 - 4,
                                    random.nextFloat() * 5,
                                    random.nextFloat() * 20 + 10
                            ),
                            random.nextFloat() * 3f
            ).setMaterial(0);
        }

        objects[20] = new Ground(0).setMaterial(1);

        Light[] lights = new Light[]{
//                new Light( new Vec3(20f, 5f, 10f), 400f, new Vec3(1, 0.5f, 0f), 2f ),
//                new Light( new Vec3(-3f, 4f, 10f), 50f, new Vec3(1, 0, 0), .5f ),
//                new Light( new Vec3(3f, 4f, 10f), 5f, new Vec3(0, 0, 1), .5f ),
//                new Light( new Vec3(0f, 3f, 12f), 5f, new Vec3(0, 1, 0), .5f ),
//                new Light( new Vec3(0f, 3f, 8f), 5f, new Vec3(1, 0, 1), .5f )
        };

        //Create scene
        Scene scene = new Scene(
                objects, lights,
                ImageIO.read(new File("venice_sunset_4k.png"))
        );

        Material[] materials = new Material[]{
                MaterialFactory.create("brick/Bricks094"),
                MaterialFactory.create("sand/Ground054"),
        };

        //Create window
        Window window = new Window(
                "Test",
                2560, 1440,
                new Renderer(scene, materials)
        );
    }
}
