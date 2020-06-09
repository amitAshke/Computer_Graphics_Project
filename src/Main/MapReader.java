package Main;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapReader {
    public int[][] loadMap(String fileName) {
        try {
            File mapFile = new File(fileName);
            FileInputStream fis = new FileInputStream(mapFile);
            DataInputStream din = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(din));
            String line  = br.readLine();
            int size, row = 0;
            if (line != null) {
                size = Integer.parseInt(line);
                if (size <= 0) {
                    return null;
                }
            } else {
                return null;
            }
            int[][] map = new int[size][size];
            while ((line = br.readLine()) != null)   {
                String[] split = line.split(" ");
                for (int col = 0; col < split.length; ++col) {
                    map[row][col] = Integer.parseInt(split[col]);
                }
                ++row;
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}