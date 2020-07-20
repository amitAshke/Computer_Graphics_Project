package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Main.World;
import Player.Player;
import Renderables.Renderable;
import Time.TimeBound;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;

public class AltProjectile extends Projectile implements Renderable, TimeBound {

    private List<Projectile> projectiles;
    private double yrot;
    private int mapX, mapZ, projectileLimit = 4;
    private CollisionHandler collisionHandler;
    private boolean detached = false;

    public AltProjectile(String modelPath, String texturePath, String materialPath,
                         Vector3D position, Vector3D forward) {
        super(modelPath, texturePath, materialPath, position, forward, new Vector3D(0, 1, 0));

        this.yrot = 0;
        this.projectiles = new ArrayList<>(projectileLimit);
        for (int i = 1; i <= projectileLimit; ++i) {
            double angle = (360.0 / projectileLimit) * i + yrot;
            Vector3D projForward = forward.rotate(Math.toRadians(angle), 'y').normalize();
//            projectiles.add(new Projectile(model, material, texture, position, projForward, up));
            projectiles.add(new StandardProjectile(model, material, texture, position, projForward, up));
        }

        this.position = position;
        this.mapX = -1;
        this.mapZ = -1;
        this.collisionHandler = new CollisionHandler();

        updateProjectileCollidables();
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    @Override
    protected void updateProjectileCollidables() {
        projectileCollidables.addAll(World.dummies);
    }

    @Override
    public void setPosition(Vector3D position) {
        this.position = position;
        rotateProjectiles();
        for(Projectile projectile : projectiles) {
            projectile.setPosition(position);
            projectile.updateCollisionCapsule();
        }
    }

    private void rotateProjectiles() {
        for(Projectile projectile : projectiles) {
            projectile.forward = projectile.forward.rotate(Math.toRadians(2), 'y').normalize();
            projectile.leftRightAngle = projectile.calculateLeftRightAngle();
        }
    }

    public void detachProjectiles() {
        for(int index = 0; index < projectiles.size(); ++index) {
            projectiles.get(index).makeIndependent();
            double angle = (360.0 / projectileLimit) * index + yrot;
            Vector3D forward = new Vector3D(Math.sin(Math.toRadians(angle)),0.001, Math.cos(Math.toRadians(angle))).normalize();
            projectiles.get(index).setLeftRightAngle(angle);
            projectiles.get(index).setForward(forward);
            projectiles.get(index).setPosition(this.position.scaleAdd(0.05, forward));
        }
        detached = true;
    }

    @Override
    public void render(GL2 gl) {
        if (!detached) {
            for (Projectile projectile : projectiles) {
                projectile.render(gl);
            }
        }
    }

    @Override
    public boolean tick() {
        if (model == null) { return false; }

        if (projectiles.size() > 0) {
            if (!detached) {
                yrot = (yrot + 2) % 360;
                this.setPosition(Player.camera.position);
                for(int index = 0; index < projectiles.size(); ++index) {
                    Projectile projectile = projectiles.get(index);
                    Collidable collidable = collisionHandler.handleCollisionWithProjectile(projectile.capsule, projectileCollidables);
                    if (collidable != null) {
                        projectiles.remove(index);
                        --index;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
