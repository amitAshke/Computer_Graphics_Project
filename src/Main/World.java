package Main;

import Collision.Dummy;
import LinearAlgebra.Vectors.Vector3D;
import Player.Player;
import Renderables.*;
import Time.TimeBound;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

public class World implements TimeBound {

    private MapReader mapReader;
    private Player player;
    private List<Renderable> renderables;
    private Render3D render3D;

    public static int[][] map;
    public static List<Dummy> dummies;

    public World(String mapFilePath, Render3D render3D) {
        this.mapReader = new MapReader();
        this.render3D = render3D;
        this.renderables = new ArrayList<>();
        this.dummies = new ArrayList<>();

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
                    case (1):
                        renderables.add(new FloorTile(row, col));
                        renderables.add(new CeilingTile(row, col));
                        break;
                    default:
                }
            }
        }
    }

    public boolean tick() {

        player.tick();

        for (int i = 0; i < dummies.size(); ++i) {
            if (dummies.get(i).getHitPoints() <= 0) {
                dummies.remove(i);
                --i;
            }
        }

        return true;
    }

    public void render(GL2 gl) {
        render3D.renderAllV2(gl, player, renderables);
    }
}
