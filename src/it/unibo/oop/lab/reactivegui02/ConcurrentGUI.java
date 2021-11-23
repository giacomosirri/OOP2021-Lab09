package it.unibo.oop.lab.reactivegui02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 *
 */
public final class ConcurrentGUI extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -718335728756022921L;
    private static final long SLEEP_TIME = 100;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final List<JButton> buttons;
    private final JLabel label;

    /**
     * 
     */
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.buttons = new ArrayList<>(Arrays.asList(new JButton("up"), new JButton("down"), new JButton("stop")));
        this.label = new JLabel("0");
        final JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.add(this.label);
        for (final JButton thisButton : this.buttons) {
            mainPanel.add(thisButton);
        }
        this.getContentPane().add(mainPanel);
        this.setVisible(true);
        final Agent agent = new Agent();
        new Thread(agent).start();
        this.buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!agent.goUp) {
                    agent.setGoUp();
                }
            }
        });
        this.buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (agent.goUp) {
                    agent.setGoDown();
                }
            }
        });
        this.buttons.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stop();
                for (final JButton thisButton : ConcurrentGUI.this.buttons) {
                    thisButton.setEnabled(false);
                }
            }
        });
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
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            ConcurrentGUI.this.label.setText(Integer.toString(Agent.this.current));
                        }
                    });
                    if (this.goUp) {
                        this.incrementCounter();
                    } else {
                        this.decrementCounter();
                    }
                    Thread.sleep(SLEEP_TIME);
                } catch (InvocationTargetException | InterruptedException e) {
                    JOptionPane.showMessageDialog(ConcurrentGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private synchronized void incrementCounter() {
            this.current++;
        }
        
        private synchronized void decrementCounter() {
            this.current--;
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
