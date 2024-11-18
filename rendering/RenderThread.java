package rendering;

import math.Ray;
import math.Vec2;
import math.Vec3;

import java.awt.image.BufferedImage;

public class RenderThread extends Thread {
    public final int startX;
    public final int startY;
    public final int endX;
    public final int endY;

    private final int height;

    private final Vec3 playerCam;

    private final Renderer renderer;

    public final BufferedImage tile;

    public RenderThread(BufferedImage mainImage, Renderer renderer, int startX, int startY, int endX, int endY, Vec3 playerCam, int height) {
        this.startX = startX;
        this.startY = startY;

        this.endX = endX;
        this.endY = endY;

        this.height = height;

        this.playerCam = playerCam;
        this.renderer = renderer;

        this.tile = mainImage;
    }

    // Narkowicz 2015, "ACES Filmic Tone Mapping Curve"
    // by dmnsgn
    private Vec3 aces(Vec3 x) {
        float a = 2.51f;
        float b = 0.03f;
        float c = 2.43f;
        float d = 0.59f;
        float e = 0.14f;

        return x.scale(
                x.scale(a)
                        .addInPlace(new Vec3(b)))
                .div(
                        x.scale(
                                x.scale(c)
                                        .addInPlace(new Vec3(d))
                        )
                                .addInPlace(new Vec3(e)));
    }

    @Override
    public void run() {
        float fractX = 1f / height;
        float fractY = 1f / height;

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                float x = j * fractX;
                float y = 1 - (i * fractY);

                Ray curr = Ray.fromVec2(
                        new Vec2(x - 0.5f, y - 0.5f), 2,
                        playerCam
                );

                Vec3 frag = renderer.render(curr, RenderingSettings.BOUNCES - 1, 1f).color.scale(RenderingSettings.EXPOSURE);

                frag = aces(frag);
                frag = new Vec3((float) Math.pow(frag.x, 0.8), (float) Math.pow(frag.y, 0.8), (float) Math.pow(frag.z, 0.8));

                int currRed = frag.x < 0 ? 0 : frag.x > 1 ? 255 : (int) (frag.x * 255);
                int currGreen = frag.y < 0 ? 0 : frag.y > 1 ? 255 : (int) (frag.y * 255);
                int currBlue = frag.z < 0 ? 0 : frag.z > 1 ? 255 : (int) (frag.z * 255);

                tile.setRGB(
                        j - startX, i - startY,
                        ((currRed << 16) | (currGreen << 8) | (currBlue))
                );
            }

            renderer.generateRandomValues();
        }
    }
}
