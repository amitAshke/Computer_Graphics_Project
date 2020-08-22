package Collision;

import Collision.Shapes.Capsule;
import LinearAlgebra.Vectors.Vector3D;
import Main.Level;
import Main.Renderer;
import Renderables.*;
import Time.TimeBound;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;

public class StandardProjectile implements Renderable, TimeBound {

    private long collisionTime;
    private final double initialSpeed = 0.08, rotationAngle = 6, lifeSpanAfterCollision = 1000000000.0 / 5;
    protected CollisionHandler collisionHandler;
    protected List<Collidable> projectileCollidables;
    protected int mapX, mapZ;
    protected boolean alive = true;
    protected MaterialProps material = null;
    protected Texture texture;
    protected Vector3D position, forward, up;
    protected double upDownAngle, leftRightAngle, rotation, speed, length, hitRadius;;
    protected boolean collided;
    protected Capsule capsule;

    public StandardProjectile(Vector3D position, Vector3D forward, Vector3D up, double speed) {

        texture = Renderer.projectileTex;

        setDefaults(position, forward, up);

        if (speed < 0) {
            this.speed = initialSpeed;
        } else {
            this.speed = speed;
        }

        updateProjectileCollidables();
        updateCollisionCapsule();
    }

    private void setDefaults(Vector3D position, Vector3D forward, Vector3D up) {
        this.position = new Vector3D(position.getX() + forward.getX() * 0.2 + up.getX() * -0.1,
                position.getY() + forward.getY() * 0.2 + up.getY() * -0.1,
                position.getZ() + forward.getZ() * 0.2 + up.getZ() * -0.1);;
        this.forward = forward;
        this.up = up;
        this.upDownAngle = calculateUpDownAngle();
        this.leftRightAngle = calculateLeftRightAngle();
        this.projectileCollidables = new ArrayList<>();
        this.mapX = -1;
        this.mapZ = -1;
        this.collisionHandler = new CollisionHandler();
        this.rotation = 0;
        this.length = 0.33;
        this.hitRadius = 0.04;
        this.collided = false;
    }

    public Capsule getCapsule() {
        return capsule;
    }

    public void setPosition(Vector3D position) {
        this.position = new Vector3D(position.getX() + forward.getX() * 0.2 + up.getX() * -0.1,
                position.getY() + forward.getY() * 0.2 + up.getY() * -0.1,
                position.getZ() + forward.getZ() * 0.2 + up.getZ() * -0.1);;
    }

    public void setForward(Vector3D forward) {
        this.forward = forward;
    }

    public void setLeftRightAngle(double leftRightAngle) {
        this.leftRightAngle = leftRightAngle;
    }

    public void addRotation() {
        rotation = (rotation + rotationAngle) % 360;
    }

    @Override
    public void render(GL2 gl) {

        if (!alive) { return; }

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

        gl.glCallList(Renderer.projectileModel);

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
        int rowLowerLimit = Math.max(0, mapX - 1), rowUpperLimit = Math.min(mapX + 2, Level.map.length);
        int colLowerLimit = Math.max(0, mapZ - 1), colUpperLimit = Math.min(mapZ + 2, Level.map[0].length);
        for (int row = rowLowerLimit; row < rowUpperLimit; ++row) {
            for (int col = colLowerLimit; col < colUpperLimit; ++col) {
                switch (Level.map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        projectileCollidables.add(newWallBlock);
                        break;
//                    case (2):
//                    case (1):
//                        FloorTile newFloorTile = new FloorTile(row, col);
//                        projectileCollidables.add(newFloorTile);
//                        projectileCollidables.add(new CeilingTile(row, col));
//                        break;
                    default:
                        FloorTile newFloorTile = new FloorTile(row, col);
                        projectileCollidables.add(newFloorTile);
                        projectileCollidables.add(new CeilingTile(row, col));
                        break;
                }
            }
        }
        projectileCollidables.addAll(Level.enemies);
    }

    protected void updateCollisionCapsule() {
        Vector3D botton, top;
        if (forward.getY() >= 0) {
            botton = position;
            top = position.scaleAdd(length, forward);
        } else {
            botton = position.scaleAdd(length, forward);
            top = position;
        }
        capsule = new Capsule(botton, top, hitRadius);
    }

    public void setProjectileCollidables(List<Collidable> projectileCollidables) {
        this.projectileCollidables = projectileCollidables;
    }

    public void makeIndependent() {
        speed = initialSpeed;
    }

    public double calculateLeftRightAngle() {
        Vector3D projectedForward = new Vector3D(forward.getX(), 0, forward.getZ()).normalize();
        double angle = 0;

        if (forward.getX() < 0) {
            angle = Math.toDegrees(Math.acos(projectedForward.dotProduct(new Vector3D(0, 0, 1)))) * -1;
        } else if (forward.getX() > 0) {
            angle = Math.toDegrees(Math.acos(projectedForward.dotProduct(new Vector3D(0, 0, 1))));
        } else {
            if (up.getX() < 0) {
                angle = Math.toDegrees(Math.acos(up.dotProduct(new Vector3D(0, 0, 1)))) * -1;
            } else if (up.getX() > 0) {
                angle = Math.toDegrees(Math.acos(up.dotProduct(new Vector3D(0, 0, 1))));
            }
        }

        return angle;
    }

    public double calculateUpDownAngle() {
        Vector3D projectedForward = new Vector3D(forward.getX(), 0, forward.getZ()).normalize();
        double angle = Math.toDegrees(Math.acos(projectedForward.dotProduct(forward)));

        if (forward.getY() > 0) {
            angle *= -1;
        }

        return angle;
    }

    public boolean tick() {
        if (!alive) { return false; }

        if (!collided) {
            addRotation();
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
            if (sinceCollision > lifeSpanAfterCollision) {
                this.resetValues();
                return false;
            }
        }
        return true;
    }

    private void resetValues() {
        alive = false;
    }
}
