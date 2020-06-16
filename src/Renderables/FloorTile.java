package Renderables;

import Collision.Collidable;
import LinearAlgebra.Vectors.Vector3D;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;

public class FloorTile implements Renderable, Collidable {
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

    public float getX1() {
        return x1;
    }

    public float getX2() {
        return x2;
    }

    public float getZ1() {
        return z1;
    }

    public float getZ2() {
        return z2;
    }

    public void render(GL2 gl) {
        int texID = floorTex.getTextureObject(gl), texTarget = floorTex.getTarget();
//        gl.glBindTexture(texTarget, texID);
        floorTex.enable(gl);
        floorTex.bind(gl);

        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3f(0, 1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, 0, z1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(x2, 0, z2);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(x1, 0, z2);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, 0, z1);

        gl.glEnd();

//        gl.glBindTexture(texTarget, 0);
//        gl.glDeleteTextures(1, new int[]{texID}, 0);
//        floorTex.disable(gl);
    }

    public boolean isCollidingWithPlayer(Vector3D playerPosition) {
        return false;
    }

    public Vector3D handlePlayerCollision(Vector3D newPosition) {
        return newPosition;
    }

    public boolean isCollidingWithProjectile(Vector3D position, double length, double radius, Vector3D direction) {
        Vector3D tip = position.scaleAdd(length, direction);

        double x = Math.max(x1, Math.min(tip.getX(), x2));
        double y = Math.max(-2, Math.min(tip.getY(), 0));
        double z = Math.max(z1, Math.min(tip.getZ(), z2));

        double distance = Math.sqrt((x - tip.getX()) * (x - tip.getX()) +
                (y - tip.getY()) * (y - tip.getY()) +
                (z - tip.getZ()) * (z - tip.getZ()));

        return distance < radius;
    }
}
