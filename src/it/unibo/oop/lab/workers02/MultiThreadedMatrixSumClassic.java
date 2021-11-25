package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of a sum calculator for an arbitrary-sized matrix.
 * 
 */
public class MultiThreadedMatrixSumClassic implements SumMatrix {

    private final int nthreads; 

    /**
     * 
     * @param nthread
     *          number of threads performing the sum.
     */
    public MultiThreadedMatrixSumClassic(final int nthread) {
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
        double sum = 0;
        final int rows = matrix.length;
        final int assignRows = rows / this.nthreads + rows % this.nthreads;
        final List<Worker> workers = new ArrayList<>(this.nthreads);
        for (int start = 0; start < rows; start += assignRows) {
            workers.add(new Worker(matrix, start, assignRows));
        }
        for (final Worker thisWorker : workers) {
            thisWorker.start();
        }
        for (final Worker thisWorker : workers) {
            try {
                thisWorker.join();
                sum += thisWorker.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

}
