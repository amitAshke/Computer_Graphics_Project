package LinearAlgebra.Matrices;

import LinearAlgebra.Vectors.Vector3D;

public class WorldViewMatrix3D extends TransformationMatrix3D {
    public WorldViewMatrix3D(Vector3D position, Vector3D[] uvw) {
        super(position);
        this.buildMatrix(uvw);
    }

    public WorldViewMatrix3D(Vector3D position, double[][] matrix) {
        super(matrix, position);
    }

    public void buildMatrix(Vector3D[] uvw) {
        double[][] rot = super.zeroMatrix();
        rot[3][3] = 1;
        for (int i = 0; i < uvw.length; i++) {
            Vector3D p = uvw[i];
            rot[i][0] = p.getX();
            rot[i][1] = p.getY();
            rot[i][2] = p.getZ();
        }
        this.matrix = rot;
    }

    public WorldViewMatrix3D transpose() {
        double[][] tran = super.zeroMatrix();
        tran[3][3] = 1;
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                tran[i][j] = this.matrix[i][j];
            }
        }
        for (int row = 0; row < tran.length; ++row) {
            for (int col = 0; col < row; ++col) {
                tranSwap(tran, row, col);
            }
        }
        return new WorldViewMatrix3D(this.pos, tran);
    }

    private void tranSwap(double[][] matrix, int i, int j) {
        double temp = matrix[i][j];
        matrix[i][j] = matrix[j][i];
        matrix[j][i] = temp;
    }
}
