package Player;

import Collision.*;
import LinearAlgebra.Vectors.Vector3D;
import Main.World;
import Renderables.WallBlock;
import Time.TimeBound;

import javax.media.opengl.glu.GLU;
import java.util.ArrayList;
import java.util.List;

public class Player implements TimeBound {

    private static Controller controller;
    private CollisionHandler collisionHandler;
    private List<Collidable> playerCollidables;
    private int mapX, mapZ;
    private Capsule capsule;

    public static int projectileLimit = 4;
    public static List<Projectile> projectiles;
    public static AltProjectile altProjectile = null;
    public static Camera camera;
    public static final double HIT_RADIUS = 0.3;

    public Player() {

        Vector3D position = new Vector3D(3.5, 1.2, 2.8),
//                w_Vector = new Vector3D(Math.sin(Math.toRadians(-30)), 0, Math.cos(Math.toRadians(-30))),
                w_Vector = new Vector3D(0, 0, 1),
                v_Vector = new Vector3D(0, 1, 0);
        camera = new Camera(position, w_Vector, v_Vector);
        controller = new Controller();
        collisionHandler = new CollisionHandler();
        playerCollidables = new ArrayList<>();
        projectiles = new ArrayList<>();

        this.mapX = -1;
        this.mapZ = -1;

        updatePlayerHitbox();
        updatePlayerCollidables();
    }

    public static Controller getController() { return controller; }

    public int getProjectileLimit() {
        return projectileLimit;
    }

    public boolean tick() {

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

        camera.position = collisionHandler.handleCollisionWithPlayer(capsule, camera.position, playerCollidables);

        updatePlayerHitbox();

        return true;

    }

    public void setView(GLU glu) {
        camera.setView(glu);
    }

    private void updatePlayerHitbox() {
        Vector3D top = new Vector3D(camera.position.getX(), camera.position.getY() - 0.2, camera.position.getZ());
        Vector3D bottom = new Vector3D(camera.position.getX(), HIT_RADIUS, camera.position.getZ());
        capsule = new Capsule(bottom, top, HIT_RADIUS);
    }

    private void updatePlayerCollidables() {
        if (mapX == (int)camera.position.getX() && mapZ == (int)camera.position.getZ()) {
            return;
        }
        mapX = (int)camera.position.getX();
        mapZ = (int)camera.position.getZ();
        playerCollidables.clear();
        int rowLowerLimit = Math.max(0, mapX - 1), rowUpperLimit = Math.min(mapX + 2, World.map.length);
        int colLowerLimit = Math.max(0, mapZ - 1), colUpperLimit = Math.min(mapZ + 2, World.map[0].length);
        for (int row = rowLowerLimit; row < rowUpperLimit; ++row) {
            for (int col = colLowerLimit; col < colUpperLimit; ++col) {
                switch (World.map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        playerCollidables.add(newWallBlock);
                        break;
                    default:
                }
            }
        }
        playerCollidables.addAll(World.dummies);
    }
}
