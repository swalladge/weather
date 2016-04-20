package FrontEnd;

import BackEnd.Database;
import BackEnd.WeatherObservation;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


/**
 * The user interface class - this sets up the gui, connects to the database, adds the listeners, etc.
 */
public class GUI implements ActionListener {

    JFrame frame = new JFrame();
    animatedJPanel drawPanel = new animatedJPanel();
    JPanel searchPanel = new JPanel();
    JButton loadButton = new JButton("Load");
    JButton displayButton = new JButton("Display");
    JButton searchButton = new JButton("Search");
    JButton animateButton = new JButton("Animate!");
    JScrollPane scrollPane = new JScrollPane();
    JTable dataTable;
    JEditorPane displayText;
    JTextField searchText = new JTextField();
    Database db = null;

    public GUI() {
        // add event listener to buttons
        loadButton.addActionListener(this);
        displayButton.addActionListener(this);
        animateButton.addActionListener(this);
        searchButton.addActionListener(this);

        // main display output and weather table
        displayText = new JEditorPane();
        displayText.setContentType("text/html");
        displayText.setText("<h2>Welcome to the weather observation viewer!</h2><h3>Press <i>load</i> to load the weather data, then <i>display</i> to view it!</h3>");
        displayText.setEditable(false);
        scrollPane.setViewportView(displayText);
        
        // search panel consisting of a text box and button
        searchPanel.setAlignmentX(1);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        searchText.setColumns(30);

        searchPanel.add(searchText);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);

        // setup the drawing panel
        Dimension d = new Dimension(500, 500);
        drawPanel.setPreferredSize(d);
        // TODO: setup drawing stuff, timers, load images, etc.

        // we have a layout now
        SpringLayout layout = new SpringLayout();
        Container content = frame.getContentPane();
        content.setLayout(layout);

        // add all the components
        content.add(scrollPane);
        content.add(searchPanel);
        content.add(drawPanel);
        content.add(loadButton);
        content.add(displayButton);

        // line up everything with spring layout
        layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, frame);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, frame);
        layout.putConstraint(SpringLayout.NORTH, searchPanel, 10, SpringLayout.SOUTH, scrollPane);
        layout.putConstraint(SpringLayout.EAST, searchPanel, 0, SpringLayout.EAST, scrollPane);
        layout.putConstraint(SpringLayout.WEST, drawPanel, 10, SpringLayout.EAST, scrollPane);
        layout.putConstraint(SpringLayout.NORTH, loadButton, 10, SpringLayout.SOUTH, drawPanel);
        layout.putConstraint(SpringLayout.WEST, loadButton, 10, SpringLayout.EAST, scrollPane);
        layout.putConstraint(SpringLayout.WEST, displayButton, 10, SpringLayout.EAST, scrollPane);
        layout.putConstraint(SpringLayout.NORTH, displayButton, 10, SpringLayout.SOUTH, loadButton);

        // frame
        frame.pack();
        frame.setTitle("Weather Observations");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread painting = new Thread(drawPanel);
        painting.start();
        //new javax.swing.Timer(40, drawPanel).start();
    }

    public void setDB(Database db) {
        this.db = db;
    }

    public void showGUI() {
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // TODO: add actions for search and animate toggle buttons
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

    private class animatedJPanel extends JPanel implements ActionListener, Runnable {
        final int rightBound = 500;
        final int lowerBound = 500;
        long offset = 0;
        long startTime = (new Date()).getTime();

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // TODO: split drawing out into a separate class - and add methods to that for rain, sunshine, etc.
            Graphics2D pen = (Graphics2D) g;

            pen.setColor(Color.blue);

            offset = ((new Date()).getTime() - startTime);
            pen.fillOval(30,(int) (30+offset/30)%lowerBound, 10, 20);
            pen.fillOval(90,(int) (20+offset/40)%lowerBound, 10, 20);
            pen.fillOval(200,(int) (20+offset/10)%lowerBound, 10, 20);
            pen.fillOval(230,(int) (50+offset/80)%lowerBound, 15, 30);

        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // update animation here
            repaint();

        }

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
