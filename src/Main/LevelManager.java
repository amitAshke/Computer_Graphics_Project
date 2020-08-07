package Main;

import LinearAlgebra.Vectors.Vector3D;
import Player.Player;

import javax.media.opengl.GL2;

public class LevelManager {
    private Render3D render3D;
    private int currentLevel;
    private String[] levelFiles;
    private MapReader mapReader;

    public Player player;

    public LevelManager(Render3D render3D) {
        this.render3D = render3D;
        currentLevel = -1;
        player = new Player();
        levelFiles = new String[]{"src\\map1.txt", "src\\map2.txt"};
        mapReader = new MapReader();
    }

    private Vector3D getNewPlayerPosition(int[][] map) {
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                if (map[row][col] == 4) {
                    return new Vector3D(row + 0.5, 1.2, col + 0.5);
                }
            }
        }
        return null;
    }

    public World getLevel() {
        ++currentLevel;
        if (currentLevel >= levelFiles.length) { return null; }
        int[][] map = mapReader.loadMap(levelFiles[currentLevel]);
        Vector3D newPlayerPosition = getNewPlayerPosition(map);
        player.reset(newPlayerPosition);
        return new World(map, render3D);
    }
}
