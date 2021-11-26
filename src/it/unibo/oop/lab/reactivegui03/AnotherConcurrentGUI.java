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
    private static final long SLEEP_TIME = 10_000;

    /**
     * 
     */
    public AnotherConcurrentGUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    AnotherConcurrentGUI.this.displayError(e.getMessage());
                }
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            AnotherConcurrentGUI.super.doOnStop();
                        }
                    });
                } catch (InterruptedException | InvocationTargetException e) {
                    AnotherConcurrentGUI.this.displayError(e.getMessage());
                }
            }
        }).start();
    }

}