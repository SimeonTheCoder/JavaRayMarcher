package rendering;

import materials.textures.TextureRGB;
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

    public final TextureRGB image;
    public final BufferedImage mainImage;

    public RenderThread(TextureRGB image, BufferedImage mainImage, Renderer renderer, int startX, int startY, int endX, int endY, Vec3 playerCam, int height) {
        this.startX = startX;
        this.startY = startY;

        this.endX = endX;
        this.endY = endY;

        this.height = height;

        this.playerCam = playerCam;
        this.renderer = renderer;

        this.image = image;
        this.mainImage = mainImage;
    }

    // Narkowicz 2015, "ACES Filmic Tone Mapping Curve"
    // by dmnsgn
    private Vec3 aces(Vec3 x) {
        float a = 2.51f;
        float b = 0.03f;
        float c = 2.43f;
        float d = 0.59f;
        float e = 0.14f;

        return x.scale(x.scale(a).add(new Vec3(b))).div(x.scale(x.scale(c).add(new Vec3(d))).add(new Vec3(e)));
    }

    @Override
    public void run() {
        float fractX = 1f / height;
        float fractY = 1f / height;

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {

                float x = j * fractX;
                float y = 1 - (i * fractY);

                //Create ray
                Ray curr = Ray.fromVec2(
                        new Vec2(x - 0.5f, y - 0.5f), 2,
                        playerCam
                );

                //Set current pixel color
                Vec3 frag = renderer.render(curr, RenderingSettings.BOUNCES - 1, 1f).color.scale(RenderingSettings.EXPOSURE);

//                frag = frag.scale(1f / (0.155f + frag.length())).scale(1.019f);
                frag = aces(frag);
//                frag = frag.scale(1f / (1f + frag.length()));
                frag = new Vec3(Math.max(0, frag.x), Math.max(0, frag.y), Math.max(0, frag.z));
                frag = new Vec3((float) Math.pow(frag.x, 0.8), (float) Math.pow(frag.y, 0.8), (float) Math.pow(frag.z, 0.8));

//                frag = Vec3.lerp(frag, new Vec3((frag.x + frag.y + frag.z) / 3f), 1 - Math.min(1, Math.max(0, (frag.x + frag.y + frag.z) / 3f)));

                int currRed = frag.x < 0 ? 0 : frag.x > 1 ? 255 : (int) (frag.x * 255);
                int currGreen = frag.y < 0 ? 0 : frag.y > 1 ? 255 : (int) (frag.y * 255);
                int currBlue = frag.z < 0 ? 0 : frag.z > 1 ? 255 : (int) (frag.z * 255);

                if (j < image.getWidth() && i < image.getHeight())
                    image.image[i][j][0] = (byte) currRed;
                image.image[i][j][1] = (byte) currGreen;
                image.image[i][j][2] = (byte) currBlue;
//                (j, i, (currRed << 16) | (currGreen << 8) | currBlue);
            }

            renderer.generateRandomValues();
        }

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                mainImage.setRGB(
                        j, i,
                        (
                                ((image.image[i][j][0] & 0xFF) << 16) |
                                        ((image.image[i][j][1] & 0xFF) << 8) |
                                        (image.image[i][j][2] & 0xFF)
                        )
                );
            }
        }
    }
}
