package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Main.World;
import Player.Player;
import Renderables.*;

import Time.TimeBound;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Projectile implements Renderable, TimeBound {

    private WavefrontObject model = null;
    private MaterialProps material;
    private Texture texture;
    private Vector3D position, forward, up;
    private double upDownAngle, leftRightAngle;
    private double speed = 0.01, length = 0.3, hitRadius = 0.05;
    private double yrot = 0;
    private List<Collidable> projectileCollidables;
    private int mapX, mapZ;
    private CollisionHandler collisionHandler;
    private boolean collided = false;
    long collisionTime;



    public Projectile(String modelPath, String texturePath, String materialPath,
                      Vector3D position, Vector3D forward, Vector3D up, double upDownAngle, double leftRightAngle) {
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

        this.model = new WavefrontObject(modelPath);

        if (!materialPath.equals("")) {
            this.material = new MaterialProps(materialPath);
        } else {
            this.material = null;
        }

        this.position = position;
        this.forward = forward;
        this.up = up;
        this.upDownAngle = upDownAngle;
        this.leftRightAngle = leftRightAngle;
        this.projectileCollidables = new ArrayList<>();
        this.mapX = -1;
        this.mapZ = -1;
        this.collisionHandler = new CollisionHandler();

        updateProjectileCollidables();
    }

    public void render(GL2 gl) {

        if (model == null) { return; }

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

//        System.out.println(leftRightAngle + "   " + upDownAngle);

        gl.glPushMatrix();

        gl.glTranslated(position.getX() + forward.getX() * 0.2 + up.getX() * -0.1,
                position.getY() + forward.getY() * 0.2 + up.getY() * -0.1,
                position.getZ() + forward.getZ() * 0.2 + up.getZ() * -0.1);

        gl.glRotated(leftRightAngle, 0, 1, 0);
        gl.glRotated(upDownAngle, 1, 0, 0);

        gl.glRotated(-90, 0, 1, 0);
        gl.glRotated(-90, 0, 0, 1);
        gl.glRotated(yrot, 0, 1, 0);

        model.drawModel(gl);

        if (texture != null) {
            texture.disable(gl);
        }

        gl.glPopMatrix();

    }

    private void updateProjectileCollidables() {
        if (mapX == (int)position.getX() && mapZ == (int)position.getZ()) {
            return;
        }
        mapX = (int)position.getX();
        mapZ = (int)position.getZ();
        projectileCollidables.clear();
        int rowLowerLimit = Math.max(0, mapX - 1), rowUpperLimit = Math.min(mapX + 2, World.map.length);
        int colLowerLimit = Math.max(0, mapZ - 1), colUpperLimit = Math.min(mapZ + 2, World.map[0].length);
        for (int row = rowLowerLimit; row < rowUpperLimit; ++row) {
            for (int col = colLowerLimit; col < colUpperLimit; ++col) {
                switch (World.map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        projectileCollidables.add(newWallBlock);
                        break;
                    case (1):
                        FloorTile newFloorTile = new FloorTile(row, col);
                        projectileCollidables.add(newFloorTile);
                        break;
                    default:
                }
            }
        }
    }

    public boolean tick() {
        if (model == null) { return false; }

        if (!collided) {
            ++yrot;
            updateProjectileCollidables();
            this.position = position.scaleAdd(speed, forward);
            Collidable collidable = collisionHandler.handleCollisionWithProjectile(position, length, hitRadius, forward, projectileCollidables);
            if (collidable != null) {
                collided = true;
                collisionTime = System.nanoTime();
            }
        } else {
            long sinceCollision = System.nanoTime() - collisionTime;
            if (sinceCollision > 1000000000.0) {
                this.resetValues();
                return false;
            }
        }
        return true;
    }

    private void resetValues() {
        model = null;
    }
}
