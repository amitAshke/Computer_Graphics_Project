package Main;

import Collision.Collidable;
import Collision.Projectile;
import LinearAlgebra.Vectors.Vector3D;
import Player.Player;
import Renderables.FloorTile;
import Renderables.Renderable;
import Renderables.WallBlock;
import Time.TimeBound;

import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

public class World implements TimeBound {

    private MapReader mapReader;
    private Player player;
    private List<Renderable> renderables;
    private Render3D render3D;

    public static int[][] map;
    public static List<Projectile> projectiles;

    public World(String mapFilePath, Render3D render3D) {
        this.mapReader = new MapReader();
        this.render3D = render3D;
        this.renderables = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        this.map = mapReader.loadMap(mapFilePath);
        getRenderablesAndCollidablesInMap();
        this.player = new Player();
    }

    public int[][] getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    private void getRenderablesAndCollidablesInMap() {
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                switch (map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        renderables.add(newWallBlock);
                        break;
                    case (1):
                        renderables.add(new FloorTile(row, col));
                        break;
                    default:
                }
            }
        }
    }

    public boolean tick() {

        for(int index = 0; index < projectiles.size(); ++index) {
            boolean alive = projectiles.get(index).tick();
            if (!alive) {
                projectiles.remove(index);
                --index;
            }
        }

        player.tick();

        return true;
    }

    public void render(GLAutoDrawable glAutoDrawable) {
        render3D.renderAllV2(glAutoDrawable, player, renderables, projectiles);
    }
}
