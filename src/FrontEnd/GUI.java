package FrontEnd;

import BackEnd.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The user interface class - this sets up the gui, connects to the database, adds the listeners, etc.
 */
public class GUI implements ActionListener {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel bottom = new JPanel();
    JButton loadButton = new JButton("Load");
    JButton displayButton = new JButton("Display");
    JTextArea dataTable = new JTextArea();
    JScrollPane scrollPane = new JScrollPane();

    Database db = null;

    public GUI() {
         // add event listener to buttons
         loadButton.addActionListener(this);
         displayButton.addActionListener(this);

         // the table of data (currently a textarea)
         dataTable = new JTextArea(5, 20);
         dataTable.setEditable(false);
         dataTable.setLineWrap(false);
         dataTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
         scrollPane = new JScrollPane(dataTable);

         // bottom panel
         bottom.setAlignmentX(1);
         bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

         // add button and buffer
         bottom.add(loadButton);
         bottom.add(Box.createRigidArea(new Dimension(15, 0)));
         bottom.add(displayButton);
         bottom.add(Box.createRigidArea(new Dimension(15, 0)));

         // panel
         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

         // add stuff to panel
         panel.add(scrollPane);
         panel.add(Box.createRigidArea(new Dimension(0, 15)));
         panel.add(bottom);
         panel.add(Box.createRigidArea(new Dimension(0, 15)));

         // frame
         frame.setTitle("Weather Observations");
         frame.setSize(400, 400);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.add(panel);

    }

    public void setDB(Database db) {
        this.db = db;
    }

    public void showGUI() {
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand() == "Load") {
            db.loadObservationsFromHTMLFile();
            this.loadButton.setText("Loaded");
        } else if (actionEvent.getActionCommand() == "Display") {
            displayTable();
        }
    }

    private void displayTable() {
        String observations = db.getObservations();
        if (observations == null) {
            dataTable.setText("No weather data loaded!");
        } else {
            dataTable.setText(observations);
        }
    }
}
