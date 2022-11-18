package dev.extrreme.spacebot.utils;

import org.ejml.simple.SimpleMatrix;

public class MatrixUtility {
    /**
     * Generate the fundamental X rotation matrix for a rotation by a specified angle.
     *
     * @param angle The angle to generate the rotation matrix for.
     * @return The rotation matrix evaluated with the specified angle.
     */
    public static SimpleMatrix rotX(double angle) {
        return new SimpleMatrix(new double[][] {
            { 1,        0,                  0        },
            { 0, Math.cos(angle),    Math.sin(angle) },
            { 0, -1*Math.sin(angle), Math.cos(angle) }
        });
    }

    /**
     * Generate the fundamental Y rotation matrix for a rotation by a specified angle.
     *
     * @param angle The angle to generate the rotation matrix for.
     * @return The rotation matrix evaluated with the specified angle.
     */
    public static SimpleMatrix rotY(double angle) {
        return new SimpleMatrix(new double[][] {
                { Math.cos(angle), 0, -1*Math.sin(angle) },
                {        0,        1,          0         },
                { Math.sin(angle), 0,   Math.cos(angle)  }
        });
    }

    /**
     * Generate the fundamental Z rotation matrix for a rotation by a specified angle.
     *
     * @param angle The angle to generate the rotation matrix for.
     * @return The rotation matrix evaluated with the specified angle.
     */
    public static SimpleMatrix rotZ(double angle) {
        return new SimpleMatrix(new double[][] {
                {   Math.cos(angle),  Math.sin(angle), 0},
                { -1*Math.sin(angle), Math.cos(angle), 0},
                {          0,                0,        1}
        });
    }

    /**
     * Perform a chain of matrix multiplications
     *
     * The matrices are multiplied as follows:<br><br>
     * matrices[n] * ... * matrices[1] * matrices[0]<br><br>
     *
     * @param matrices The matrices to be multiplied.
     * @return The resulting matrix.
     */
    public static SimpleMatrix chain(SimpleMatrix... matrices) {
        SimpleMatrix result = matrices[0];

        for (int i = 1; i < matrices.length; i++) {
            result = multiply(matrices[i], result);
        }

        return result;
    }

    /**
     * Perform a matrix multiplication
     *
     * The matrices are multiplied as follows: B * A
     *
     * @param A The base matrix.
     * @param B The matrix to multiply A by.
     * @return The resulting matrix.
     */
    public static SimpleMatrix multiply(SimpleMatrix A, SimpleMatrix B) {
        return A.mult(B);
    }
}
