package materials.textures;

import utils.ImageUtils;

import java.awt.image.BufferedImage;

public class TextureRGB implements Texture {
    public final byte[][][] image;

    private final int width;
    private final int height;

    public TextureRGB(BufferedImage image) {
        this.image = ImageUtils.convert(image);

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public TextureRGB(byte[][][] image) {
        this.image = image;

        this.width = image[0].length;
        this.height = image.length;
    }

    @Override
    public Texture clone() {
        byte[][][] clone = new byte[this.height][this.width][3];

        for (int i = 0; i < this.height; i ++) {
            for (int j = 0; j < this.width; j ++) {
                System.arraycopy(image[i][j], 0, clone[i][j], 0, 3);
            }
        }

        return new TextureRGB(clone);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getBitDepth() {
        return 24;
    }

    @Override
    public int getRed(int x, int y) {
        return image[y][x][0] & 0xFF;
    }

    @Override
    public int getGreen(int x, int y) {
        return image[y][x][1] & 0xFF;
    }

    @Override
    public int getBlue(int x, int y) {
        return image[y][x][2] & 0xFF;
    }
}
