package it.unibo.oop.lab.reactivegui03;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
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

    /**
     * 
     */
    public AnotherConcurrentGUI() {
        new Thread(new Timer()).start();
    }

    private final class Timer extends Agent implements Runnable  {

        private static final long SLEEP_TIME = 10_000;

        @Override
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            Timer.this.stop();
                            for (final JButton thisButton : AnotherConcurrentGUI.this.getButtons()) {
                                thisButton.setEnabled(false);
                            }
                        }
                    });
                } catch (InvocationTargetException e) {
                    AnotherConcurrentGUI.this.displayError(e.getMessage());
                }
            } catch (InterruptedException e) {
                AnotherConcurrentGUI.this.displayError(e.getMessage());
            }
        }
    }
}
