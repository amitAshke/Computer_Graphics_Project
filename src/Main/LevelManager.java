package Main;

import Player.Player;

import javax.media.opengl.GL2;

public class LevelManager {
    private Render3D render3D;
    private int currentLevel;
    private String[] levelFiles;

    public Player player;

    public LevelManager(Render3D render3D) {
        this.render3D = render3D;
        currentLevel = -1;
        player = new Player();
        levelFiles = new String[]{"src\\map1.txt", "src\\map2.txt"};
    }

    public World getLevel() {
        ++currentLevel;
        if (currentLevel >= levelFiles.length) { return null; }

        return new World(levelFiles[currentLevel], render3D);
    }
}
