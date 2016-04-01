package BackEnd;

import java.util.Collection;

/**
 * the database interface used by the frontend
 */
public interface Database {

    public void loadObservationsFromHTMLFile();

    public Collection<WeatherObservation> getObservations();

}
