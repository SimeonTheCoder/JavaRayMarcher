package materials.textures;

import utils.ImageUtils;

import java.awt.image.BufferedImage;

public class TextureBW implements Texture{
    public final byte[][] image;

    private final int width;
    private final int height;

    public TextureBW(BufferedImage image) {
        this.image = ImageUtils.convertBW(image);

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public TextureBW(byte[][] image) {
        this.image = image;

        this.width = image[0].length;
        this.height = image.length;
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
        return 8;
    }

    @Override
    public int getRed(int x, int y) {
        return image[y][x] & 0xFF;
    }

    @Override
    public int getGreen(int x, int y) {
        return image[y][x] & 0xFF;
    }

    @Override
    public int getBlue(int x, int y) {
        return image[y][x] & 0xFF;
    }

    @Override
    public Texture clone() {
        byte[][] clone = new byte[this.height][this.width];

        for (int i = 0; i < this.height; i ++) {
            System.arraycopy(image[i], 0, clone[i], 0, this.width);
        }

        return new TextureBW(clone);
    }
}
