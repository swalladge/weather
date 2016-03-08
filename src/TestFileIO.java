import java.text.ParseException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestFileIO {
    private static Logger logger = Logger.getLogger(TestFileIO.class.getName());


    public static void main(String[] args) throws ParseException {
        logger.getParent().setLevel(Level.ALL);

        WeatherHistory history = new WeatherHistory();

        history.loadFromFile("observations.txt");

        System.out.println(history);

        if (history.loadFromFile("doesnotexist.fail")) {
            logger.log(Level.WARNING, "loadFromFile returned wrong result");
        } else {
            logger.log(Level.FINE, "loadFromFile successfully handled missing file");
        }

    }

}
