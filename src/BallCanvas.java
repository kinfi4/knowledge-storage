import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class BallCanvas extends JPanel {
    public static final int XSIZE = 20;
    public static final int YSIZE = 20;

    public ArrayList<Integer[]> holesPosition = new ArrayList<>(3);
    private final ArrayList<Ball> balls = new ArrayList<>();

    public void add(Ball b){
        this.balls.add(b);
    }

    public void remove(Ball b) {
        this.balls.remove(b);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        try {
            for (Ball b : balls) {
                b.draw(g2);
            }
        } catch (ConcurrentModificationException ignored) {}

        for(Integer[] holePosition : this.holesPosition) {
            g2.setColor(Color.RED);
            g2.fill(new Ellipse2D.Double(holePosition[0], holePosition[1], XSIZE, YSIZE));
        }
    }
}