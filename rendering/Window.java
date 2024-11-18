package rendering;

import input.Keyboard;
import materials.textures.TextureRGB;
import math.Vec3;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends JPanel {
    public boolean blocked = false;

    private final Vec3 playerCam = new Vec3(2.3999996f, 2.5999997f, 0);

    private final JFrame frame;

    private final Renderer renderer;

    private final int width;
    private final int height;

    private final RenderThread[] threads;

    private boolean done = true;
    private boolean renderComplete = false;

    private BufferedImage finalImage;

    public Window(String title, int width, int height, Renderer raymarchRenderer) {
        frame = new JFrame(title);

        this.renderer = raymarchRenderer;

        frame.setSize(width, height);

        this.width = width;
        this.height = height;

        frame.add(this);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

        frame.addKeyListener(new Keyboard(this.playerCam, this));

        threads = new RenderThread[RenderingSettings.THREAD_COUNT];

        threadInit();
    }

    public void threadInit() {
        int h = this.height;
        int w = this.width;

        for (int i = 0; i < RenderingSettings.THREAD_COUNT; i++)
            if (threads[i] != null) threads[i].interrupt();

        for (int i = 0; i < RenderingSettings.THREAD_COUNT; i++) {
            int tileX = i % 6;
            int tileY = i / 6;

            threads[i] = new RenderThread(
                    new BufferedImage(
                            (tileX + 1) * w / 6 - tileX * w / 6,
                            (tileY + 1) * h / 6 - tileY * h / 6,
                            BufferedImage.TYPE_INT_RGB
                    ),
                    renderer.clone(),
                    tileX * w / 6,
                    tileY * h / 6,
                    (tileX + 1) * w / 6,
                    (tileY + 1) * h / 6,
                    playerCam.clone(),
                    h
            );
        }

        for (int i = 0; i < RenderingSettings.THREAD_COUNT; i++)
            threads[i].start();
    }

    @Override
    public void paint(Graphics g) {
        done = true;

        if (!renderComplete) {
            for (int i = 0; i < threads.length; i++) {
                int tileX = i % 6;
                int tileY = i / 6;

                if (threads[i] == null) {
                    done = false;
                    continue;
                }

                g.drawImage(threads[i].tile,
                        tileX * this.width / 6,
                        tileY * this.height / 6,
                        (tileX + 1) * this.width / 6 - tileX * this.width / 6,
                        (tileY + 1) * this.height / 6 - tileY * this.height / 6,
                        null);

                if (threads[i].isAlive()) done = false;
            }
        } else {
            g.drawImage(finalImage, 0, 0, this.width, this.height, null);
        }

        if (done && !renderComplete) {
            BufferedImage[] tiles = new BufferedImage[RenderingSettings.THREAD_COUNT];
            int[] startX = new int[RenderingSettings.THREAD_COUNT], endX = new int[RenderingSettings.THREAD_COUNT];
            int[] startY = new int[RenderingSettings.THREAD_COUNT], endY = new int[RenderingSettings.THREAD_COUNT];

            for (int i = 0; i < threads.length; i ++) {
                tiles[i] = threads[i].tile;

                startX[i] = threads[i].startX;
                startY[i] = threads[i].startY;
                endX[i] = threads[i].endX;
                endY[i] = threads[i].endY;
            }

            finalImage = ImageUtils.stitch (
                    tiles, startX, startY, endX, endY, this.width, this.height
            );

            File outputfile = new File("render.jpg");

            try {
                ImageIO.write(finalImage, "jpg", outputfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            finalImage = Denoiser.denoise(stitched);

            renderComplete = true;
        }

//        g.drawString(playerCam.x + " " + playerCam.y + " " + playerCam.z, 50, 50);

        repaint();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
