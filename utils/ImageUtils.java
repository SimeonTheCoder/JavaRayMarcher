package utils;

import java.awt.image.BufferedImage;

public class ImageUtils {
    public static BufferedImage clone (BufferedImage image, int scalingFactor) {
        BufferedImage clone = new BufferedImage(
                image.getWidth() / scalingFactor,
                image.getHeight() / scalingFactor,
                BufferedImage.TYPE_BYTE_GRAY
        );

        for (int i = 0; i < image.getHeight(); i += scalingFactor) {
            for (int j = 0; j < image.getWidth(); j += scalingFactor) {
                clone.setRGB(j / scalingFactor, i / scalingFactor, image.getRGB(j, i));
            }
        }

        return clone;
    }

    public static byte[][][] convert (BufferedImage image) {
        byte[][][] clone = new byte[image.getHeight()][image.getWidth()][3];

        for (int i = 0; i < image.getHeight(); i ++) {
            for (int j = 0; j < image.getWidth(); j ++) {
                int rgb = image.getRGB(j, i);

                byte red = (byte) ((rgb >> 16) & 0xFF);
                byte green = (byte) ((rgb >> 8) & 0xFF);
                byte blue = (byte) (rgb & 0xFF);

                clone[i][j][0] = red;
                clone[i][j][1] = green;
                clone[i][j][2] = blue;
            }
        }

        return clone;
    }

    public static byte[][] convertBW (BufferedImage image) {
        byte[][] clone = new byte[image.getHeight()][image.getWidth()];

        for (int i = 0; i < image.getHeight(); i ++) {
            for (int j = 0; j < image.getWidth(); j ++) {
                int rgb = image.getRGB(j, i);

                byte red = (byte) ((rgb >> 16) & 0xFF);
                byte green = (byte) ((rgb >> 8) & 0xFF);
                byte blue = (byte) (rgb & 0xFF);

                clone[i][j] = blue;
            }
        }

        return clone;
    }
}
