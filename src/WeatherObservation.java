

public class WeatherObservation {
    String place = "";
    Double date = 0.0;
    Double temperature = 0.0;
    Double humidity = 0.0;
    Double uvIndex = 0.0;
    Double windSpeed = 0.0;

    public WeatherObservation() {

    }

    @Override
    public String toString() {
        return "WeatherObservation{" +
                "place='" + place + '\'' +
                ", date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", uvIndex=" + uvIndex +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
