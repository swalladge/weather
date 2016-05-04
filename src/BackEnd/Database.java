package BackEnd;

import java.util.Collection;

/**
 * the database interface used by the frontend
 */
public interface Database {

    public boolean loadObservationsFromHTMLFile();

    public Collection<WeatherObservation> getObservations();

    public Collection<WeatherObservation> checkWeatherByDate(String date);

}
