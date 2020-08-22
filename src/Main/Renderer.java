package Main;

import Collision.Enemy;
import Player.Player;
import Renderables.Renderable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for all of the rendering of the game.
 */
public class Renderer {

    public static GLU glu;
    public static Texture floorTex, ceilingTex, wallTex, projectileTex, dummyTex, lampTex, healthTex, crosshairTex;

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
            filename = "src\\resources\\textures\\crosshair.png";
            crosshairTex = TextureIO.newTexture(new File(filename), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Render3D: Error during texture loading.");
            throw new RuntimeException(e);
        }
    }

    // Render level related objects.
    public void renderLevel(GL2 gl, List<Renderable> renderables) {
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

        for (Renderable renderable : renderables) {
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
}
