package Testing;

import BackEnd.WeatherHistory;
import BackEnd.WeatherObservation;

import java.text.ParseException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestClasses {
    private static Logger logger = Logger.getLogger(TestClasses.class.getName());


    public static void main(String[] args) throws ParseException {
        logger.getParent().setLevel(Level.ALL);

        // init a few weather observations (made up)
        WeatherObservation w1 = new WeatherObservation("Sydney", "01/02/2016", 32.0, 45.0, 11.0, 10.5);
        WeatherObservation w2 = new WeatherObservation("Alice Sprints", "01/03/2016", 42.0, 8.0, 17.0, 15.3);
        WeatherObservation w3 = new WeatherObservation("Broken Hill", "02/16/2016", 31.0, 25.0, 15.0, 10.5);
        WeatherObservation w4 = new WeatherObservation("Adelaide", "24/01/2016", 35.0, 20.0, 12.0, 21.0);

        // build a history of observations
        WeatherHistory history = new WeatherHistory();
        history.addObservation(w1);
        history.addObservation(w2);
        history.addObservation(w4);

        // see what it looks like!
        System.out.println(history);


        // nah broken beyond repair
        System.out.println(history);

        // save the weather history as it is currently
        System.out.println("saving history");
        String saveFile = "serializeddata.ser";
        history.saveToSerialized(saveFile);
        System.out.println(history);

        // load the previously saved data
        System.out.println("loaded saved history");
        history.loadFromSerialized(saveFile);
        System.out.println(history);


        // testing loading from non-existant file, and also testing the logger
        Boolean ok = history.loadFromSerialized("randomaoenuhaonuh");
        if (ok) {
            System.out.println("successfully loaded data from 'randomaoenuhaonuh");
        } else {
            System.out.println("couldn't load data from 'randomaoenuhaonuh - file not found or invalid data");
        }

    }

}
