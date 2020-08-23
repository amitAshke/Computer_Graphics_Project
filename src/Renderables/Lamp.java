package Renderables;

import LinearAlgebra.Vectors.Vector3D;
import Main.Renderer;
import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

public class Lamp implements Renderable {
    private Vector3D position;
    private Texture texture;

    public Lamp(int row, int col) {

        texture = Renderer.lampTex;

        this.position = new Vector3D(row + 0.5, 1.6, col + 0.5);
    }

    @Override
    public void render(GL2 gl) {
        float[] fPosition = { (float) position.getX(), (float) (position.getY() - 0.4), (float) position.getZ(), 1.0f };

        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, fPosition, 0);

        gl.glPushMatrix();

        gl.glTranslated(position.getX(), position.getY() + 0.4, position.getZ());
        gl.glCallList(Renderer.lampBaseModel);

        if (texture != null) {
            texture.disable(gl);
        }

        gl.glTranslated(0, -0.1, 0);
        gl.glScaled(0.8, 0.8, 0.8);
        gl.glCallList(Renderer.sphereModel);

        gl.glPopMatrix();
    }
}
