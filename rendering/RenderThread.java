package rendering;

import math.Ray;
import math.Vec2;
import math.Vec3;

import java.awt.image.BufferedImage;

public class RenderThread extends Thread{
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;

    private final int height;

    private final Vec3 playerCam;

    private final Renderer renderer;

    private final BufferedImage image;

    public RenderThread(BufferedImage image, Renderer renderer, int startX, int startY, int endX, int endY, Vec3 playerCam, int height) {
        this.startX = startX;
        this.startY = startY;

        this.endX = endX;
        this.endY = endY;

        this.height = height;

        this.playerCam = playerCam;
        this.renderer = renderer;

        this.image = image;
    }

    @Override
    public void run() {
        for (int i = startY; i < endY; i ++) {
            for (int j = startX; j < endX; j ++) {
//                if(RenderingSettings.RESOLUTION_SCALING != 1 && ThreadLocalRandom.current().nextFloat() > 0.03f * RenderingSettings.RESOLUTION_SCALING) continue;
//                if(RenderingSettings.RESOLUTION_SCALING == 1 && ThreadLocalRandom.current().nextFloat() > 0.1f) continue;

                float x = (j + 0f) / height;
                float y = 1 - (i + 0f) / height;

                //Create ray
                Ray curr = Ray.fromVec2(
                        new Vec2(x - 0.5f, y - 0.5f), 2,
                        playerCam
                );

                //Set current pixel color
                Vec3 frag = renderer.render(curr, RenderingSettings.BOUNCES - 1, 1f).scale(RenderingSettings.EXPOSURE);

                frag = frag.scale(1 + frag.length());

                frag = Vec3.lerp(frag, new Vec3((frag.x + frag.y + frag.z) / 3f), 1 - Math.min(1, Math.max(0, (frag.x + frag.y + frag.z) / 3f)));

                int currRed = frag.x < 0 ? 0 : frag.x > 1 ? 255 : (int) (frag.x * 255);
                int currGreen = frag.y < 0 ? 0 : frag.y > 1 ? 255 : (int) (frag.y * 255);
                int currBlue = frag.z < 0 ? 0 : frag.z > 1 ? 255 : (int) (frag.z * 255);

                if(j < image.getWidth() && i < image.getHeight()) image.setRGB(j, i, (currRed << 16) | (currGreen << 8) | currBlue);
            }
        }
    }
}
