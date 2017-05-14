package tetris;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        Tetris t = new Tetris();

        t.setSize(t.getWidth(), t.getHeight());
        t.setVisible(true);
        t.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                t.pauseGame();
                int confirm = JOptionPane.showOptionDialog(t,
                    "Are You Sure to Close this Application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(1);
                } else {
                    t.setSize(t.getWidth(), t.getHeight());
                    t.setVisible(true);
                }
            }
        };
        t.addWindowListener(exitListener);
    }
}