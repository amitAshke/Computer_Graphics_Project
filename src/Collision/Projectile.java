package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Renderables.*;

import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;
import java.util.List;

public class Projectile {

    protected CollisionHandler collisionHandler;
    protected List<Collidable> projectileCollidables;
    protected int mapX, mapZ;
    protected MaterialProps material;
    protected Texture texture;
    protected Vector3D position, forward, up;
    protected double upDownAngle, leftRightAngle, maxRange, rotation, speed, length, hitRadius;;
    protected boolean collided, alive = true;


    public Projectile(Vector3D position, Vector3D forward, Vector3D up) {

        setDefaults(position, forward, up);

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
        this.speed = 0;
        this.length = 0.33;
        this.hitRadius = 0.04;
        this.maxRange = 15;
        this.collided = false;
    }

    public double calculateUpDownAngle() {
        Vector3D projectedForward = new Vector3D(forward.getX(), 0, forward.getZ()).normalize();
        double angle = Math.toDegrees(Math.acos(projectedForward.dotProduct(forward)));

        if (forward.getY() > 0) {
            angle *= -1;
        }

        return angle;
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
}
