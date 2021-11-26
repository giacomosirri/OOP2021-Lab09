package it.unibo.oop.lab.reactivegui03;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

import it.unibo.oop.lab.reactivegui02.ConcurrentGUI;

/**
 * 
 *
 */
public class AnotherConcurrentGUI extends ConcurrentGUI {

    /**
     * 
     */
    private static final long serialVersionUID = -8710276539980695794L;
    private final Timer timer;
    /**
     * 
     */
    public AnotherConcurrentGUI() {
        timer = new Timer();
        new Thread(this.timer).start();
    }

    private final class Timer implements Runnable  {

        private static final long SLEEP_TIME = 10_000;
        private boolean stop;

        @Override
        public void run() {
            try {
                while (!stop) {
                    Thread.sleep(SLEEP_TIME);
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                AnotherConcurrentGUI.this.doOnStop();
                            }
                        });
                    } catch (InterruptedException | InvocationTargetException e) {
                        AnotherConcurrentGUI.this.displayError(e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                AnotherConcurrentGUI.this.displayError(e.getMessage());
            }
        }

    }

    /**
     * 
     */
    public void doOnStop() {
        this.timer.stop = true;
        super.doOnStop();
    }

}