package Main;

import Collision.Collidable;
import LinearAlgebra.Vectors.Vector3D;
import Player.Player;
import Renderables.FloorTile;
import Renderables.Renderable;
import Renderables.WallBlock;

import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

public class World {

    MapReader mapReader;
    private int[][] map;
    private Player player;
    private List<Renderable> renderables;
    private Render3D render3D;

    public static List<Collidable> playerCollidables;


    public World(String mapFilePath, Render3D render3D) {
        this.mapReader = new MapReader();
        this.player = new Player();
        this.render3D = render3D;
        this.renderables = new ArrayList<>();
        this.playerCollidables = new ArrayList<>();

        map = mapReader.loadMap(mapFilePath);
        getRenderablesAndCollidablesInMap();
        updatePlayerCollidables();
        return;
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

    private void updatePlayerCollidables() {
        playerCollidables.clear();
        Vector3D playerPosition = player.getCamera().position;
        int xPosition = (int)playerPosition.getX(), zPosition = (int)playerPosition.getZ();
        for (int row = Math.max(0, xPosition - 1); row < Math.min(xPosition + 2, map.length); ++row) {
            for (int col = Math.max(0, zPosition - 1); col < Math.min(zPosition + 2, map[0].length); ++col) {
                switch (map[row][col]) {
                    case (0):
                        WallBlock newWallBlock = new WallBlock(row, col);
                        playerCollidables.add(newWallBlock);
                        break;
                    default:
                }
            }
        }
    }

    public void tick() {
        updatePlayerCollidables();
        player.tick();
    }

    public void render(GLAutoDrawable glAutoDrawable) {
        render3D.renderAllV2(glAutoDrawable, player, renderables);
    }
}
