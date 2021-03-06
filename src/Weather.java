import BackEnd.Database;
import BackEnd.WeatherHistory;
import FrontEnd.GUI;

/**
 * The main class - run this file for the main program!
 */
public class Weather {

    public static void main(String[] args) {

        // init the database
        Database db = new WeatherHistory();

        // setup the gui and connect to the database
        GUI gui = new GUI();
        gui.setDB(db);

        // show the gui!
        gui.showGUI();
    }
}
