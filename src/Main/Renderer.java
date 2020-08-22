package Main;

import Collision.Enemy;
import Player.Player;
import Renderables.Renderable;
import Renderables.WavefrontObject;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * This class is responsible for all of the rendering of the game.
 */
public class Renderer {

    private Texture instructions;

    public static GLU glu;
    public static Texture floorTex, ceilingTex, wallTex, projectileTex, dummyTex, lampTex, healthTex;

    // Variable used to save the models' IDs.
    public static int lampBaseModel, sphereModel, projectileModel, enemyModel;

    public Renderer(GL2 gl) {
        glu = new GLU();

        gl.glEnable(GL.GL_TEXTURE_2D);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);

        try {
            String filename="src\\resources\\textures\\stone_floor_texture.jpg";
            floorTex = TextureIO.newTexture(new File(filename),true);
            filename = "src\\resources\\textures\\metal_ceiling_tiles.jpg";
            ceilingTex = TextureIO.newTexture(new File(filename),true);
            filename = "src\\resources\\textures\\stone_wall.jpg";
            wallTex = TextureIO.newTexture(new File(filename),true);
            filename = "src\\resources\\models\\textures\\Dagger_1K_Diffuse.png";
            projectileTex = TextureIO.newTexture(new File(filename), true);
            filename = "src\\resources\\models\\textures\\rough.tif";
            dummyTex = TextureIO.newTexture(new File(filename), true);
            filename = "src\\resources\\models\\textures\\black.png";
            lampTex = TextureIO.newTexture(new File(filename), true);
            filename = "src\\resources\\textures\\health.png";
            healthTex = TextureIO.newTexture(new File(filename), true);
            filename = "src\\resources\\instructions.png";
            instructions = TextureIO.newTexture(new File(filename), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Renderer: Error during texture loading.");
            throw new RuntimeException(e);
        }

        try {
            // Set models IDs.
            projectileModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\Dagger.obj");
            enemyModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\Knight_Statue.obj");
            lampBaseModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\lampobj.obj");
            sphereModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\sphere.obj");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Renderer: Error during models loading.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Render the level.
     */
    public void renderLevel(GL2 gl, Level level) {
    float[] material = {0.8f,0.8f,0.8f,1.0f};

//        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

//        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
//        gl.glLoadIdentity();
//        player.setView(glu);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);

        for (Enemy enemy : Level.enemies) {
            enemy.render(gl);
        }

        for (Renderable renderable : level.getRenderables()) {
            renderable.render(gl);
        }

//        gl.glPopMatrix();
    }

    // Render player related objects.
    public void renderPlayer(GL2 gl, Player player) {

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

//        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
//        gl.glLoadIdentity();
        player.setView(glu);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);


        if (Player.specialProjectile != null) {
            Player.specialProjectile.render(gl);
        }

        for (Renderable projectile : Player.projectiles) {
            projectile.render(gl);
        }

        Player.hud.render(gl);
        
//        gl.glPopMatrix();
    }

    public void renderInstructions(GL2 gl, GLUT glut) {
        gl.glDisable(GL2.GL_LIGHTING);
        instructions.enable(gl);
        instructions.bind(gl);

        glEnable2D(gl);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(Display.WINDOW_WIDTH, 0);

        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(Display.WINDOW_WIDTH, Display.WINDOW_HEIGHT);

        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(0, Display.WINDOW_HEIGHT);

        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(0, 0);

        gl.glEnd();
        glDisable2D(gl);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    public void renderFpsCounter(GL2 gl, GLUT glut, double unprocessedSeconds) {
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glWindowPos2d(20, Display.WINDOW_HEIGHT - 60);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, (int) (1 / unprocessedSeconds) + " FPS");
        gl.glRasterPos2d(0, 0);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }

    public void renderResult(GL2 gl, GLUT glut, Level level) {
        gl.glLoadIdentity();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        gl.glWindowPos2d(Display.WINDOW_WIDTH / 2 - 50, Display.WINDOW_HEIGHT / 2);
        if (level == null) {
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "YOU WIN!");
        } else {
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "You Lose!");
        }
        gl.glWindowPos2d(Display.WINDOW_WIDTH / 2 - 70, Display.WINDOW_HEIGHT / 2 - 20);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Press R to restart");

        gl.glRasterPos2d(0, 0);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }

    public void renderNextFrame(GL2 gl, Level level) {
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        renderPlayer(gl, Display.levelManager.player);
        renderLevel(gl, level);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();
    }

    void glEnable2D(GL2 gl) {

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
}
