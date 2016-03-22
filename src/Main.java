import BackEnd.Database;
import BackEnd.WeatherHistory;
import FrontEnd.GUI;

/**
 * The main class - run this file for the main program!
 */
public class Main {

    public static void main(String[] args) {

        // init the database
        Database db = new WeatherHistory();

        GUI gui = new GUI();
        gui.setDB(db);

        gui.showGUI();
    }
}
