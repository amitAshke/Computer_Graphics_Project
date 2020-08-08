package LinearAlgebra.Matrices;

import LinearAlgebra.Matrices.TransformationMatrix3D;

/**
 * Translation matrix class used to move the camera.
 */
public class TranslateMatrix3D extends TransformationMatrix3D {
    public TranslateMatrix3D(double a, double b, double c) {
        super();
        this.buildMatrix(a, b, c);
    }

    public void buildMatrix(double a, double b, double c) {
        double[][] m = super.zeroMatrix();
        double params[] = {a, b, c, 1};
        for (int i = 0; i < 4; i++) {
            m[i][i] = 1;
            m[i][3] = params[i];
        }
        super.matrix = m;
    }
}
