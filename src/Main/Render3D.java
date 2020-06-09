package Main;

import Main.Display;
import Player.Player;
import Renderables.Renderable;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import java.util.List;

public class Render3D {

    static GLU glu;

    public Render3D(GL2 gl) {
        glu = new GLU();

        gl.glEnable(GL.GL_TEXTURE_2D);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
    }

    public void renderAllV2(GLAutoDrawable glAutoDrawable, Player player, List<Renderable> renderables) {
        float[] material = {0.8f,0.8f,0.8f,1.0f};

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        player.setView(glu);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50, Display.WINDOW_WIDTH / Display.WINDOW_HEIGHT, Player.HIT_RADIUS - 0.2,1000);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);

        gl.glBegin(GL2.GL_QUADS);

        for (Renderable renderable : renderables) {
            renderable.render(gl);
        }

        gl.glEnd();
    }
}
