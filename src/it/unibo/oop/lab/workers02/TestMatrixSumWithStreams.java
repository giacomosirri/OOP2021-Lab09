package it.unibo.oop.lab.workers02;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * TestMatrix for workers02.
 *
 */
public class TestMatrixSumWithStreams {

    private static final int SIZE = 10_000;
    private static final String MSEC = " msec";
    private static final double EXPECTED_DELTA = 0.01;

    /**
     * Base test for a multithreaded list sum.
     */
    @Test
    public void testBasic() {
        double sum = 0;
        final double[][] matrix = new double[SIZE][SIZE];
        for (double[] d : matrix) {
            for (int i = 0; i < SIZE; i++) {
                d[i] = 1;
                sum += 1;
            }
        }
        System.out.println("BTW: the sum with " + SIZE + " elements is: " + sum);
        /*
         * Prepare time ant test with different number of threads
         */
        long time;
        for (final int threads: new int[] { 1, 2, 3, 8, 16, 32, 100 }) {
            final SumMatrix sumMatrix = new MultiThreadedMatrixSumWithStreams(threads);
            time = System.currentTimeMillis();
            assertEquals(sum, sumMatrix.sum(matrix), EXPECTED_DELTA);
            System.out.println("Tried with " + threads + " thread: "
                    + (System.currentTimeMillis() - time) + MSEC);
        }
    }
}
