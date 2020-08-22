package Main;

import Collision.Enemy;
import Renderables.*;
import Time.TimeBound;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents the game's level.
 */
public class Level implements TimeBound {

    private List<Renderable> renderables;
    private Renderer renderer;

    public static int[][] map;
    public static List<Enemy> enemies;

    public Level(int[][] map, Renderer renderer) {
        this.renderer = renderer;
        this.renderables = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.map = map;
        getRenderablesAndCollidablesInMap();
    }

    /**
     * Sets up renderable and collidable objects in the level.
     */
    private void getRenderablesAndCollidablesInMap() {
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                switch (map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        renderables.add(newWallBlock);
                        break;
                    case (2):
                        Enemy enemy = new Enemy(row, col, 0);
                        enemies.add(enemy);
                        renderables.add(new FloorTile(row, col));
                        renderables.add(new CeilingTile(row, col));
                        break;
                    case (3):
                        renderables.add(new Lamp(row, col));
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

    /**
     * Function which signifies the flow of time.
     */
    public boolean tick() {

        // Checks if a dummy "died" and removes it if so.
        for (int i = 0; i < enemies.size(); ++i) {
            if (!enemies.get(i).tick()) {
                enemies.remove(i);
                --i;
            }
        }

        return true;
    }

    /**
     * Render the level.
     */
    public void render(GL2 gl) {
        renderer.renderLevel(gl, renderables);
    }
}
