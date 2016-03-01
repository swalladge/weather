import java.util.ArrayList;

public class WeatherHistory {
    ArrayList<WeatherObservation> history = new ArrayList<>();

    public WeatherHistory() {

    }

    public void addObservation(WeatherObservation w) {
        history.add(w);
    }

    @Override
    public String toString() {
        return "WeatherHistory{" +
                "history=" + history +
                '}';
    }
}
