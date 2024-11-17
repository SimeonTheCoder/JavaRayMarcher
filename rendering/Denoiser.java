package rendering;

import java.awt.image.BufferedImage;

public class Denoiser {
    public static BufferedImage denoise (BufferedImage input) {
        BufferedImage copy = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = RenderingSettings.DENOISE_KERNEL_SIZE / 2; i < input.getHeight() - RenderingSettings.DENOISE_KERNEL_SIZE / 2; i ++) {
            for (int j = RenderingSettings.DENOISE_KERNEL_SIZE / 2; j < input.getWidth() - RenderingSettings.DENOISE_KERNEL_SIZE / 2; j ++) {
                int aveRed = 0, aveGreen = 0, aveBlue = 0;
                int red = 0, green = 0, blue = 0;

                int c = input.getRGB(j, i);

                red = (c >> 16) & 0xFF;
                green = (c >> 8) & 0xFF;
                blue = c & 0xFF;

                int divisor = 0;

                for (int m = 0; m < RenderingSettings.DENOISE_KERNEL_SIZE / 2; m ++) {
                    divisor += (RenderingSettings.DENOISE_KERNEL_SIZE - m * 2) * (RenderingSettings.DENOISE_KERNEL_SIZE - m * 2);

                    for (int k = -RenderingSettings.DENOISE_KERNEL_SIZE / 2 + m; k <= RenderingSettings.DENOISE_KERNEL_SIZE / 2 - m; k++) {
                        for (int l = -RenderingSettings.DENOISE_KERNEL_SIZE / 2 + m; l <= RenderingSettings.DENOISE_KERNEL_SIZE / 2 - m; l++) {
                            int color = input.getRGB(j + l, i + k);

                            aveRed += (color >> 16) & 0xFF;
                            aveGreen += (color >> 8) & 0xFF;
                            aveBlue += color & 0xFF;
                        }
                    }
                }

                aveRed /= divisor;
                aveGreen /= divisor;
                aveBlue /= divisor;

                int aveDifference = (Math.abs(aveRed - red) + Math.abs(aveGreen - green) + Math.abs(aveBlue - blue)) / 3;

                aveDifference = (int) Math.max(0, Math.min(255, Math.pow(aveDifference, 1.5)));

                red = (int) (aveRed * (aveDifference / 255f) + red * (1f - aveDifference / 255f));
                green = (int) (aveGreen * (aveDifference / 255f) + green * (1f - aveDifference / 255f));
                blue = (int) (aveBlue * (aveDifference / 255f) + blue * (1f - aveDifference / 255f));

                copy.setRGB(j, i, (red << 16) | (green << 8) | blue);
            }
        }

        return copy;
    }
}
