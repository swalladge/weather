import java.util.ArrayList;

public class WeatherHistory {
    ArrayList<WeatherObservation> history = new ArrayList<>();

    public WeatherHistory() {

    }

    public ArrayList<WeatherObservation> getHistory() {
        return history;
    }

    public void addObservation(WeatherObservation w) {
        history.add(w);
    }

    public void insertObservation(int index, WeatherObservation w) {
        history.add(index, w);
    }

    public Integer getHistorySize() {
        return history.size();
    }

    public WeatherObservation getObservation(int index) {
        return history.get(index);
    }

    public void setObservation(int index, WeatherObservation w) {
        history.set(index, w);
    }

    public void removeObservation(int index) {
        history.remove(index);
    }

    private String gen_padding(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(" ");
        }
        return s.toString();
    }

    @Override
    public String toString() {
        String output = "--------------\n| WeatherHistory\n| " + this.getHistorySize() + " weather observations\n";

        int max = 0;

        for (WeatherObservation item: history) {
            String place = item.getPlace();
            int i = place.length();
            if (i > max) {
                max = i;
            }
        }

        for (WeatherObservation o: history) {
            String place = o.getPlace();
            int padding = max - place.length();
            output = output + "|- " + this.gen_padding(padding) + o + "\n";
        }
        output += "--------------";

        return output;

    }
}
