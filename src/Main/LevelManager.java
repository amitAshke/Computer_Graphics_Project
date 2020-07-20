package Main;


public class LevelManager {
    private int currentLevel;
    private String[] levelFiles;
    
    public LevelManager() {
        currentLevel = -1;
        levelFiles = new String[]{"src\\map1.txt", "src\\map1.txt"};
    }

    public World getLevel(Render3D render3D) {
        ++currentLevel;
        if (currentLevel >= levelFiles.length) { return null; }
        return new World(levelFiles[currentLevel], render3D);
    }
}
