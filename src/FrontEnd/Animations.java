package FrontEnd;

import java.awt.*;
import java.util.*;

/** class to manage a range of graphics given bounds and a pen
 * (as well as a time offset for animation support)
 */
public class Animations {
    int width = 0;
    int height = 0;
    long offset = 0; // time offset - auto updated - use for animations
    int NDROPS = 200;
    int NSTARS = 42;
    int[][] raindrops = new int[NDROPS][4]; // [x, relative y-offset, speed, size]
    Image landscape;
    int[][] stars = new int[NSTARS][2];
    ArrayList<String> availableAnimations = new ArrayList<>();

    Animations() {
        landscape = Toolkit.getDefaultToolkit().getImage("countryside.png");
        availableAnimations.add("rain");
        availableAnimations.add("sunny");
    }

    public void init(int w, int h) {
        width = w;
        height = h;

        Random r = new Random((int) (new Date()).getTime());
        for (int i=0; i<NDROPS; i++) {
            raindrops[i][3] = r.nextInt(13) + 3; // size
            raindrops[i][0] = r.nextInt(width-raindrops[i][3]); // x-coord (base right bound on previous size)
            raindrops[i][1] = r.nextInt(height); // y-offset
            raindrops[i][2] = r.nextInt(35) + (31 - raindrops[i][3]*2); // speed (lower = faster)
        }

        for (int i=0; i<NSTARS; i++) {
            stars[i][0] = r.nextInt(width);
            stars[i][1] = r.nextInt(height/2);
        }

        Arrays.sort(raindrops, new Comparator<int[]>() {
            @Override public int compare(final int[] one, final int[] two) {
                if (one[3] > two[3]) {
                    return 1;
                } else if (one[3] < two[3]) {
                    return -1;
                }
                return 0;
            }
        });
    }

    private void rain(Graphics2D pen) {

        // sky background
        pen.setColor(new Color(184, 195, 196));
        pen.fillRect(0,0,width,height);

        // draw half the raindrops
        int quarter = NDROPS/4;
        pen.setColor(new Color(55, 115, 193));
        for (int i=0; i<quarter; i++) {
            pen.fillOval(raindrops[i][0], (int) (raindrops[i][1]+offset/raindrops[i][2])%height,
                    raindrops[i][3], raindrops[i][3]*2);
        }

        // draw the clouds
        pen.setColor(new Color(71, 90, 116));
        for (int i=-10; i<width; i+=50) {
            pen.fillOval(i, -10, 70, 30);
        }

        pen.drawImage(landscape, 0, 0, null);


        // draw rest of raindrops
        pen.setColor(new Color(55, 115, 193));
        for (int i=quarter; i<NDROPS; i++) {
            pen.fillOval(raindrops[i][0], (int) (raindrops[i][1]+offset/raindrops[i][2])%height,
                    raindrops[i][3], raindrops[i][3]);
        }
    }

    private void sunny(Graphics2D pen) {

        // sky background
        pen.setColor(new Color(167, 225, 246));
        pen.fillRect(0,0,width,height);

        int period = (int) ((((offset+15000)/120) % 700));
        int alpha = Math.abs(period-255);

        // sun
        if (200-period > -90 && 200-period < height) {
            pen.setColor(new Color(244, 224, 83));
            pen.fillOval(period / 3, 200 - period, 90, 90);
        } else {
        }

        // the landscape
        pen.drawImage(landscape, 0, 0, null);

        if (alpha > 170) {
            // darkness
            pen.setColor(new Color(0, 0, 0, Math.min(255, alpha)));
            pen.fillRect(0, 0, width, height);

            // stars
            pen.setColor(new Color(255, 255, 255, Math.min(255, alpha/2)));
            for (int i=0; i<NSTARS; i++) {
                pen.drawString("★", stars[i][0], stars[i][1]);
            }
        }
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public boolean available(String a) {
        if (availableAnimations.contains(a)) {
            return true;
        }
        return false;
    }

    public void animate(String animation, Graphics2D pen) {
        if (animation == "rain") {
            rain(pen);
        } else if (animation == "sunny") {
            sunny(pen);
        }
    }
}

