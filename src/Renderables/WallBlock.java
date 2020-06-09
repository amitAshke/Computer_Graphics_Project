package Renderables;

import Collision.Collidable;
import LinearAlgebra.Vectors.Vector3D;
import Player.Player;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;

public class WallBlock implements Renderable, Collidable {
    private float x1, x2, z1, z2;
    private Texture wallTex;

    public WallBlock(float x1, float z1) {
        this.x1 = x1;
        this.x2 = x1 + 1;
        this.z1 = z1;
        this.z2 = z1 + 1;

        try {
            String filename="src\\resources\\textures\\brick_wall_texture.jpg";
            wallTex = TextureIO.newTexture(new File( filename ),true);
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
        int texID = wallTex.getTextureObject(gl), texTarget = wallTex.getTarget();
        gl.glBindTexture(texTarget, texID);
        gl.glNormal3f(0,0,1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, 0, z1);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x2, 2, z1);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x1, 2, z1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, 0, z1);

        gl.glNormal3f(0,0,1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, 0, z2);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x2, 2, z2);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x1, 2, z2);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, 0, z2);

        gl.glNormal3f(0,0,1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x1, 0, z2);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x1, 2, z2);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x1, 2, z1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, 0, z1);

        gl.glNormal3f(0,0,1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, 0, z2);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x2, 2, z2);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x2, 2, z1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x2, 0, z1);


        gl.glBindTexture(texTarget, 0);
//        gl.glDeleteTextures(1, new int[]{texID}, 0);
    }

    public boolean isCollidingWithPlayer(Vector3D playerPosition) {
        double x_Position = playerPosition.getX(), z_Position = playerPosition.getZ();
        double x_Distance = Math.abs(x1 + 0.5 - x_Position), z_Distance = Math.abs(z1 + 0.5 - z_Position);

        if (x_Distance > (0.5 + Player.HIT_RADIUS)) { return false; }
        if (z_Distance > (0.5 + Player.HIT_RADIUS)) { return false; }

        if (x_Distance <= 0.5) { return true; }
        if (z_Distance <= 0.5) { return true; }

        double cornerDistance_sq = Math.pow(x_Distance - 0.5, 2) + Math.pow(z_Distance - 0.5, 2);

        return (cornerDistance_sq <= Math.pow(Player.HIT_RADIUS, 2));

//        return x_Position > x1 - Player.HIT_RADIUS && x_Position < x2 + Player.HIT_RADIUS &&
//                z_Position > z1 - Player.HIT_RADIUS && z_Position < z2 + Player.HIT_RADIUS;
    }

    public int getPlayerCollisionCode(Vector3D playerPosition) {
        double x_Position = playerPosition.getX(), z_Position = playerPosition.getZ();
        double x_Distance = Math.abs(x1 + 0.5 - x_Position), z_Distance = Math.abs(z1 + 0.5 - z_Position);

        if (x_Distance > (0.5 + Player.HIT_RADIUS)) { return 2; }
        if (z_Distance > (0.5 + Player.HIT_RADIUS)) { return 1; }

        if (x_Distance <= 0.5) { return 2; }
        if (z_Distance <= 0.5) { return 1; }

        double cornerDistance_sq = Math.pow(x_Distance - 0.5, 2) + Math.pow(z_Distance - 0.5, 2);

        if (cornerDistance_sq <= Math.pow(Player.HIT_RADIUS, 2)) {
            if (x_Distance < z_Distance) {
                return 2;
            }
            return 1;
        }

        return 0;
    }

    public Vector3D handlePlayerCollision(Vector3D playerPosition) {
        double x_Position = playerPosition.getX(), y_Position = playerPosition.getY(), z_Position = playerPosition.getZ();
        double x1Distance = Math.abs(x_Position - x1), x2Distance = Math.abs(x_Position - x2),
                z1Distance = Math.abs(z_Position - z1), z2Distance = Math.abs(z_Position - z2);
        int collisionCode = getPlayerCollisionCode(playerPosition);

        switch (collisionCode) {
            case (1):
                if ((x1Distance < x2Distance) && (x1Distance < Player.HIT_RADIUS)) {
                    playerPosition = new Vector3D(x1 - Player.HIT_RADIUS, y_Position, z_Position);
                } else if((x2Distance < x1Distance) && (x2Distance < Player.HIT_RADIUS)) {
                    playerPosition = new Vector3D(x2 + Player.HIT_RADIUS, y_Position, z_Position);
                }
                break;
            case (2):
                if((z1Distance < z2Distance) && (z1Distance < Player.HIT_RADIUS)) {
                    playerPosition = new Vector3D(x_Position, y_Position, z1 - Player.HIT_RADIUS);
                } else if((z2Distance < z1Distance) && (z2Distance < Player.HIT_RADIUS)) {
                    playerPosition = new Vector3D(x_Position, y_Position, z2 + Player.HIT_RADIUS);
                }
            default:
        }

        return playerPosition;
    }
}
