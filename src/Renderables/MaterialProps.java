package Renderables;

import java.io.*;

public class MaterialProps {
    private float shininess = -1;
    private float dissolve = -1;
    private float luminance = -1;
    private float[] ambient = new float[3];
    private float[] diffuse = new float[3];
    private float[] emission = new float[3];
    private float[] specular = new float[3];

    public MaterialProps(String materialPath) {
        try {
            BufferedReader br = null;
            File modelFile = new File(materialPath);
            FileInputStream fis = new FileInputStream(modelFile);
            DataInputStream din = new DataInputStream(fis);
            br = new BufferedReader(new InputStreamReader(din));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) { //read any descriptor data in the file
                    // Zzzz ...
                } else if (line.equals("")) {
                    // Ignore whitespace data
                } else if (line.startsWith("Ns ")) {
                    shininess = ProcessDataNumber(line);
                } else if (line.startsWith("d ")) {
                    dissolve = ProcessDataNumber(line);
                } else if (line.startsWith("illum ")) {
                    luminance = ProcessDataNumber(line);
                } else if (line.startsWith("Ka ")) {
                    ambient = ProcessDataArray(line);
                } else if (line.startsWith("Kd ")) {
                    diffuse = ProcessDataArray(line);
                } else if (line.startsWith("Ks ")) {
                    specular = ProcessDataArray(line);
                } else if (line.startsWith("Ke ")) {
                    emission = ProcessDataArray(line);
                }
            }
            br.close();
        } catch (IOException e) {
        }
    }

    private float[] ProcessDataArray(String line) {
        String[] split = line.split(" ");
        float[] values = new float[3];
        for (int i = 1; i < 4; ++i) {
            values[i - 1] = Float.parseFloat(split[i]);
        }
        return values;
    }

    private float ProcessDataNumber(String line) {
        String[] split = line.split(" ");
        return Float.parseFloat(split[1]);
    }

    public float getShininess() {
        return shininess;
    }

    public float getDissolve() {
        return dissolve;
    }

    public float getLuminance() {
        return luminance;
    }

    public float[] getAmbient() {
        return ambient;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public float[] getEmission() {
        return emission;
    }

    public float[] getSpecular() {
        return specular;
    }
}
