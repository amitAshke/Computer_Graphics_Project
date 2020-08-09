package Renderables;

import LinearAlgebra.Vectors.Vector3D;
import Main.Display;
import Main.Render3D;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

public class Lamp implements Renderable {
    private Vector3D position;
    private Texture texture;
    private MaterialProps material;

    public Lamp(int row, int col) {

        texture = Render3D.lampTex;

        this.position = new Vector3D(row + 0.5, 1.6, col + 0.5);
    }

    @Override
    public void render(GL2 gl) {
        float[] fPosition = { (float) position.getX(), (float) position.getY(), (float) position.getZ(), 1.0f };

        if (material != null) {
            if (material.getShininess() != -1) {
                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getShininess());
            }
//            if (material.getDissolve() != -1) {
//                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, , material.getShininess());
//            }
            if (material.getLuminance() != -1) {
                gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_LUMINANCE, material.getLuminance());
            }
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, material.getAmbient(), 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, material.getDiffuse(), 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, material.getSpecular(), 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material.getEmission(), 0);
        }

        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fPosition, 0);

        gl.glPushMatrix();

        gl.glTranslated(position.getX(), position.getY() + 0.4, position.getZ());
        gl.glCallList(Display.lampBaseModel);

        if (texture != null) {
            texture.disable(gl);
        }

        gl.glTranslated(0, -0.1, 0);
        gl.glScaled(0.8, 0.8, 0.8);
        gl.glCallList(Display.sphereModel);

        gl.glPopMatrix();
    }
}
