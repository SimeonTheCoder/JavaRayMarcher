package materials.textures;

import utils.Clone;

public interface Texture extends Clone<Texture> {
    int getWidth();
    int getHeight();

    int getBitDepth();

    int getRed(int x, int y);
    int getGreen(int x, int y);
    int getBlue(int x, int y);
}
