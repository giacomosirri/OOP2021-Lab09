package it.unibo.oop.lab.reactivegui03;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import it.unibo.oop.lab.reactivegui02.ConcurrentGUI;

/**
 * 
 *
 */
public class AnotherConcurrentGUIWithLambdas extends ConcurrentGUI {

    /**
     * 
     */
    private static final long serialVersionUID = -8710276539980695794L;
    private static final long SLEEP_TIME = 10_000;

    /**
     * 
     */
    public AnotherConcurrentGUIWithLambdas() {
        new Thread(() -> { 
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                this.displayError(e.getMessage());
            }
            try {
                SwingUtilities.invokeAndWait(() -> super.doOnStop());
            } catch (InterruptedException | InvocationTargetException e) {
                this.displayError(e.getMessage());
            }
        }).start();
    }
}
