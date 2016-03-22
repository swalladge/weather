package Testing; /**
 * testing stuff for swing gui
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyButtonEvents implements ActionListener {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel bottom = new JPanel();
    JButton button = new JButton("                      ~                      ");
    boolean alreadypressed = false;

    public void buildTheGui() {
        // make a button
        button.addActionListener(this); // add event listener

        // bottom panel
        bottom.setAlignmentX(1f);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

        // add button and buffer
        bottom.add(button);
        bottom.add(Box.createRigidArea(new Dimension(15, 0)));

        // panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // add stuff to panel
        panel.add(Box.createVerticalGlue());
        panel.add(bottom);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // frame
        frame.setTitle("something");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        // make visible
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (alreadypressed) {
            frame.dispose();
            return;
        }
        this.button.setText("Please do not press this button again.");
        alreadypressed = true;
    }
}

public class TestSwing {

    public static void main(String[] args) {
        MyButtonEvents b = new MyButtonEvents();
        b.buildTheGui();
    }
}
