
public class TestClasses {


    public static void main(String[] args) {
        WeatherObservation w = new WeatherObservation();
        WeatherObservation w2 = new WeatherObservation();
        System.out.println(w);

        WeatherHistory history = new WeatherHistory();
        history.addObservation(w);
        history.addObservation(w2);

        System.out.println(history);

    }

}
