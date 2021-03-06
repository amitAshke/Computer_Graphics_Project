package Player;

import LinearAlgebra.Matrices.RotationMatrix3D;
import LinearAlgebra.Matrices.WorldViewMatrix3D;
import LinearAlgebra.Vectors.Vector3D;

import javax.media.opengl.glu.GLU;

/**
 * This class represents the camera of the game.
 */
public class Camera {
    private static Camera singleInstance = null;
    public Vector3D position, w_Vector, u_Vector, v_Vector;

    public Camera() {
    }

    public static Camera getInstance() {
        if (singleInstance == null)
            singleInstance = new Camera();

        return singleInstance;
    }

    public void setValues(Vector3D position, Vector3D look, Vector3D up) {
        this.position = position;
        this.w_Vector = look.normalize();
        this.v_Vector = up.normalize();
        this.u_Vector = w_Vector.crossProduct(v_Vector).normalize();
    }

    public void moveForwardOrBackward(double distance) {
        Vector3D w_Projection;
        if (v_Vector.getY() > 0) {
            w_Projection = w_Vector.projectToXZ().normalize();
        } else {
            if (w_Vector.getY() == 1) {
                w_Projection = v_Vector.projectToXZ().neg().normalize();
            } else {
                w_Projection = v_Vector.projectToXZ().normalize();
            }
        }

        position = position.scaleAdd(distance, w_Projection);
    }

    public void moveRightOrLeft(double distance) { position = position.scaleAdd(distance, u_Vector.projectToXZ()); }

    /**
     * This function fix inconsistencies with the orthogonality of the camera's vectors.
     */
    public void fixOrthogonality() {
        if (w_Vector.dotProduct(u_Vector) > 0.01 || w_Vector.dotProduct(u_Vector) < -0.01) {
//            System.out.println(w_Vector.dotProduct(u_Vector) + " => w-u: not orthogonal");
//            System.out.println("angle:" + w_Vector.angle(u_Vector));
//            System.out.println("w_vector:  " + w_Vector);
//            System.out.println("u_vector:  " + u_Vector);
            u_Vector.setY(0);
            u_Vector = u_Vector.normalize();
            w_Vector = v_Vector.crossProduct(u_Vector);
//            System.out.println("new w_vector:  " + w_Vector);
//            System.out.println("");
        }
        if (v_Vector.dotProduct(u_Vector) > 0.01 || v_Vector.dotProduct(u_Vector) < -0.01) {
//            System.out.println(v_Vector.dotProduct(u_Vector) + " => v-u: not orthogonal");
//            System.out.println("angle:" + u_Vector.angle(v_Vector));
//            System.out.println("v_vector:  " + v_Vector);
//            System.out.println("u_vector:  " + u_Vector);
            u_Vector.setY(0);
            u_Vector = u_Vector.normalize();
            v_Vector = u_Vector.crossProduct(w_Vector);
//            System.out.println("new v_vector:  " + v_Vector);
//            System.out.println("");
        }
        if (w_Vector.dotProduct(v_Vector) > 0.01 || w_Vector.dotProduct(v_Vector) < -0.01) {
//            System.out.println(w_Vector.dotProduct(v_Vector) + " => w-v: not orthogonal");
//            System.out.println("angle:" + w_Vector.angle(v_Vector));
//            System.out.println("w_vector:  " + w_Vector);
//            System.out.println("v_vector:  " + v_Vector);
            u_Vector.setY(0);
            u_Vector = u_Vector.normalize();
            v_Vector = u_Vector.crossProduct(w_Vector);
//            System.out.println("new v_vector:  " + v_Vector);
//            System.out.println("");
        }
        return;
    }

    public void rotateUpDown(double angle) {

        if (angle == 0) { return; }

        Vector3D[] uvw = {u_Vector, v_Vector, w_Vector};

        WorldViewMatrix3D worldViewMatrix = new WorldViewMatrix3D(position, uvw);
        RotationMatrix3D rotate = new RotationMatrix3D(angle, 'x');
        WorldViewMatrix3D reversedWorldViewMatrix = worldViewMatrix.transpose();

        w_Vector = worldViewMatrix.transform(w_Vector).normalize();
        w_Vector = rotate.transform(w_Vector).normalize();
        w_Vector = reversedWorldViewMatrix.transform(w_Vector).normalize();

        v_Vector = worldViewMatrix.transform(v_Vector).normalize();
        v_Vector = rotate.transform(v_Vector).normalize();
        v_Vector = reversedWorldViewMatrix.transform(v_Vector).normalize();

        if (v_Vector.getY() < 0) {
            v_Vector.setY(0);
            v_Vector = v_Vector.normalize();
            w_Vector = v_Vector.crossProduct(u_Vector);
        }
    }

    public void rotateLeftRight(double angle) {

        if (angle == 0) { return; }

        RotationMatrix3D rotate = new RotationMatrix3D(angle, 'y');

        w_Vector = rotate.transform(w_Vector).normalize();
        u_Vector = rotate.transform(u_Vector).normalize();
    }

    /**
     * Sets the camera view of the game using a GLU object.
     */
    public void setView(GLU glu) {
        glu.gluLookAt(	position.getX(), position.getY(), position.getZ(),
                position.getX() + w_Vector.getX(), position.getY() + w_Vector.getY(), position.getZ() + w_Vector.getZ(),
                v_Vector.getX(), v_Vector.getY(), v_Vector.getZ());
    }

    public void resetDirections() {
        w_Vector = new Vector3D(0, 0, 1);
        v_Vector = new Vector3D(0, 1, 0);
        this.u_Vector = w_Vector.crossProduct(v_Vector).normalize();
    }
}
