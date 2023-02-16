import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private static final int BULK_CREATE_BALLS_NUMBER = 1000;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    public BallCanvas canvas;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");

        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        JButton buttonBulkCreate = new JButton("Bulk Create");
        JButton buttonJoinVisualization = new JButton("Join Visualization");

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ball b = new Ball(canvas);
                canvas.add(b);

                BallThread thread = new BallThread(b);
                thread.start();
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonBulkCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var ball = new Ball(canvas, Color.RED, false);

                canvas.add(ball);

                BallThread thread = new BallThread(ball);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();

                for (int i = 0; i < BULK_CREATE_BALLS_NUMBER; i++) {
                    Ball b = new Ball(canvas, Color.BLUE, false);
                    canvas.add(b);

                    thread = new BallThread(b);
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.start();
                }
            }
        });

        buttonJoinVisualization.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ball b1 = new Ball(canvas, Color.GREEN, true);
                Ball b2 = new Ball(canvas, Color.BLUE, true);

                canvas.add(b1);
                canvas.add(b2);

                var thread1 = new BallThread(b1);
                var thread2 = new BallThreadJoined(b2, thread1);

                thread1.setName("GREEN");
                thread2.setName("BLUE");

                thread1.start();
                thread2.start();
            }
        });

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(buttonBulkCreate);
        buttonPanel.add(buttonJoinVisualization);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}