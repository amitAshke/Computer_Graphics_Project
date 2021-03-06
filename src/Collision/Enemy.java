package Collision;

import Collision.Shapes.Capsule;
import Collision.Shapes.Hitbox;
import LinearAlgebra.Vectors.Vector3D;
import Main.Level;
import Main.LevelManager;
import Main.Renderer;
import Player.Player;
import Renderables.Renderable;
import Renderables.WallBlock;
import Time.TimeBound;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Main.Display.soundPlayer;

public class Enemy implements Collidable, Renderable, TimeBound {
    private Texture texture;
    private Vector3D position;
    private double leftRightAngle, projectileHitRadius = 0.15, playerHitRadius = 0.4, playerHitHeight = 1.2,
            projectileHitHeight = 1.2, aggroRange = 5, speed = 0.005, strikeCooldown = 1;
    private int hitPoints = 3, mapX = -1, mapZ = -1;
    private Capsule playerCollisionCapsule, projectileCollisionCapsule;
    private List<Collidable> collidables;
    private long lastTimeStrike = System.nanoTime();

    private static CollisionHandler collisionHandler;


    public Enemy(int row, int col, double leftRightAngle) {

        texture = Renderer.dummyTex;

        this.position = new Vector3D(row + 0.5, 0, col + 0.5);
        this.leftRightAngle = leftRightAngle;
        this.collidables = new ArrayList<>();
        collisionHandler = new CollisionHandler();

        updatePlayerCollisionCapsule();
        updateProjectileCollisionCapsule();
    }

    private void updatePlayerCollisionCapsule() {
        Vector3D topVectorPlayerHit = new Vector3D(position.getX(), position.getY() + playerHitHeight + playerHitRadius, position.getZ());
        Vector3D bottomVectorPlayerHit = new Vector3D(position.getX(), position.getY() + playerHitRadius, position.getZ());
        playerCollisionCapsule = new Capsule(bottomVectorPlayerHit, topVectorPlayerHit, playerHitRadius);
    }

    private void updateProjectileCollisionCapsule() {
        Vector3D topVectorProjectileHit = new Vector3D(position.getX(), position.getY() + projectileHitHeight + projectileHitRadius, position.getZ());
        Vector3D bottomVectorProjectileHit = new Vector3D(position.getX(), position.getY() + projectileHitRadius, position.getZ());
        projectileCollisionCapsule = new Capsule(bottomVectorProjectileHit, topVectorProjectileHit, projectileHitRadius);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Hitbox getProjectileCollisionShape() {
        return projectileCollisionCapsule;
    }

    public Hitbox getPlayerCollisionShape() {
        return playerCollisionCapsule;
    }

    public void render(GL2 gl) {

        double rotationAngle = 0;

        if (isPlayerInAgroRange()) {
            rotationAngle = Player.camera.position.subtract(0, Player.camera.position.getY(), 0).subtract(position).angle(new Vector3D(0, 0, -1));
            if (Player.camera.position.getX() > position.getX()) {
                rotationAngle *= -1;
            }
        }

        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }

        gl.glPushMatrix();

        gl.glTranslated(position.getX(), position.getY(), position.getZ());
        gl.glRotated(rotationAngle, 0, 1, 0);

        gl.glCallList(Renderer.enemyModel);

        gl.glPopMatrix();

        if (texture != null) {
            texture.disable(gl);
        }
    }

    public boolean tick() {
        Vector3D oldPosition = position;

        if (isPlayerInAgroRange() && hitPoints > 0) {
            Vector3D forward, movement;
            updateDummyCollidables();
            forward = Player.camera.position.subtract(0, Player.camera.position.getY(), 0).subtract(position).normalize();
            movement = pickMovement(forward);
            position = position.add(movement);
            position = collisionHandler.handleCollisionWithPlayer(playerCollisionCapsule, oldPosition, position, collidables);
            updatePlayerCollisionCapsule();
            updateProjectileCollisionCapsule();
        }

        return hitPoints > 0;
    }

    public Vector3D handlePlayerCollision(Vector3D newPosition) {
        long currentTimeStrike = System.nanoTime(), passedTime = currentTimeStrike - lastTimeStrike;

        // If the time that passed since the last shot is greater than the cooldown.
        if (passedTime / 1000000000.0 > strikeCooldown) {
            lastTimeStrike = currentTimeStrike;
            LevelManager.player.playerHit();
        }

        return newPosition;
    }

    public void projectileCollisionEffect() {
        --hitPoints;
        if (hitPoints <= 0) {
            soundPlayer.playDummyKill();
            playerCollisionCapsule = null;
            projectileCollisionCapsule = null;
        } else {
            soundPlayer.playDummyHit();
        }
    }

    private boolean isPlayerInAgroRange() {
        return Player.camera.position.subtract(position).magnitude() < aggroRange;
    }

    private Vector3D pickMovement(Vector3D forward) {
        Random r = new Random();
        int choice = r.nextInt(100);
        Vector3D movement, up = new Vector3D(0, 1, 0), right = forward.crossProduct(up).normalize();

        if (choice < 10) {
            movement = right.neg().scale(speed);
        } else if (choice < 20) {
            movement = right.scale(speed);
        } else if (choice < 40) {
            // Move left-forward
            movement = forward.scale(Math.sqrt(2) * speed).add(right.neg().scale(Math.sqrt(2) * speed));
        } else if (choice < 60) {
            // Move right-forward
            movement = forward.scale(Math.sqrt(2) * speed).add(right.scale(Math.sqrt(2) * speed));
        } else {
            movement = forward.scale(speed);
        }

        return movement;
    }

    private void updateDummyCollidables() {
        if (mapX == (int)position.getX() && mapZ == (int)position.getZ()) {
            return;
        }
        mapX = (int)position.getX();
        mapZ = (int)position.getZ();
        collidables.clear();
        int rowLowerLimit = Math.max(0, mapX - 1), rowUpperLimit = Math.min(mapX + 2, Level.map.length);
        int colLowerLimit = Math.max(0, mapZ - 1), colUpperLimit = Math.min(mapZ + 2, Level.map[0].length);
        for (int row = rowLowerLimit; row < rowUpperLimit; ++row) {
            for (int col = colLowerLimit; col < colUpperLimit; ++col) {
                switch (Level.map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        collidables.add(newWallBlock);
                        break;
                    default:
                }
            }
        }
        collidables.add(LevelManager.player);
    }
}
