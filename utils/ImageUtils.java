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

                clone[i][j] = (byte) ((red + green + blue) / 3);
            }
        }

        return clone;
    }

    public static BufferedImage stitch (BufferedImage[] tiles, int[] startX, int[] startY, int[] endX, int[] endY, int targetWidth, int targetHeight) {
        BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        for (int currTile = 0; currTile < tiles.length; currTile ++) {
            for (int i = startY[currTile]; i < endY[currTile]; i ++) {
                for (int j = startX[currTile]; j < endX[currTile]; j ++) {
                    int ox = j - startX[currTile];
                    int oy = i - startY[currTile];

                    result.setRGB( j, i, tiles[currTile].getRGB(ox, oy) );
                }
            }
        }

        return result;
    }
}
