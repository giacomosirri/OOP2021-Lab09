package it.unibo.oop.lab.workers02;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MultiThreadedMatrixSumClassic implements SumMatrix {

    private final int nthreads; 

    /**
     * 
     * @param nthread
     */
    public MultiThreadedMatrixSumClassic(final int nthread) {
        this.nthreads = nthread;
    }

    private final class Worker extends Thread {

        private final double[] elems;
        private final int first;
        private final int nelems;
        private int result;

        private Worker(final double[] elems, final int first, final int nelems) {
            this.elems = elems;
            this.first = first;
            this.nelems = nelems;
            this.result = 0;
        }

        @Override
        public void run() {
            System.out.println("Working from position " + this.first + " to position " + (this.first + this.nelems - 1));
            for (int i = this.first; i < elems.length && i < first + nelems; i++) {
                this.result += this.elems[i];
            }
        }

        public double getResult() {
            return this.result;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        double sum = 0;
        final int rows = matrix.length;
        final int columns = matrix[0].length;
        final int size = matrix[0].length / this.nthreads + matrix[0].length % this.nthreads;
        final List<Worker> workers = new ArrayList<>(this.nthreads);
        for (int i = 0; i < rows; i++) {
            for (int start = 0; start < columns; start += size) {
                workers.add(new Worker(matrix[i], start, size));
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
            workers.clear();
        }

        return sum;
    }

}
