/**
 * testing stuff for swing gui
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyButtonEvents implements ActionListener {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton button;
    boolean alreadypressed = false;

    public void buildTheGui() {
        button = new JButton("         ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(panel);
        panel.add(button);
        button.addActionListener(this);

        frame.setSize(400, 400);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (alreadypressed) {return;}
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
