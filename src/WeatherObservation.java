import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherObservation {
    private String place = "";
    private Date date = new Date();
    private Double temperature = 0.0;
    private Double humidity = 0.0;
    private Double uvIndex = 0.0;
    private Double windSpeed = 0.0;

    public WeatherObservation() {

    }

    public WeatherObservation(String place, String date, Double temperature, Double humidity, Double uvIndex, Double windSpeed) throws ParseException {
        this.place = place;

        // this constructor uses a string for the date for ease of testing
        // - unsure of how will be used in future, so storing as date object
        this.date = new SimpleDateFormat("yyyy-mm-dd").parse(date);

        this.temperature = temperature;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.windSpeed = windSpeed;
    }

    public Date getDate() {
        return date;
    }

    public String getISOdate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        return format.format(this.date);
    }

    public void setDate(String date) throws ParseException {
        this.date = new SimpleDateFormat("yyyy-mm-dd").parse(date);
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Double uvIndex) {
        this.uvIndex = uvIndex;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        DecimalFormat dFormat = new DecimalFormat("0.#");
        String output = place + " on " + this.getISOdate() + " the weather was: " +
                dFormat.format(temperature) + "°C, " +
                dFormat.format(humidity) + " RH, " +
                dFormat.format(uvIndex) + " UV, " +
                dFormat.format(windSpeed) + "km/h wind speed";

        return output;
    }
}
