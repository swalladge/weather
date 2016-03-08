import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherHistory implements Serializable {
    ArrayList<WeatherObservation> history = new ArrayList<>();
    private static Logger logger = Logger.getLogger(WeatherHistory.class.getName());

    public WeatherHistory() {

    }

    public ArrayList<WeatherObservation> getHistory() {
        return history;
    }

    public void addObservation(WeatherObservation w) {
        history.add(w);
    }

    public void insertObservation(int index, WeatherObservation w) {
        history.add(index, w);
    }

    public Integer getHistorySize() {
        return history.size();
    }

    public WeatherObservation getObservation(int index) {
        return history.get(index);
    }

    public void setObservation(int index, WeatherObservation w) {
        history.set(index, w);
    }

    public void removeObservation(int index) {
        history.remove(index);
    }

    private String gen_padding(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(" ");
        }
        return s.toString();
    }

    public void clear() {
        this.history.clear();
    }

    /**
     * serializes the history ArrayList to file
     * @param file - file to save to
     * @return returns true if able to save
     */
    public Boolean saveToSerialized(String file) {

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
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
     * @param file - file to load from
     * @return returns true if successful load
     */
    public Boolean loadFromSerialized(String file) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            ArrayList temp = null;
            try {
                temp = (ArrayList) is.readObject();
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

    public Boolean loadFromFile(String filename) {
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
                    String date = new SimpleDateFormat("yyyy-mm-dd").format(dateDate);

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
            this.history = tempArray;
            return true;

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
        return false;
    }

    public Boolean saveToFile(String filename) {
        File theFile = new File(filename);
        FileWriter fileWrite = null;
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(theFile));
            writer.write("Place Data Temperature Humidity UVIndex WindSpeed\n");
            for (WeatherObservation w: this.history) {
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

    @Override
    public String toString() {
        String output = "--------------\n| WeatherHistory\n| " + this.getHistorySize() + " weather observations\n";

        int max = 0;

        for (WeatherObservation item: history) {
            String place = item.getPlace();
            int i = place.length();
            if (i > max) {
                max = i;
            }
        }

        for (WeatherObservation o: history) {
            String place = o.getPlace();
            int padding = max - place.length();
            output = output + "|- " + this.gen_padding(padding) + o + "\n";
        }
        output += "--------------";

        return output;

    }
}
