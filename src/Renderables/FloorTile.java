package Renderables;

import Collision.AABB;
import Collision.Collidable;
import Collision.Hitbox;
import LinearAlgebra.Vectors.Vector3D;
import Main.Render3D;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

public class FloorTile implements Renderable, Collidable {
    private float x1, x2, y1, y2, z1, z2;
    private Texture floorTex;
    private AABB aabb;

    public FloorTile(float x1, float z1) {
        this.x1 = x1;
        this.x2 = x1 + 1;
        this.y1 = -2;
        this.y2 = 0;
        this.z1 = z1;
        this.z2 = z1 + 1;

        aabb = new AABB(x1, x2, y1, y2, z1, z2);

        floorTex = Render3D.floorTex;
    }

    public Hitbox getProjectileCollisionShape() {
        return aabb;
    }

    public Hitbox getPlayerCollisionShape() {
        return aabb;
    }

    public void render(GL2 gl) {

        floorTex.enable(gl);
        floorTex.bind(gl);

        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3f(0, 1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(x2, y2, z1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(x2, y2, z2);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(x1, y2, z2);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x1, y2, z1);

        gl.glEnd();
    }
    
    public Vector3D handlePlayerCollision(Vector3D newPosition) {
        return newPosition;
    }

    public void projectileCollisionEffect() { }
}
