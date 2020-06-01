import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import java.io.File;
import java.io.IOException;

public class Render3D {

    static GLU glu;
    private Texture floorTex;
    private int distance = 10;

    public Render3D(GL2 gl) {
        glu = new GLU();

        gl.glEnable(GL.GL_TEXTURE_2D);
        try {
            String filename="src\\resources\\textures\\stone_floor_texture.jpg"; // the FileName to open
            floorTex = TextureIO.newTexture(new File( filename ),true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
    }

    public void floor(GL2 gl, double centerX, double centerZ) {

        float[] material = {0.8f,0.8f,0.8f,1.0f};

        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);

        floorTex.bind(gl);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);

        gl.glBegin(GL2.GL_QUADS);

        int startX = (int)centerX - distance, endX = (int)centerX + distance;
        int startZ = (int)centerZ - distance, endZ = (int)centerZ + distance;

        for(int x = startX; x < endX; ++x) {
            for(int z = startZ; z < endZ; ++z) {
                gl.glNormal3f(0.0f, 1.0f, 0.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f((x + 1), 0, z);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f((x + 1), 0, (z + 1));
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(x, 0, (z + 1));
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(x, 0, z);
            }
        }

        gl.glEnd();
    }

    public void renderAll(GLAutoDrawable glAutoDrawable, Player player) {

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        player.setView(glu);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50,Display.WINDOW_WIDTH / Display.WINDOW_HEIGHT,1,1000);

        floor(gl, player.getCamera().getPosition().getX(), player.getCamera().getPosition().getZ());
    }
}
