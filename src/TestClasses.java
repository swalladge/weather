import java.text.ParseException;
import java.util.Date;

public class TestClasses {


    public static void main(String[] args) throws ParseException {

        // init a few weather observations (made up)
        WeatherObservation w1 = new WeatherObservation("Sydney", "2016-02-01", 32.0, 45.0, 11.0, 10.5);
        WeatherObservation w2 = new WeatherObservation("Alice Springs", "2016-01-03", 42.0, 8.0, 17.0, 15.3);
        WeatherObservation w3 = new WeatherObservation("Broken Hill", "2016-02-16", 31.0, 25.0, 15.0, 10.5);
        WeatherObservation w4 = new WeatherObservation("Adelaide", "2016-01-24", 35.0, 20.0, 12.0, 21.0);

        WeatherHistory history = new WeatherHistory();
        history.addObservation(w1);
        history.addObservation(w2);
        history.addObservation(w3);
        history.addObservation(w4);

        System.out.println(history);

    }

}
