package BackEnd;

/**
 * the database interface used by the frontend
 */

public interface Database {

    public void loadObservationsFromHTMLFile();

    public String getObservations();

}
