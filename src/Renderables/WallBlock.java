package Renderables;

import Collision.Shapes.AABB;
import Collision.Collidable;
import Collision.Shapes.Hitbox;
import LinearAlgebra.Vectors.Vector3D;
import Main.Render3D;
import Player.Player;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

public class WallBlock implements Renderable, Collidable {
    private float x1, x2, y1, y2, z1, z2;
    private static Texture wallTex = null;
    private AABB aabb;

    public WallBlock(float x1, float z1) {
        this.x1 = x1;
        this.x2 = x1 + 1;
        this.y1 = 0;
        this.y2 = 2;
        this.z1 = z1;
        this.z2 = z1 + 1;

        aabb = new AABB(x1, x2, y1, y2, z1, z2);

        wallTex = Render3D.wallTex;
    }

    public Hitbox getProjectileCollisionShape() {
        return aabb;
    }

    public Hitbox getPlayerCollisionShape() {
        return aabb;
    }

    public void render(GL2 gl) {
        wallTex.enable(gl);
        wallTex.bind(gl);

        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3f(0,0,-1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, y1, z1);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x2, y2, z1);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x1, y2, z1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, y1, z1);

        gl.glNormal3f(0,0,1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, y1, z2);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x2, y2, z2);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x1, y2, z2);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, y1, z2);

        gl.glNormal3f(-1,0,0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x1, y1, z2);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x1, y2, z2);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x1, y2, z1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, y1, z1);

        gl.glNormal3f(1,0,0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, y1, z2);
        gl.glTexCoord2f(1, 2);
        gl.glVertex3f(x2, y2, z2);
        gl.glTexCoord2f(0, 2);
        gl.glVertex3f(x2, y2, z1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x2, y1, z1);

        gl.glEnd();

        wallTex.disable(gl);
    }

    public Vector3D handlePlayerCollision(Vector3D playerPosition) {
        double x_Position = playerPosition.getX(), y_Position = playerPosition.getY(), z_Position = playerPosition.getZ();
        double x = Math.max(x1, Math.min(x_Position, x2));
        double y = Math.max(y1, Math.min(y_Position, y2));
        double z = Math.max(z1, Math.min(z_Position, z2));
        Vector3D fix;

        if (x == x1 && z != z1 && z != z2) {
            playerPosition = new Vector3D(x1 - Player.HIT_RADIUS, y_Position, z_Position);
        } else if (x == x2 && z != z1 && z != z2) {
            playerPosition = new Vector3D(x2 + Player.HIT_RADIUS, y_Position, z_Position);
        } else if (z == z1 && x != x1 && x != x2) {
            playerPosition = new Vector3D(x_Position, y_Position, z1 - Player.HIT_RADIUS);
        } else if (z == z2 && x != x1 && x != x2) {
            playerPosition = new Vector3D(x_Position, y_Position, z2 + Player.HIT_RADIUS);
        } else {
            fix = playerPosition.subtract(new Vector3D(x, y, z)).normalize();
            if (Math.abs(fix.getX()) > Math.abs(fix.getZ())) {
                if (x == x1) {
                    playerPosition = new Vector3D(x1 - Player.HIT_RADIUS, y_Position, z_Position);
                } else if (x == x2) {
                    playerPosition = new Vector3D(x2 + Player.HIT_RADIUS, y_Position, z_Position);
                }
            } else {
                if (z == z1) {
                    playerPosition = new Vector3D(x_Position, y_Position, z1 - Player.HIT_RADIUS);
                } else if (z == z2) {
                    playerPosition = new Vector3D(x_Position, y_Position, z2 + Player.HIT_RADIUS);
                }
            }
        }

        return playerPosition;
    }

    public void projectileCollisionEffect() { }
}
