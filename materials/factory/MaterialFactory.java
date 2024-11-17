package materials.factory;

import materials.Material;
import materials.textures.Texture;
import materials.textures.TextureBW;
import materials.textures.TextureRGB;
import math.Vec2;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MaterialFactory {
    public static Material create(String name) throws IOException {
        return new Material(
                new Texture[]{
                        new TextureRGB(ImageIO.read(new File("materials/" + name + "_1K-JPG_Color.jpg"))),
                        new TextureBW(ImageIO.read(new File("materials/" + name + "_1K-JPG_Displacement.jpg"))),
                        new TextureBW(ImageIO.read(new File("materials/" + name + "_1K-JPG_Roughness.jpg")))
                },
                new int[]{ 24, 8, 8 },
                new Vec2(1f, 1f)
        );
    }

    public static Material create(String name, Vec2 tilingFactor) throws IOException {
        return new Material(
                new Texture[]{
                        new TextureRGB(ImageIO.read(new File("materials/" + name + "_1K-JPG_Color.jpg"))),
                        new TextureBW(ImageIO.read(new File("materials/" + name + "_1K-JPG_Displacement.jpg"))),
                        new TextureBW(ImageIO.read(new File("materials/" + name + "_1K-JPG_Roughness.jpg")))
                },
                new int[]{ 24, 8, 8 },
                tilingFactor
        );
    }
}
