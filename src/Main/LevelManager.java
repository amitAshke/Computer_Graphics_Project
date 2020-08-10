package Main;

import LinearAlgebra.Vectors.Vector3D;
import Player.Player;

/**
 * This class dictates the flow of the levels in the game. It also keeps a property of Player to move it between levels.
 */
public class LevelManager {
    private Render3D render3D;
    private int currentLevel;
    private String[] levelFiles;
    private MapReader mapReader;

    public static Player player;

    public LevelManager(Render3D render3D) {
        this.render3D = render3D;
        currentLevel = -1;
        player = new Player();
        levelFiles = new String[]{"src\\map1.txt", "src\\map2.txt", "src\\map3.txt"};
        mapReader = new MapReader();
    }

    /**
     * This functions returns location of the player's camera based on the 2D matrix that represents the map of the current level.
     */
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

    /**
     * This functions return the next level.
     */
    public Level getNextLevel() {
        ++currentLevel;
        if (currentLevel >= levelFiles.length) { return null; }
        int[][] map = mapReader.loadMap(levelFiles[currentLevel]);
        Vector3D newPlayerPosition = getNewPlayerPosition(map);
        player.reset(newPlayerPosition);
        return new Level(map, render3D);
    }

    public void restartGame() {
        currentLevel = -1;
    }
}
