import java.text.ParseException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestClasses {
    private static Logger logger = Logger.getLogger(TestClasses.class.getName());


    public static void main(String[] args) throws ParseException {
        logger.getParent().setLevel(Level.ALL);

        // init a few weather observations (made up)
        WeatherObservation w1 = new WeatherObservation("Sydney", "2016-02-01", 32.0, 45.0, 11.0, 10.5);
        WeatherObservation w2 = new WeatherObservation("Alice Sprints", "2016-01-03", 42.0, 8.0, 17.0, 15.3);
        WeatherObservation w3 = new WeatherObservation("Broken Hill", "2016-02-16", 31.0, 25.0, 15.0, 10.5);
        WeatherObservation w4 = new WeatherObservation("Adelaide", "2016-01-24", 35.0, 20.0, 12.0, 21.0);

        // build a history of observations
        WeatherHistory history = new WeatherHistory();
        history.addObservation(w1);
        history.addObservation(w2);
        history.addObservation(w4);

        // test insert
        history.insertObservation(2, w3);

        // see what it looks like!
        System.out.println(history);

        // let's get an observation and play with it
        WeatherObservation fixThis = history.getObservation(1);
        System.out.println("Broken? - " + fixThis);
        fixThis.setHumidity(15.0);
        fixThis.setDate("2016-01-12");
        fixThis.setPlace("Alice Springs");
        System.out.println("Fixed?  - " + fixThis);


        // nah broken beyond repair
        history.removeObservation(1);
        System.out.println(history);

        // get some random stats
        Random generator = new Random();

        WeatherObservation obs1 = history.getObservation(generator.nextInt(history.getHistorySize()));
        System.out.printf("Random correlation: wind speed was %skm/h on %s in %s!%n", obs1.getWindSpeed(), obs1.getISOdate(), obs1.getPlace());

        WeatherObservation obs2 = history.getObservation(generator.nextInt(history.getHistorySize()));
        System.out.printf("Random correlation: relative humidity was %s%% on a %s degree day in %s!%n", obs2.getHumidity(), obs2.getTemperature(), obs2.getPlace());

        // save the weather history as it is currently
        System.out.println("saving history");
        String saveFile = "serializeddata.ser";
        history.saveToSerialized(saveFile);
        System.out.println(history);

        // remove some observations
        System.out.println("removing some history");
        history.removeObservation(0);
        history.removeObservation(0);
        history.removeObservation(0);
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
