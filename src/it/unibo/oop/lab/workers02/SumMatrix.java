package it.unibo.oop.lab.workers02;

/**
 * An interface defining the sum for a matrix.
 *
 */
public interface SumMatrix {

    /**
     * Computes the sum of all the values in the matrix.
     * 
     * @param matrix
     *            an arbitrary-sized matrix
     * @return the sum of its elements
     */
    double sum(double[][] matrix);

}
