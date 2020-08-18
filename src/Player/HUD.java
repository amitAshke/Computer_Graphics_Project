package Player;

import Main.Display;
import Main.Render3D;
import Renderables.Renderable;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class HUD implements Renderable {

    private Texture healthTex;

    public HUD() {
        healthTex = Render3D.healthTex;
    }

    void glEnable2D(GL2 gl) {
        int[] vPort = new int[4];

        gl.glMatrixMode(GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glOrtho(0, Display.WINDOW_WIDTH, 0, Display.WINDOW_HEIGHT, -1, 1);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
    }

    void glDisable2D(GL2 gl) {
        gl.glMatrixMode(GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glPopMatrix();
    }

    public void render(GL2 gl) {

        int leftInitial = 20, upInitial = 540, width = 30, height = 30, leftOffset = 40;

        healthTex.enable(gl);
        healthTex.bind(gl);

        glEnable2D(gl);
        gl.glBegin(GL2.GL_QUADS);

        for (int i = 0; i < Player.hitPoints; ++i) {

            gl.glTexCoord2f(1, 0);
            gl.glVertex2d(leftInitial + width, upInitial);

            gl.glTexCoord2f(1, 1);
            gl.glVertex2d(leftInitial + width, upInitial + height);

            gl.glTexCoord2f(0, 1);
            gl.glVertex2d(leftInitial, upInitial + height);

            gl.glTexCoord2f(0, 0);
            gl.glVertex2d(leftInitial, upInitial);

            leftInitial += leftOffset;
        }

        gl.glEnd();
        glDisable2D(gl);
    }
}
