package it.unibo.oop.lab.workers02;

import java.util.stream.IntStream;

/**
 * 
 *
 */
public class MultiThreadedMatrixSumWithStreams implements SumMatrix {
    private final int nthreads; 

    /**
     * 
     * @param nthread
     *          number of threads performing the sum.
     */
    public MultiThreadedMatrixSumWithStreams(final int nthread) {
        this.nthreads = nthread;
    }

    private final class Worker extends Thread {

        private final double[][] elems;
        private final int firstRow;
        private final int nRows;
        private int result;

        /**
         * Builds a new worker.
         * 
         * @param elems
         *            the matrix to sum
         * @param row
         *            the initial row for this worker
         * @param nRows
         *            the number of rows to sum for this worker
         */
        Worker(final double[][] elems, final int row, final int nRows) {
            this.elems = elems;
            this.firstRow = row;
            this.nRows = nRows;
            this.result = 0;
        }

        @Override
        public void run() {
            System.out.println("Working from row " + this.firstRow + " to row " + (this.firstRow + this.nRows - 1));
            for (int i = 0; i < nRows && i + firstRow < this.elems.length; i++) {
                for (int j = 0; j < this.elems[i].length; j++) {
                    this.result += this.elems[i][j];
                }
            }
        }

        /**
         * Returns the result of summing up the double values within the matrix.
         * 
         * @return the sum of every element in the matrix
         */
        public double getResult() {
            return this.result;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int rows = matrix.length;
        final int assignRows = rows / this.nthreads + rows % this.nthreads;
        return IntStream.iterate(0,  start -> start + assignRows)
            .limit(this.nthreads)
            .mapToObj(start -> new Worker(matrix, start, assignRows))
            .peek(Thread::start)
            .peek(MultiThreadedMatrixSumWithStreams::joinUninterruptibly)
            // Get their result and sum
            .mapToDouble(Worker::getResult)
            .sum();
    }

    private static void joinUninterruptibly(final Thread target) {
        var joined = false;
        while (!joined) {
            try {
                target.join();
                joined = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
