package Renderables;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;

public class FloorTile implements Renderable {
    private float x1, x2, z1, z2;
    private Texture floorTex;

    public FloorTile(float x1, float z1) {
        this.x1 = x1;
        this.x2 = x1 + 1;
        this.z1 = z1;
        this.z2 = z1 + 1;

        try {
            String filename="src\\resources\\textures\\stone_floor_texture.jpg";
            floorTex = TextureIO.newTexture(new File( filename ),true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void render(GL2 gl) {
        int texID = floorTex.getTextureObject(gl), texTarget = floorTex.getTarget();
        gl.glBindTexture(texTarget, texID);

        gl.glNormal3f(0, 1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, 0, z1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(x2, 0, z2);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(x1, 0, z2);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, 0, z1);
        gl.glBindTexture(texTarget, 0);
//        gl.glDeleteTextures(1, new int[]{texID}, 0);
    }
}
