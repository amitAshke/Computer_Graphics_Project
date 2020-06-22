package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Main.Display;
import Player.Player;
import Renderables.MaterialProps;
import Renderables.Renderable;
import Renderables.WavefrontObject;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;

public class Dummy implements Collidable, Renderable {
    private WavefrontObject model = null;
    private MaterialProps material;
    private Texture texture;
    private Vector3D position;
    private double leftRightAngle, projectileHitRadius = 0.15, playerHitRadius = 0.4, playerHitHeight = 1.2, projectileHitHeight = 1.4;
    private int hitPoints = 3;
    private Capsule playerCollisionCapsule, projectileCollisionCapsule;

    public Dummy(String modelPath, String texturePath, String materialPath,
                      int row, int col, double leftRightAngle) {
        try {
            if (!texturePath.equals("")) {
                this.texture = TextureIO.newTexture(new File( texturePath ),true);
            } else {
                this.texture = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        if (!materialPath.equals("")) {
//            this.material = new MaterialProps(materialPath);
//        } else {
//            this.material = null;
//        }

        this.position = new Vector3D(row + 0.5, 0, col + 0.5);
        this.leftRightAngle = leftRightAngle;

        Vector3D topVectorPlayerHit = new Vector3D(position.getX(), position.getY() + playerHitHeight + playerHitRadius, position.getZ());
        Vector3D bottomVectorPlayerHit = new Vector3D(position.getX(), position.getY() + playerHitRadius, position.getZ());
        playerCollisionCapsule = new Capsule(bottomVectorPlayerHit, topVectorPlayerHit, playerHitRadius);

        Vector3D topVectorProjectileHit = new Vector3D(position.getX(), position.getY() + projectileHitHeight + projectileHitRadius, position.getZ());
        Vector3D bottomVectorProjectileHit = new Vector3D(position.getX(), position.getY() + projectileHitRadius, position.getZ());
        projectileCollisionCapsule = new Capsule(bottomVectorProjectileHit, topVectorProjectileHit, projectileHitRadius);
    }

    public Dummy(int row, int col, double leftRightAngle) {
        this.position = new Vector3D(row + 0.5, 0, col + 0.5);
        this.leftRightAngle = leftRightAngle;

        Vector3D topVectorPlayerHit = new Vector3D(position.getX(), position.getY() + playerHitHeight + playerHitRadius, position.getZ());
        Vector3D bottomVectorPlayerHit = new Vector3D(position.getX(), position.getY() + playerHitRadius, position.getZ());
        playerCollisionCapsule = new Capsule(bottomVectorPlayerHit, topVectorPlayerHit, playerHitRadius);

        Vector3D topVectorProjectileHit = new Vector3D(position.getX(), position.getY() + projectileHitHeight + projectileHitRadius, position.getZ());
        Vector3D bottomVectorProjectileHit = new Vector3D(position.getX(), position.getY() + projectileHitRadius, position.getZ());
        projectileCollisionCapsule = new Capsule(bottomVectorProjectileHit, topVectorProjectileHit, projectileHitRadius);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Hitbox getProjectileCollisionShape() {
        return projectileCollisionCapsule;
    }

    public Hitbox getPlayerCollisionShape() {
        return playerCollisionCapsule;
    }

    public void render(GL2 gl) {

//        if (model == null) {
//            return;
//        }

        if (material != null) {
            if (material.getShininess() != -1) {
                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getShininess());
            }
//            if (material.getDissolve() != -1) {
//                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, , material.getShininess());
//            }
            if (material.getLuminance() != -1) {
                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_LUMINANCE, material.getLuminance());
            }
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, material.getAmbient(), 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, material.getDiffuse(), 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, material.getSpecular(), 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material.getEmission(), 0);
        }

        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }

        gl.glPushMatrix();

        gl.glTranslated(position.getX(), position.getY(), position.getZ());

        gl.glCallList(Display.dummyModel);

        gl.glPopMatrix();

        if (texture != null) {
            texture.disable(gl);
        }
    }

    public Vector3D handlePlayerCollision(Vector3D newPosition) {
        Vector3D leveledPosition = new Vector3D(position.getX(), newPosition.getY(), position.getZ());
        Vector3D fixDirection = newPosition.subtract(leveledPosition).normalize();
        Vector3D fixedPosition = position.scaleAdd(Player.HIT_RADIUS + playerHitRadius, fixDirection);
        fixedPosition.setY(newPosition.getY());
        return fixedPosition;
    }

    public void projectileCollisionEffect() {

    }
}
