package BackEnd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: implement tree balancing
class BST {
    private Node root;

    public BST() {
        root = null;
    }

    public void add(WeatherObservation o) {
        if (root == null) {
            root = new Node(o);
        } else {
            root.insert(o);
        }
    }

    public Integer size() {
        if (root == null) {
            return 0;
        } else {
            return root.size();
        }
    }

    public ArrayList<WeatherObservation> find(Date d) {
        if (root == null) {
            return new ArrayList<>();
        } else {
            return root.find(d);
        }
    }

    public ArrayList<WeatherObservation> traverse() {
        if (root == null) {
            return new ArrayList<>();
        } else {
            return root.traverse();
        }
    }

    private class Node {

        private ArrayList<WeatherObservation> obs = new ArrayList<>();
        private Node parent;
        private final Date date;
        private Node leftChild = null;
        private Node rightChild = null;

        public Node(WeatherObservation o) {
            obs.add(o);
            this.date = o.getDate();
        }

        public void insert(WeatherObservation o) {
            if (o.getDate().compareTo(date) == 0) {
                obs.add(o);
            } else if (o.getDate().compareTo(date) > 0) {
                if (rightChild == null) {
                    rightChild = new Node(o);
                } else {
                    rightChild.insert(o);
                }
            } else {
                if (leftChild == null) {
                    leftChild = new Node(o);
                } else {
                    leftChild.insert(o);
                }
            }
        }

        @Override
        public String toString() {
            String s = "";
            if (leftChild != null) {
                s += leftChild.toString();
            }
            for (WeatherObservation o: obs) {
                s += o + "\n";
            }
            if (rightChild != null) {
                s += rightChild.toString();
            }

            return s;

        }

        public Integer size() {
            Integer sum = 1;
            if (leftChild != null) {
                sum += leftChild.size();
            }
            if (rightChild != null) {
                sum += rightChild.size();
            }
            return sum;
        }

        public ArrayList<WeatherObservation> find(Date d) {
            if (d.compareTo(date) == 0) {
                return obs;
            } else if (d.compareTo(date) > 0 && rightChild != null) {
                return rightChild.find(d);
            } else if (d.compareTo(date) < 0 && leftChild != null) {
                return leftChild.find(d);
            } else {
                return new ArrayList<>();
            }
        }

        public ArrayList<WeatherObservation> traverse() {
            ArrayList<WeatherObservation> a = new ArrayList<>();

            if (leftChild != null) {
                a.addAll(leftChild.traverse());
            }

            a.addAll(obs);

            if (rightChild != null) {
                a.addAll(rightChild.traverse());
            }

            return a;
        }
    }

}



public class WeatherHistory implements Serializable, Database {
    BST history = new BST();
    private static Logger logger = Logger.getLogger(WeatherHistory.class.getName());

    public WeatherHistory() {

    }

    @Override
    public void loadObservationsFromHTMLFile() {

        String htmlData = "";

        // for loading from local file
        /*
        FileReader fr = null;
        BufferedReader bfr = null;
        try {
            String filename = "observations.html";
            fr = new FileReader(new File(filename));
            bfr = new BufferedReader(fr);
            String line;
            while ((line = bfr.readLine()) != null) {
                htmlData += line;
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "FileNotFoundException [{0}]", e.getMessage());
            return;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            return;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            }
            try {
                if (bfr != null) {
                    bfr.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            }
        }
        */


        // load from the given url
        InputStreamReader isr = null;
        BufferedReader r = null;
        try {
            String urlString = "http://rengland.spinetail.cdu.edu.au/observations/";
            URL url = new URL(urlString);
            URLConnection connect = url.openConnection();
            isr = new InputStreamReader(connect.getInputStream());
            r = new BufferedReader(isr);
            String line;
            while ((line = r.readLine()) != null) {
                htmlData += line;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            return;
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            }
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            }
        }


        try {
            Document document = Jsoup.parse(htmlData);
            Elements entries = document.getElementsByTag("tr");
            boolean firstEntry = true;
            for (Element entry : entries) {
                // ignore first entry - it's the table header
                if (firstEntry) {
                    firstEntry = false;
                    continue;
                }

                String place = entry.child(0).text();
                String date  = entry.child(1).text();
                Double temperature = Double.parseDouble(entry.child(2).text());
                Double humidity = Double.parseDouble(entry.child(3).text());
                Double uvIndex = Double.parseDouble(entry.child(4).text());
                Double windSpeed = Double.parseDouble(entry.child(5).text());

                WeatherObservation obs = new WeatherObservation(place, date, temperature, humidity, uvIndex, windSpeed);
                this.addObservation(obs);

            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Parse Exception [{0}]", e.getMessage());
        }
    }

    @Override
    public Collection<WeatherObservation> getObservations() {
        return this.getHistory();
    }

    // returns null if invalid/unsupported date format, or list of observations found (could be empty list)
    @Override
    public Collection<WeatherObservation> checkWeatherByDate(String date) {

        Date d;
        try {
            d = new SimpleDateFormat("dd/mm/yyyy").parse(date);
        } catch (ParseException e) {
            try {
                d = new SimpleDateFormat("yyyy-mm-dd").parse(date);
            } catch (ParseException e2) {
                //logger.log(Level.INFO, "unrecognized date format");
                return null;
            }
        }
        return history.find(d);
    }


    @Override
    public String toString() {
        if (history == null) {
            return "No observations loaded!";
        }
        return history.toString();
    }


    public Collection<WeatherObservation> getHistory() {
        return history.traverse();
    }

    public void addObservation(WeatherObservation w) {
        history.add(w);
    }

    public Integer getHistorySize() {
        return history.size();
    }


    private String gen_padding(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(" ");
        }
        return s.toString();
    }

    public void clear() {
        this.history = null;
    }

    /**
     * serializes the history ArrayList to file
     * @param filename file to save to
     * @return returns true if able to save, else false
     */
    public Boolean saveToSerialized(String filename) {

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(this.history);
            os.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * loads the previously saved serialized history list
     * @param filename file to load from
     * @return returns true if successful load
     */
    public Boolean loadFromSerialized(String filename) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
            BST temp = null;
            try {
                temp = (BST) is.readObject();
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "ClassNotFoundException [{0}]", e.getMessage());
                return false;
            }
            is.close();

            // only save to history if for sure no previous errors occurred
            this.history = temp;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            return false;
        }
        return true;
    }

    private ArrayList<WeatherObservation> getDataFromFile(String filename) {
        File theFile = new File(filename);
        FileReader fileReader = null;
        BufferedReader reader = null;
        String line = null;

        try {
            fileReader = new FileReader(theFile);
            reader = new BufferedReader(fileReader);

            /* NOTE: lecture slides allude to the input file being tab deliminated,
             *       but sample file given only contains spaces.
             *       Due to this, and the fact that place names can have spaces, this regex solution is used.
             *       Any combination of tabs/spaces are handled, as long as the correct order is kept in file data.
             */
            Pattern format = Pattern.compile("^\\s*(.+?)\\s+(\\d{1,2}/\\d{1,2}/\\d{4})\\s+([^\\s]+\\s+[^\\s]+\\s+[^\\s]+\\s+[^\\s]+)\\s*$");

            // also handle comments, headers, and blank lines!
            Pattern comment = Pattern.compile("^\\s*(place\\s+date\\s+temperature\\s+humidity\\s+uvindex\\s+windspeed|#.*|//.*|)\\s*$", Pattern.CASE_INSENSITIVE);
            ArrayList<WeatherObservation> tempArray = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                Matcher m = format.matcher(line);
                if (m.matches()) {
                    String place = m.group(1);
                    Date dateDate = new SimpleDateFormat("dd/mm/yyyy").parse(m.group(2));
                    String date = new SimpleDateFormat("dd/mm/yyyy").format(dateDate);

                    String[] other = m.group(3).split("\\s+");
                    Double temperature = Double.parseDouble(other[0]);
                    Double humidity = Double.parseDouble(other[1]);
                    Double uvIndex = Double.parseDouble(other[2]);
                    Double windSpeed = Double.parseDouble(other[3]);

                    WeatherObservation w = new WeatherObservation(place, date, temperature, humidity, uvIndex, windSpeed);
                    tempArray.add(w);
                } else if (comment.matcher(line).matches()) {
                    // ignore
                } else {
                    throw new ParseException("Invalid format in file.", -1);
                }
            }

            // actually update the history array now no exception found
            return tempArray;

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "File Not Found [{0}]", e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
        } catch (ParseException e) {
            // when date or other data wrong format
            logger.log(Level.SEVERE, "Parse Exception [{0}]", e.getMessage());
        } finally {
            try {
                if (fileReader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            }

        }
        return null;
    }

    /**
     * Loads weather history from file - overwrites all current data
     * @param filename name of file to read from
     * @return returns true if successful, else false
     */
    public Boolean loadFromFile(String filename) {
        ArrayList<WeatherObservation> data = this.getDataFromFile(filename);
        if (data == null) {
            return false;
        }
        history = new BST();
        for (WeatherObservation o: data) {
            history.add(o);
        }
        return true;
    }

    /**
     * adds all observations from file to current history - doesn't overwrite
     * @param filename name of file to read from
     * @return returns true if successful, else false
     */
    public Boolean addFromFile(String filename) {
        ArrayList<WeatherObservation> data = this.getDataFromFile(filename);
        if (data == null) {
            return false;
        }
        for (WeatherObservation o: data) {
            history.add(o);
        }
        return true;
    }

    /**
     * Saves current history to file
     * @param filename name of file to save to
     * @return returns true if successful save, else false
     */
    public Boolean saveToFile(String filename) {
        File theFile = new File(filename);
        FileWriter fileWrite = null;
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(theFile));
            writer.write("Place Data Temperature Humidity UVIndex WindSpeed\n");
            for (WeatherObservation w: history.traverse()) {
                writer.write(w.formattedString() + "\n");
            }

            return true;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
        } finally {

            try {
                if (writer != null) {
                    writer.close();
                }
            }
            catch (IOException e) {
                logger.log(Level.SEVERE, "IOException [{0}]", e.getMessage());
            }
        }

        return false;
    }

}
