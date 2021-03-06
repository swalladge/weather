package BackEnd;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class WeatherObservation implements Serializable, Comparable<WeatherObservation> {
    private String place = "";
    private Date date = new Date();
    private Double temperature = 0.0;
    private Double humidity = 0.0;
    private Double uvIndex = 0.0;
    private Double windSpeed = 0.0;
    private static Logger logger = Logger.getLogger(WeatherObservation.class.getName());

    public WeatherObservation() {

    }

    public WeatherObservation(String place, String date, Double temperature, Double humidity, Double uvIndex, Double windSpeed) throws ParseException {
        this.place = place;

        // this constructor uses a string for the date for ease of testing
        // - unsure of how will be used in future, so storing as date object
        this.date = new SimpleDateFormat("dd/mm/yyyy").parse(date);

        this.temperature = temperature;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.windSpeed = windSpeed;
    }

    public Date getDate() {
        return date;
    }

    public String getISODate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        return format.format(this.date);
    }

    public Integer getDateAsInt() {
        SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
        return Integer.parseInt(format.format(this.date));
    }

    @Override
    public int compareTo(WeatherObservation other) {
        // sorts by date (newest first)
        int d1 = other.getDateAsInt();
        int d2 = this.getDateAsInt();
        if (d1 == d2) {
            return this.getPlace().compareTo(other.getPlace());
        }
        return d1 - d2;
    }

    public String getNormalDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        return format.format(this.date);
    }

    public void setISODate(String date) throws ParseException {
        this.date = new SimpleDateFormat("yyyy-mm-dd").parse(date);
    }

    public void setNormalDate(String date) throws ParseException {
        this.date = new SimpleDateFormat("dd/mm/yyyy").parse(date);
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
        //DecimalFormat dFormat = new DecimalFormat("##0.0");
        String dFormat = "%4.1f";
        String output = place + " on " + this.getISODate() + ": " +
                String.format(dFormat, temperature) + "°C, " +
                String.format(dFormat, humidity) + " RH, " +
                String.format(dFormat, uvIndex) + " UV, " +
                String.format(dFormat, windSpeed) + "km/h wind speed";

        return output;
    }

    public String formattedString() {
        return String.format("%s %s %s %s %s %s", place, getNormalDate(), temperature, humidity, uvIndex, windSpeed);
    }
}
