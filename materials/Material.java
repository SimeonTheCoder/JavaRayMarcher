package materials;

import materials.textures.Texture;
import materials.textures.TextureBW;
import materials.textures.TextureRGB;
import math.Vec2;
import utils.MathUtils;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Material {
    public static final int ALBEDO = 0;
    public static final int DISPLACEMENT = 1;
    public static final int ROUGHNESS = 2;

    public Texture[] slots;
    public int[] bitDepth;

    public Vec2 tilingFactor;

    public Material clone () {
        Texture[] slotsCopy = new Texture[slots.length];
        int[] bitDepthCopy = new int[slots.length];

        Arrays.setAll(slotsCopy, i -> slots[i].clone());
        System.arraycopy(bitDepth, 0, bitDepthCopy, 0, bitDepthCopy.length);

        return new Material(
                slotsCopy, bitDepthCopy,
                new Vec2(
                        this.tilingFactor.x,
                        this.tilingFactor.y
                )
        );
    }

    public Material (BufferedImage[] slots, int[] bitDepth, Vec2 tilingFactor) {
        this.slots = new Texture[slots.length];

        for (int i = 0; i < slots.length; i ++) {
            if (bitDepth[i] == 24) {
                this.slots[i] = new TextureRGB(slots[i]);
            } else {
                this.slots[i] = new TextureBW(slots[i]);
            }
        }

        this.bitDepth = bitDepth;
        this.tilingFactor = tilingFactor;
    }

    public Material (Texture[] slots, int[] bitDepth, Vec2 tilingFactor) {
        this.slots = slots;

        this.bitDepth = bitDepth;
        this.tilingFactor = tilingFactor;
    }

    public float[] sampleTexture (int textureIndex, Vec2 uv) {
        uv.scaleInPlace(tilingFactor);

//        if(bitDepth[textureIndex] == 8) uv = uv.scale(1f/ 3f / 3f);

        uv.x = (float) Math.min(0.99, MathUtils.mod(uv.x, 1));
        uv.y = (float) Math.min(0.99, MathUtils.mod(uv.y, 1));

        int x = (int) (uv.x * slots[textureIndex].getWidth());
        int y = (int) (uv.y * slots[textureIndex].getHeight());

        int r = slots[textureIndex].getRed(x,y);
        int g = slots[textureIndex].getGreen(x,y);
        int b = slots[textureIndex].getBlue(x,y);

        return new float[]{
                r / 255f,
                g / 255f,
                b / 255f
        };
    }

    public float sampleTextureGrayscale (int textureIndex, Vec2 uv) {
        uv.scaleInPlace(tilingFactor);

//        if(bitDepth[textureIndex] == 8) uv = uv.scale(1f/ 3f / 3f);

        uv.x = (float) Math.max(0, Math.min(0.99, MathUtils.mod(uv.x, 1)));
        uv.y = (float) Math.max(0, Math.min(0.99, MathUtils.mod(uv.y, 1)));

        int x = (int) (uv.x * slots[textureIndex].getWidth());
        int y = (int) (uv.y * slots[textureIndex].getHeight());

        return slots[textureIndex].getRed(x, y) / 255f;
    }
}
