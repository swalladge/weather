package FrontEnd;

import BackEnd.Database;
import BackEnd.WeatherObservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;


/**
 * The user interface class - this sets up the gui, connects to the database, adds the listeners, etc.
 */
public class GUI implements ActionListener, KeyListener {

    private static Logger logger = Logger.getLogger(GUI.class.getName());
    JFrame frame = new JFrame();
    animatedJPanel drawPanel = new animatedJPanel();
    JButton loadButton = new JButton("Load");
    JButton displayButton = new JButton("Display");
    JButton searchButton = new JButton("Search");
    JButton animateButton = new JButton("Pause Animation");
    JScrollPane scrollPane = new JScrollPane();
    JTable dataTable;
    JEditorPane displayText;
    JTextField searchText = new JTextField();
    Database db = null;
    Thread painting;
    String welcomeText = "<h2>Welcome to the weather observation viewer!</h2>" +
            "<h3>Press [load] to load the weather data, then [display] to view it!</h3>" +
            "<p>You can search for a particular date (like '25/12/2015') using the search box.</p>";

    public GUI() {
        // add event listener to buttons
        loadButton.addActionListener(this);
        displayButton.addActionListener(this);
        animateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (drawPanel.suspended) {
                    animateButton.setText("Pause Animation");
                } else {
                    animateButton.setText("Play Animation");
                }
                drawPanel.suspended = !drawPanel.suspended;
            }
        });
        searchButton.addActionListener(this);

        // main display output and weather table
        displayText = new JEditorPane();
        displayText.setContentType("text/html");
        displayText.setText(welcomeText);
        displayText.setEditable(false);
        //displayText.setPreferredSize(new Dimension(430, 500));
        displayText.setBackground(new Color(0xFAF2CD));
        displayText.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        String[] columns = {"Place", "Date", "Temperature", "Humidity", "UV Index", "Wind Speed"};
        Object[][] data = new Object[2][6];
        dataTable = new JTable(data, columns) {
            // disable editing cells (can't change the weather from the past! :P)
            // source: http://www.codeproject.com/Questions/557307/DisableplusEditingplusonplusJTablepluscellplusinpl
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        dataTable.setBackground(new Color(0xD1EFD8));
        scrollPane.setViewportView(dataTable);
        dataTable.setFillsViewportHeight(true);
        scrollPane.setPreferredSize(new Dimension(550, 500));


        //searchText.setColumns(30);
        searchText.addKeyListener(this);

        // setup the drawing panel
        drawPanel.setPreferredSize(new Dimension(1000, 200));

        // we have a layout now
        SpringLayout layout = new SpringLayout();
        Container content = frame.getContentPane();
        content.setLayout(layout);

        // add all the components
        content.add(drawPanel);
        content.add(scrollPane);
        content.add(displayText);
        content.add(loadButton);
        content.add(displayButton);
        content.add(animateButton);
        content.add(searchText);
        content.add(searchButton);

        // line up everything with spring layout
        // put in everything anticlockwise to ensure buttons and searchbox is not distorted,
        //   and let displaytext fill remaining space

        // draw panel top left
        layout.putConstraint(SpringLayout.WEST, drawPanel, 10, SpringLayout.WEST, frame);
        layout.putConstraint(SpringLayout.NORTH, drawPanel, 10, SpringLayout.NORTH, frame);

        layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, frame);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.SOUTH, drawPanel);

        layout.putConstraint(SpringLayout.WEST, displayText, 10, SpringLayout.EAST, scrollPane);
        layout.putConstraint(SpringLayout.NORTH, displayText, 10, SpringLayout.SOUTH, drawPanel);
        layout.putConstraint(SpringLayout.EAST, displayText, 0, SpringLayout.EAST, drawPanel);

        layout.putConstraint(SpringLayout.SOUTH, displayText, -10, SpringLayout.NORTH, loadButton);

        layout.putConstraint(SpringLayout.SOUTH, searchButton, 0, SpringLayout.SOUTH, scrollPane);
        layout.putConstraint(SpringLayout.EAST, searchButton, 0, SpringLayout.EAST, displayText);

        layout.putConstraint(SpringLayout.SOUTH, loadButton, -10, SpringLayout.NORTH, searchButton);

        layout.putConstraint(SpringLayout.WEST, loadButton, 10, SpringLayout.EAST, scrollPane);
        layout.putConstraint(SpringLayout.WEST, displayButton, 10, SpringLayout.EAST, loadButton);
        layout.putConstraint(SpringLayout.NORTH, displayButton, 0, SpringLayout.NORTH, loadButton);
        layout.putConstraint(SpringLayout.NORTH, animateButton, 0, SpringLayout.NORTH, displayButton);
        layout.putConstraint(SpringLayout.EAST, animateButton, 0, SpringLayout.EAST, displayText);

        layout.putConstraint(SpringLayout.NORTH, searchText, 0, SpringLayout.NORTH, searchButton);
        layout.putConstraint(SpringLayout.WEST, searchText, 0, SpringLayout.WEST, displayText);
        layout.putConstraint(SpringLayout.EAST, searchText, -10, SpringLayout.WEST, searchButton);
        layout.putConstraint(SpringLayout.SOUTH, searchText, 0, SpringLayout.SOUTH, searchButton);


        // frame
        frame.pack();
        frame.setTitle("Weather Observations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(new Dimension(1024, 768));
        frame.setMinimumSize(new Dimension(1024, 768));
        frame.setResizable(false);

        painting = new Thread(drawPanel);
        painting.start();
        //new javax.swing.Timer(40, drawPanel).start(); // timers too jumpy and unreliable for smooth animations
    }

    public void setDB(Database db) {
        this.db = db;
    }

    public void showGUI() {
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        displayText.setText(welcomeText); // reset the info/welcome text
        if (actionEvent.getActionCommand() == "Load") {
            db.loadObservationsFromHTMLFile();
            this.loadButton.setText("Loaded");
            welcomeText = welcomeText +
                    "<p style=\"color:green;\">Observations are loaded and ready for displaying or searching!</p>";
            displayText.setText(welcomeText);
        } else if (actionEvent.getActionCommand() == "Display") {
            displayObservations();
        } else if (actionEvent.getActionCommand() == "Search") {
            performSearch(searchText.getText());
        }
    }

    private void performSearch(String text) {
        Collection<WeatherObservation> observations;

        text = text.trim(); // trim trailing whitespace

        // show all if searched for nothing
        if (text.length() == 0) {
            displayObservations();
            return;
        }

        // search!
        observations = db.checkWeatherByDate(text);

        // check if was invalid date format (would have returned null)
        if (observations == null) {
            displayText.setText(welcomeText +
                                "<p style=\"color:red;\"><b>Invalid date format!</b></p>" );
        } else {
            if (observations.size() == 0) {
                displayText.setText(welcomeText +
                        String.format("<p style=\"color:red;\"><b>No observations found for %s!</b></p>", text));
            }
            displayTable(observations);
        }

    }

    private void displayObservations() {
        Collection<WeatherObservation> o = db.getObservations();
        if (o.isEmpty()) {
            displayText.setText(welcomeText +
            "<p style=\"color:red;\"><b>No weather observations have been loaded!" +
                    " Try clicking 'Load' to load from the html file first.</b></p>");
        } else {
            displayTable(o);
        }
    }

    private void displayTable(Collection<WeatherObservation> observations) {
        List<WeatherObservation> obs = new ArrayList<>(observations);
        String[] columns = {"Place", "Date", "Temperature", "Humidity", "UV Index", "Wind Speed"};
        Object[][] data = new Object[observations.size()][6];

        // sort list of weather observations by date
        // - note: no longer needed as the observations are autosorted by date
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
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        dataTable.setBackground(new Color(0xD1EFD8));
        scrollPane.setViewportView(dataTable);
        dataTable.setFillsViewportHeight(true);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int c = keyEvent.getKeyCode();
        if (c == KeyEvent.VK_ENTER) {
            performSearch(searchText.getText());
        }
    }

    private class animatedJPanel extends JPanel implements Runnable {
        long offset = 0;
        long startTime = (new Date()).getTime();
        public volatile boolean suspended;
        Animations animations = new Animations();

        // setting the preferred size will set the bounds for the animation as well
        @Override
        public void setPreferredSize(Dimension d) {
            super.setPreferredSize(d);
            animations.init((int) d.getWidth(),(int) d.getHeight());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D pen = (Graphics2D) g;
            offset = ((new Date()).getTime() - startTime);
            animations.setOffset(offset);
            // TODO: logic to pick an animation here (based on selected weather event or something)?
            animations.rain(pen);
        }

        @Override
        public void run() {
            suspended = false;
            while (true) {
                repaint();
                try {
                    Thread.sleep(10);
                    if (suspended) {
                        long one = (new Date()).getTime();
                        while (suspended) {
                            Thread.sleep(50);
                        }
                        long two = (new Date()).getTime();
                        startTime += (two - one); // offset startTime to avoid time jump in animation
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

// class to manage a range of graphics given bounds and a pen (as well as a time offset for animation support)
class Animations {
    int width = 0;
    int height = 0;
    long offset = 0;
    int NDROPS = 200;
    int[][] raindrops = new int[NDROPS][4]; // [x, relative y-offset, speed, size]

    Animations() {

    }

    public void init(int w, int h) {
        width = w;
        height = h;
        
        Random r = new Random((int) (new Date()).getTime());
        for (int i=0; i<NDROPS; i++) {
            raindrops[i][3] = r.nextInt(13) + 3; // size
            raindrops[i][0] = r.nextInt(width-raindrops[i][3]); // x-coord (base right bound on previous size)
            raindrops[i][1] = r.nextInt(height); // y-offset
            raindrops[i][2] = r.nextInt(35) + (31 - raindrops[i][3]*2); // speed (lower = faster)
        }
        Arrays.sort(raindrops, new Comparator<int[]>() {
            @Override public int compare(final int[] one, final int[] two) {
                if (one[3] > two[3]) {
                    return 1;
                } else if (one[3] < two[3]) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public void rain(Graphics2D pen) {

        // sky background
        pen.setColor(new Color(188, 204, 232));
        pen.fillRect(0,0,width,height);

        // draw half the raindrops
        int half = NDROPS/2;
        pen.setColor(new Color(55, 115, 193));
        for (int i=0; i<half; i++) {
            pen.fillOval(raindrops[i][0], (int) (raindrops[i][1]+offset/raindrops[i][2])%height,
                    raindrops[i][3], raindrops[i][3]*2);
        }

        // draw the clouds
        pen.setColor(new Color(71, 90, 116));
        for (int i=-10; i<width; i+=50) {
            pen.fillOval(i, -10, 70, 30);
        }

        // draw rest of raindrops
        pen.setColor(new Color(55, 115, 193));
        for (int i=half; i<NDROPS; i++) {
            pen.fillOval(raindrops[i][0], (int) (raindrops[i][1]+offset/raindrops[i][2])%height,
                    raindrops[i][3], raindrops[i][3]*2);
        }
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}

