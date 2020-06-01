public class TransformationMatrix3D {
    protected double[][] matrix;
    protected Vector3D pos;

    protected TransformationMatrix3D() {
        this.pos = new Vector3D(0, 0, 0);
    }

    protected TransformationMatrix3D(Vector3D pos) {
        this.pos = pos;
    }

    public Vector3D getPos() {
        return pos;
    }

    public TransformationMatrix3D(TransformationMatrix3D m) {
        this.pos = m.getPos();
        this.matrix = m.getMatrix().clone();
    }

    public TransformationMatrix3D(double[][] matrix, Vector3D pos) {
        this.matrix = matrix;
        this.pos = pos;
    }

    protected double[][] zeroMatrix() {
        double[][] m = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = 0;
            }
        }
        return m;
    }

    protected void setMatrix(double[][] m) {
        this.matrix = m;
    }

    public Vector3D transform(Vector3D p) {
        double rowSum;
//        Point2D p = new Point2D.Double(p0.getX() - ox, p0.getY() - oy);

        double[] result = {0, 0, 0, 0};
        double[] vec = {p.getX(), p.getY(), p.getZ(), 1};

        for (int row = 0; row < vec.length; row++) {
            rowSum = 0;
            for (int col = 0; col < vec.length; col++) {
                rowSum += matrix[row][col] * vec[col];
            }
            result[row] += rowSum;
        }

        return new Vector3D(result[0], result[1], result[2]);
    }

    public static double[][] matrixMultiplication(double[][] a, double[][] b) {
        double[][] newMatrix = new double[4][4];

        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                for (int k = 0; k < newMatrix.length; k++) {
                    newMatrix[i][j] = newMatrix[i][j] + a[i][k] * b[k][j];
                }
            }
        }

        return newMatrix;
    }

    public TransformationMatrix3D matrixMultiplication(double[][] matrix2) {
        double[][] newMatrix = new double[4][4];

        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                for (int k = 0; k < newMatrix.length; k++) {
                    newMatrix[i][j] = newMatrix[i][j] + matrix[i][k] * matrix2[k][j];
                }
            }
        }

        return new TransformationMatrix3D(newMatrix, this.pos);
    }

    public TransformationMatrix3D matrixMultiplication(TransformationMatrix3D m) {
        return this.matrixMultiplication(m.getMatrix());
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public static double dotProduct(Vector3D p1, Vector3D p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY()
                + p1.getZ() * p2.getZ();

    }

    public static Vector3D crossProduct(Vector3D p1, Vector3D p2) {
        double resX, resY, resZ, ax = p1.getX(), ay = p1.getY(), az = p1.getZ(), bx = p2.getX(), by = p2.getY(),
                bz = p2.getZ();
        resX = ay * bz - az * by;
        resY = az * bx - ax * bz;
        resZ = ax * by - ay * bx;
        return new Vector3D(resX, resY, resZ);
    }
}