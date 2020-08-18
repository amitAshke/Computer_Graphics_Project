package Player;

import Collision.*;
import LinearAlgebra.Vectors.Vector3D;
import Main.Level;
import Renderables.WallBlock;
import Time.TimeBound;

import javax.media.opengl.glu.GLU;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the player in the game.
 */
public class Player implements TimeBound, Collidable {

    private CollisionHandler collisionHandler;
    private int mapX, mapZ, maxHealth = 5;
    private Capsule capsule;
    // A list of objects in the level that the player can collide with.
    private List<Collidable> playerCollidables;


    public static Controller controller;
    public static int projectileLimit = 8, hitPoints = 5;
    public static List<StandardProjectile> projectiles;
    public static AltProjectile altProjectile = null;
    public static Camera camera;
    public static final double HIT_RADIUS = 0.3, COOLDOWN = 0.3;
    public static HUD hud;

    public Player() {

        Vector3D position = new Vector3D(0, 0, 0),
                w_Vector = new Vector3D(0, 0, 1),
                v_Vector = new Vector3D(0, 1, 0);
        camera = new Camera(position, w_Vector, v_Vector);
        controller = new Controller();
        collisionHandler = new CollisionHandler();
        playerCollidables = new ArrayList<>();
        projectiles = new ArrayList<>();
        hud = new HUD();

        this.mapX = -1;
        this.mapZ = -1;

        updatePlayerHitbox();
    }

    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * This function signifies the flow of time for the player.
     * @return true if the player is alive and false otherwise.
     */
    public boolean tick() {
        Vector3D oldPosition = camera.position;

        if (altProjectile != null) {
            boolean alive = altProjectile.tick();
            if (!altProjectile.tick()) {
                controller.setAltActive(alive);
            }
        }

        for(int index = 0; index < projectiles.size(); ++index) {
            boolean alive = projectiles.get(index).tick();
            if (!alive) {
                projectiles.remove(index);
                --index;
            }
        }

        updatePlayerCollidables();

        controller.handleMovement();

        updatePlayerHitbox();

        controller.handleRotation();

        controller.handleFire();

        camera.position = collisionHandler.handleCollisionWithPlayer(capsule, oldPosition, camera.position, playerCollidables);

        updatePlayerHitbox();

        return true;
    }

    public void setView(GLU glu) {
        camera.setView(glu);
    }

    /**
     * This functions update the players collision shape.
     */
    private void updatePlayerHitbox() {
        Vector3D top = new Vector3D(camera.position.getX(), camera.position.getY() - 0.2, camera.position.getZ());
        Vector3D bottom = new Vector3D(camera.position.getX(), HIT_RADIUS, camera.position.getZ());
        capsule = new Capsule(bottom, top, HIT_RADIUS);
    }

    /**
     * This functions keep track of the objects in the level that the player can collide with.
     */
    private void updatePlayerCollidables() {
        if (mapX == (int)camera.position.getX() && mapZ == (int)camera.position.getZ()) {
            return;
        }
        mapX = (int)camera.position.getX();
        mapZ = (int)camera.position.getZ();
        playerCollidables.clear();
        int rowLowerLimit = Math.max(0, mapX - 1), rowUpperLimit = Math.min(mapX + 2, Level.map.length);
        int colLowerLimit = Math.max(0, mapZ - 1), colUpperLimit = Math.min(mapZ + 2, Level.map[0].length);
        for (int row = rowLowerLimit; row < rowUpperLimit; ++row) {
            for (int col = colLowerLimit; col < colUpperLimit; ++col) {
                switch (Level.map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        playerCollidables.add(newWallBlock);
                        break;
                    default:
                }
            }
        }
        playerCollidables.addAll(Level.dummies);
    }

    /**
     * This functions reset the player for the next level.
     * @param newPosition is the players new position.
     */
    public void reset(Vector3D newPosition) {
        playerCollidables.clear();
        projectiles.clear();
        altProjectile = null;
        camera.position = newPosition;
        camera.resetDirections();
        Controller.inputHandler.resetMouseButtons();
        controller.resetValues();
        hitPoints = maxHealth;
    }

    @Override
    public Hitbox getProjectileCollisionShape() {
        return null;
    }

    @Override
    public Hitbox getPlayerCollisionShape() {
        return capsule;
    }

    @Override
    public Vector3D handlePlayerCollision(Vector3D newPosition) {
        return newPosition;
    }

    @Override
    public void projectileCollisionEffect() {
    }

    public void playerHit() {
        --hitPoints;
        if(hitPoints > 0) {
            Controller.soundPlayer.playPlayerHit();
        } else {
            Controller.soundPlayer.playPlayerDie();
        }
    }
}
