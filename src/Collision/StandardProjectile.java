package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Main.Display;
import Main.World;
import Renderables.*;
import Time.TimeBound;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;
import java.util.List;

public class StandardProjectile extends Projectile implements Renderable, TimeBound {

    private long collisionTime;

    public StandardProjectile(String modelPath, String texturePath, String materialPath,
                              Vector3D position, Vector3D forward, Vector3D up) {
        super(modelPath, texturePath, materialPath, position, forward, up);

        setDefaults();

        updateProjectileCollidables();
    }

    public StandardProjectile(WavefrontObject model, MaterialProps material, Texture texture,
                      Vector3D position, Vector3D forward, Vector3D up) {
        super(model, material, texture, position, forward, up);
    }

    private void setDefaults() {
        this.speed = 0.01;
    }

    @Override
    public void render(GL2 gl) {

        if (model == null) { return; }

        if (material != null) {
            if (material.getShininess() != -1) {
                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getShininess());
            }
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

        gl.glRotated(leftRightAngle, 0, 1, 0);
        gl.glRotated(upDownAngle, 1, 0, 0);

        gl.glRotated(-90, 0, 1, 0);
        gl.glRotated(-90, 0, 0, 1);
        gl.glRotated(rotation, 0, 1, 0);
        gl.glTranslated(0, 0.06, 0);

        gl.glCallList(Display.projectileModel);

        if (texture != null) {
            texture.disable(gl);
        }

        gl.glPopMatrix();

    }

    protected void updateProjectileCollidables() {
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
                    case (2):
                    case (1):
                        FloorTile newFloorTile = new FloorTile(row, col);
                        projectileCollidables.add(newFloorTile);
                        projectileCollidables.add(new CeilingTile(row, col));
                        break;
                    default:
                }
            }
        }
        projectileCollidables.addAll(World.dummies);
    }

    public void setProjectileCollidables(List<Collidable> projectileCollidables) {
        this.projectileCollidables = projectileCollidables;
    }

    public boolean tick() {
        if (model == null) { return false; }

        if (!collided) {
            ++rotation;
            this.position = position.scaleAdd(speed, forward);
            updateCollisionCapsule();
            updateProjectileCollidables();
            Collidable collidable = collisionHandler.handleCollisionWithProjectile(capsule, projectileCollidables);
            if (collidable != null) {
                collided = true;
                collisionTime = System.nanoTime();
            }
        } else {
            long sinceCollision = System.nanoTime() - collisionTime;
            if (sinceCollision > 1000000000.0 / 5) {
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
