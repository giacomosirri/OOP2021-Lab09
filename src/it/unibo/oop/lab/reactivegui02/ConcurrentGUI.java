package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * More advanced example of a reactive GUI: implements a counter
 * with 3 buttons: up to make the counter increase its value, 
 * down to make the counter decrease its value and stop to make the value stop.
 * 
 */
public class ConcurrentGUI extends JFrame {

    private static final long serialVersionUID = -718335728756022921L;
    private static final long SLEEP_TIME = 100;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;

    private final Agent agent;
    private final JButton upButton;
    private final JButton downButton;
    private final JButton stopButton;
    private final JLabel label;

    /**
     * Builds a JFrame so that it displays the counter and buttons 
     * with the needed functions.
     */
    public ConcurrentGUI() {
        /*
         * This example is still poorly designed and it is intended only to show
         * how a reactive GUI behaves and how we should interact with it.
         */
        /* 
         * graphic elements initialization
         */
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel mainPanel = new JPanel(new FlowLayout());
        this.label = new JLabel("0");
        mainPanel.add(this.label);
        this.upButton = new JButton("up");
        this.downButton = new JButton("down");
        this.stopButton = new JButton("stop");
        mainPanel.add(upButton);
        mainPanel.add(downButton);
        mainPanel.add(stopButton);
        this.getContentPane().add(mainPanel);
        this.setVisible(true);
        /*
         * thread initialization and setup of buttons' functions
         */
        this.agent = new Agent();
        new Thread(this.agent).start();
        this.upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.setGoUp();
            }
        });
        this.downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.setGoDown();
            }
        });
        this.stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ConcurrentGUI.this.stopCounting();
            }
        });
    }

    /**
     * Makes the counter stop and disables the buttons.
     */
    public void stopCounting() {
        agent.stop();
        this.upButton.setEnabled(false);
        this.downButton.setEnabled(false);
        this.stopButton.setEnabled(false);
    }

    /**
     * Displays a message dialog that describes the error that has occurred.
     * 
     * @param message
     *              the message displayed
     */
    public void displayError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private final class Agent implements Runnable {

        private volatile boolean stop;
        private int current;
        private volatile boolean goUp;

        private Agent() {
            this.stop = false;
            this.current = 0;
            this.goUp = true;
        }

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    final int nextValue = this.current;
                    SwingUtilities.invokeAndWait(new Runnable() { // a lambda here would be the best choice (see next exercises)
                        @Override
                        public void run() {
                            ConcurrentGUI.this.label.setText(Integer.toString(nextValue));
                        }
                    });
                    this.current = this.current + (this.goUp ?  1 : -1);
                    Thread.sleep(SLEEP_TIME);
                } catch (InvocationTargetException | InterruptedException e) {
                    ConcurrentGUI.this.displayError(e.getMessage());
                }
            }
        }

        private void setGoUp() {
            this.goUp = true;
        }

        private void setGoDown() {
            this.goUp = false;
        }

        private void stop() {
            this.stop = true;
        }
    }
}
