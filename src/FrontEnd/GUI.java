package FrontEnd;

import BackEnd.Database;
import BackEnd.WeatherObservation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;


/**
 * The user interface class - this sets up the gui, connects to the database, adds the listeners, etc.
 */
public class GUI implements ActionListener {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel bottom = new JPanel();
    JButton loadButton = new JButton("Load");
    JButton displayButton = new JButton("Display");
    JScrollPane scrollPane;
    JTable dataTable;

    Database db = null;

    public GUI() {
        // add event listener to buttons
        loadButton.addActionListener(this);
        displayButton.addActionListener(this);

        scrollPane = new JScrollPane();

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
        // panel.add(TODO);
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(bottom);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // frame
        frame.setTitle("Weather Observations");
        frame.setSize(600, 400);
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
        Collection<WeatherObservation> observations = db.getObservations();
        if (observations.isEmpty()) {

            // TODO: display message about no data

        } else {
            String[] columns = {"Place", "Date", "Temperature", "Humidity", "UV Index", "Wind Speed"};
            Object[][] data = new Object[observations.size()][6];

            int i = 0;
            for (WeatherObservation o : observations) {
                ArrayList<Object> odata = new ArrayList<>();
                data[i][0] = o.getPlace();
                data[i][1] = o.getNormalDate();
                data[i][2] = o.getTemperature();
                data[i][3] = o.getHumidity();
                data[i][4] = o.getUvIndex();
                data[i][5] = o.getWindSpeed();
                i++;
            }

            dataTable = new JTable(data, columns) {
                // disable editing cells (can't change the weather from the past! :P)
                // source: http://www.codeproject.com/Questions/557307/DisableplusEditingplusonplusJTablepluscellplusinpl
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            };

            scrollPane.setViewportView(dataTable);

            dataTable.setFillsViewportHeight(true);
        }
    }
}

