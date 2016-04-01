package FrontEnd;

import BackEnd.Database;
import BackEnd.WeatherObservation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


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
    JEditorPane displayText;

    Database db = null;

    public GUI() {
        // add event listener to buttons
        loadButton.addActionListener(this);
        displayButton.addActionListener(this);

        scrollPane = new JScrollPane();

        displayText = new JEditorPane();
        displayText.setContentType("text/html");
        displayText.setText("<h2>Welcome to the weather observation viewer!</h2><h3>Press <i>load</i> to load the weather data, then <i>display</i> to view it!</h3>");
        displayText.setEditable(false);

        scrollPane.setViewportView(displayText);

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
        List<WeatherObservation> obs = new ArrayList<>(observations);
        if (observations.isEmpty()) {
            JOptionPane.showMessageDialog (null, "No weather observations have been loaded! Try clicking 'Load' to load from the html file first.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String[] columns = {"Place", "Date", "Temperature", "Humidity", "UV Index", "Wind Speed"};
            Object[][] data = new Object[observations.size()][6];


            // sort list of weather observations by date
            Collections.sort(obs);

            int i = 0;
            for (WeatherObservation o : obs) {
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
