package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Main.Level;
import Player.Player;
import Renderables.Renderable;
import Time.TimeBound;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;

public class AltProjectile implements Renderable, TimeBound {

    private Vector3D position, forward, up;
    private double maxRange;
    private List<Collidable> projectileCollidables;
    private List<StandardProjectile> projectiles;
    private double yrot, initialDistance, rotationSpeed;
    private int mapX, mapZ, projectileLimit = Player.projectileLimit;
    private CollisionHandler collisionHandler;
    private boolean detached = false, alive = true;

    public AltProjectile(Vector3D position, Vector3D forward) {

        setDefaults(position, forward, new Vector3D(0, 1, 0));

        Vector3D tempProjPosition = position.add(0, -0.2, 0);
        for (int i = 1; i <= projectileLimit; ++i) {
            double angle = (360.0 / projectileLimit) * i + yrot;
            Vector3D projForward = forward.rotate(Math.toRadians(angle), 'y').normalize();
            Vector3D projPosition = tempProjPosition.add(projForward.scale(initialDistance));
            projectiles.add(new StandardProjectile(projPosition, projForward, up, 0));
        }

        updateProjectileCollidables();
    }

    private void setDefaults(Vector3D position, Vector3D forward, Vector3D up) {
        this.position = position;
        this.forward = forward;
        this.up = up;
        this.projectileCollidables = new ArrayList<>();
        this.mapX = -1;
        this.mapZ = -1;
        this.collisionHandler = new CollisionHandler();
        this.maxRange = 15;
        this.initialDistance = 0.5;
        this.yrot = 0;
        this.rotationSpeed = 1;
        this.projectiles = new ArrayList<>(Player.projectileLimit);
    }

    public List<StandardProjectile> getProjectiles() {
        return projectiles;
    }

    protected void updateProjectileCollidables() {
        projectileCollidables.addAll(Level.enemies);
    }

    public void setPosition(Vector3D position) {
        this.position = position;
        Vector3D tempProjPosition = position.add(0, -0.2, 0);
        rotateProjectiles();
        for(StandardProjectile projectile : projectiles) {
            projectile.setPosition(tempProjPosition.add(projectile.forward.scale(initialDistance)));
            projectile.updateCollisionCapsule();
        }
    }

    private void rotateProjectiles() {
        for(StandardProjectile projectile : projectiles) {
            projectile.forward = projectile.forward.rotate(Math.toRadians(2), 'y').normalize();
            projectile.leftRightAngle = projectile.calculateLeftRightAngle();
        }
    }

    public void detachProjectiles() {
        for(int index = 0; index < projectiles.size(); ++index) {
            projectiles.get(index).makeIndependent();
        }
        detached = true;
    }

    @Override
    public void render(GL2 gl) {
        if (!detached) {
            for (StandardProjectile projectile : projectiles) {
                projectile.render(gl);
            }
        }
    }

    @Override
    public boolean tick() {
        if (!alive) { return false; }

        if (projectiles.size() > 0) {
            if (!detached) {
                yrot = (yrot + rotationSpeed) % 360;
                this.setPosition(Player.camera.position);
                for(int index = 0; index < projectiles.size(); ++index) {
                    StandardProjectile projectile = projectiles.get(index);
                    Collidable collidable = collisionHandler.handleCollisionWithProjectile(projectile.getCapsule(), projectileCollidables);
                    if (collidable != null) {
                        projectiles.remove(index);
                        --index;
                    }
                }
            }
        } else {
            alive = false;
            return false;
        }
        return true;
    }
}
