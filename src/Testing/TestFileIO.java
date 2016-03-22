package Testing;

import BackEnd.WeatherHistory;
import BackEnd.WeatherObservation;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestFileIO {
    private static Logger logger = Logger.getLogger(TestFileIO.class.getName());


    public static void main(String[] args) throws ParseException {
        logger.getParent().setLevel(Level.ALL);

        // make a new weatherhistory object and load stuff from the file
        WeatherHistory history = new WeatherHistory();
        history.loadFromFile("observations.txt");
        System.out.println(history);

        // add more observations - should not overwrite previous data
        history.addFromFile("other-observations.txt");
        System.out.println(history);

        // now direct load other observations - should now only contain stuff from this file
        history.loadFromFile("other-observations.txt");
        System.out.println(history);

        // add another observation
        WeatherObservation w1 = new WeatherObservation("Sydney", "2016-02-01", 32.0, 45.0, 11.0, 10.5);
        history.addObservation(w1);

        // save it to file!
        System.out.println(history);
        history.saveToFile("temp/savedObservations.txt");

        // testing try/catch
        if (history.loadFromFile("doesnotexist.fail")) {
            logger.log(Level.WARNING, "loadFromFile returned wrong result");
        } else {
            logger.log(Level.FINE, "loadFromFile successfully handled missing file");
        }

    }

}
