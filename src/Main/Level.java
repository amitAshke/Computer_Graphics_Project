package Main;

import Collision.Dummy;
import Renderables.*;
import Time.TimeBound;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;

public class Level implements TimeBound {

    private List<Renderable> renderables;
    private Render3D render3D;

    public static int[][] map;
    public static List<Dummy> dummies;

    public Level(int[][] map, Render3D render3D) {
        this.render3D = render3D;
        this.renderables = new ArrayList<>();
        this.dummies = new ArrayList<>();

        this.map = map;
        getRenderablesAndCollidablesInMap();
    }

    // Sets up renderable and collidable objects in the level.
    private void getRenderablesAndCollidablesInMap() {
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                switch (map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        renderables.add(newWallBlock);
                        break;
                    case (2):
                        Dummy dummy = new Dummy("src\\resources\\models\\18489_Knight_V1_.obj",
                                "src\\resources\\models\\textures\\rough.tif",
                                "",
                                row, col, 0);
                        dummies.add(dummy);
                        renderables.add(new FloorTile(row, col));
                        renderables.add(new CeilingTile(row, col));
                        break;
                    case (3):
                        renderables.add(new Lamp("src\\resources\\models\\textures\\black.png", row, col));
                        renderables.add(new FloorTile(row, col));
                        renderables.add(new CeilingTile(row, col));
                        break;
                    case (4):
                    case (1):
                        renderables.add(new FloorTile(row, col));
                        renderables.add(new CeilingTile(row, col));
                        break;
                    default:
                }
            }
        }
    }

    // Function which signifies the flow of time.
    public boolean tick() {

        for (int i = 0; i < dummies.size(); ++i) {
            if (dummies.get(i).getHitPoints() <= 0) {
                dummies.remove(i);
                --i;
            }
        }

        return true;
    }

    // Render the Level.
    public void render(GL2 gl) {
        render3D.renderLevel(gl, renderables);
    }
}
