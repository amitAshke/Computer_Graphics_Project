package Player;

import Collision.Collidable;
import Collision.CollisionHandler;
import LinearAlgebra.Vectors.Vector3D;
import Main.World;
import Renderables.WallBlock;
import Time.TimeBound;

import javax.media.opengl.glu.GLU;
import java.util.ArrayList;
import java.util.List;

public class Player implements TimeBound {

    private Controller controller;
    private CollisionHandler collisionHandler;
    private List<Collidable> playerCollidables;
    private int mapX, mapZ;

    public static Camera camera;
    public static final double HIT_RADIUS = 0.3;

    public Player() {

        Vector3D position = new Vector3D(1.3, 1.2, 3),
//                w_Vector = new Vector3D(Math.sin(Math.toRadians(-30)), 0, Math.cos(Math.toRadians(-30))),
                w_Vector = new Vector3D(0, 0, 1),
                v_Vector = new Vector3D(0, 1, 0);
        camera = new Camera(position, w_Vector, v_Vector);
        controller = new Controller();
        collisionHandler = new CollisionHandler();
        playerCollidables = new ArrayList<>();

        this.mapX = -1;
        this.mapZ = -1;

        updatePlayerCollidables();
    }

    public Camera getCamera() {
        return camera;
    }

    public Controller getController() { return controller; }

    public boolean tick() {

        updatePlayerCollidables();

        controller.handleMovement(camera);

        controller.handleRotation(camera);

        controller.handleFire(camera);

        camera.position = collisionHandler.handleCollisionWithPlayer(camera.position, playerCollidables);

        return true;

    }

    public void setView(GLU glu) {
        camera.setView(glu);
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
    }
}
