package rendering;

import input.Keyboard;
import materials.textures.TextureRGB;
import math.Ray;
import math.Vec2;
import math.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Window extends JPanel {
    public boolean blocked = false;
    private boolean renderComplete = false;
    private boolean currIterationDone = false;

    private Vec3 playerCam = new Vec3(2.3999996f, 2.5999997f, 0);

    private int FRAME = 0;

    private JFrame frame;

    private Renderer renderer;

    private String title;
    private int width, height;

    private BufferedImage image;

    private RenderThread[] threads;

    public Window (String title, int width, int height, Renderer raymarchRenderer) {
        frame = new JFrame(title);

        this.renderer = raymarchRenderer;

        this.title = title;

        frame.setSize(width, height);

        this.width = width;
        this.height = height;

        this.image = new BufferedImage(
                this.width / RenderingSettings.RESOLUTION_SCALING,
                this.height / RenderingSettings.RESOLUTION_SCALING,
                BufferedImage.TYPE_INT_RGB
        );

        frame.add(this);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

        frame.addKeyListener(new Keyboard(this.playerCam, this));

        threads = new RenderThread[RenderingSettings.THREAD_COUNT];
    }

    public void refreshImage() {
        int sizeDifference = image.getWidth() / this.width * RenderingSettings.RESOLUTION_SCALING;

        BufferedImage copy = new BufferedImage(
                this.width / RenderingSettings.RESOLUTION_SCALING,
                this.height / RenderingSettings.RESOLUTION_SCALING,
                BufferedImage.TYPE_INT_RGB
        );

        for (int i = 0; i < copy.getWidth(); i ++) {
            for (int j = 0; j < copy.getHeight(); j ++) {
                copy.setRGB(i, j, image.getRGB(i * sizeDifference, j * sizeDifference));
            }
        }

        this.image = copy;
    }

    @Override
    public void paint(Graphics g) {
        FRAME ++;

        Random random = new Random();

        if(blocked) FRAME = 1;

        if (currIterationDone && RenderingSettings.RESOLUTION_SCALING != 1 && !blocked) {
            FRAME = 0;
            RenderingSettings.RESOLUTION_SCALING /= 2;

            for(int i = 0; i < RenderingSettings.THREAD_COUNT; i ++) if(threads[i] != null) threads[i].interrupt();

            BufferedImage copy = new BufferedImage(
                    this.width / RenderingSettings.RESOLUTION_SCALING,
                    this.height / RenderingSettings.RESOLUTION_SCALING,
                    BufferedImage.TYPE_INT_RGB
            );

            for(int i = 0; i < copy.getWidth(); i += 2) {
                for(int j = 0; j < copy.getHeight(); j += 2) {
                    copy.setRGB(i, j, image.getRGB(i >> 1, j >> 1));
                    copy.setRGB(i + 1, j, image.getRGB(i >> 1, j >> 1));
                    copy.setRGB(i, j + 1, image.getRGB(i >> 1, j >> 1));
                    copy.setRGB(i + 1, j + 1, image.getRGB(i >> 1, j >> 1));
                }
            }

            this.image = copy;

            int h = this.height / RenderingSettings.RESOLUTION_SCALING;
            int w = this.width / RenderingSettings.RESOLUTION_SCALING;

            for(int i = 0; i < RenderingSettings.THREAD_COUNT; i ++) {
                int tileX = i % 6;
                int tileY = i / 6;

                threads[i] = new RenderThread(
                        new TextureRGB(image),
                        image,
                        renderer.clone(),
                        tileX * w / 6,
                        tileY * h / 6,
                        (tileX + 1) * w / 6,
                        (tileY + 1) * h / 6,
                        playerCam,
                        h
                );

//                threads[i] = new RenderThread(image, renderer, i * w / RenderingSettings.THREAD_COUNT, 0, (i + 1) * w / RenderingSettings.THREAD_COUNT, h, random, playerCam);
                threads[i].start();
            }
        }

//        for (Light light : renderer.scene.lights) {
//            light.pos = playerCam;
//        }

//        ((Sphere) renderer.scene.objects.get(1)).pos.y += Math.sin(FRAME / 20f) / 20f;
//        ((Sphere) renderer.scene.objects.get(1)).pos.x -= Math.sin(FRAME / 60f) / 80f;

//        cameraPos.y += 0.1f;

        int h = this.height / RenderingSettings.RESOLUTION_SCALING;
        int w = this.width / RenderingSettings.RESOLUTION_SCALING;

        super.paint(g);

        if(!blocked) {
            boolean done = true;
            currIterationDone = true;

            for (RenderThread thread : threads) {
                if (thread != null && thread.isAlive()) {
                    done = false;
                    currIterationDone = false;
                    break;
                }
            }

//            System.out.print(done ? "DONE!!!" : "");

            if (done && !renderComplete && RenderingSettings.RESOLUTION_SCALING == 1) {
                //denoise
                image = Denoiser.denoise(image);

                renderComplete = true;
            }
        }

        if(blocked) {
            renderComplete = false;

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    if (RenderingSettings.RESOLUTION_SCALING != 1 && random.nextFloat() > 0.03f * RenderingSettings.RESOLUTION_SCALING)
                        continue;
                    if (RenderingSettings.RESOLUTION_SCALING == 1 && random.nextFloat() > 0.1f) continue;

                    float x = (j + 0f) / h;
                    float y = 1 - (i + 0f) / h;

                    //Create ray
                    Ray curr = Ray.fromVec2(
                            new Vec2(x - 0.5f, y - 0.5f), 2,
                            playerCam
                    );

                    //Set current pixel color
                    Vec3 frag = renderer.render(curr, RenderingSettings.BOUNCES - 2, 0.1f).color.scale(RenderingSettings.EXPOSURE);

                    frag = frag.scale(1 + frag.length());

                    frag = Vec3.lerp(frag, new Vec3((frag.x + frag.y + frag.z) / 3f), 1 - Math.min(1, Math.max(0, (frag.x + frag.y + frag.z) / 3f)));

                    int currRed = frag.x < 0 ? 0 : frag.x > 1 ? 255 : (int) (frag.x * 255);
                    int currGreen = frag.y < 0 ? 0 : frag.y > 1 ? 255 : (int) (frag.y * 255);
                    int currBlue = frag.z < 0 ? 0 : frag.z > 1 ? 255 : (int) (frag.z * 255);

                    image.setRGB(j, i, (currRed << 16) | (currGreen << 8) | currBlue);
                }
            }
        }

        g.drawImage(image, 0, 0, this.width, this.height, null);

        g.drawString(playerCam.x + " " + playerCam.y + " " + playerCam.z, 50, 50);

        repaint();
    }

    public String getTitle() {
        return title;
    }

    public void updateFrameTitle() {
        this.frame.setTitle(this.title);
    }

    public void setTitle(String title) {
        this.title = title;
        this.updateFrameTitle();
    }

    public int getWidth() {
        return width;
    }

    public void updateFrameSize() {
        this.frame.setSize(this.width, this.height);
    }

    public void setWidth(int xSize) {
        this.width = xSize;

        this.updateFrameSize();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int ySize) {
        this.height = ySize;

        this.updateFrameSize();
    }
}
