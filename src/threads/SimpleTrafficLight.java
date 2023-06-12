package threads;

import java.util.concurrent.atomic.AtomicInteger;

class TrafficLight implements Runnable {
    private String currentLight = "green";
    private String prevcollor = "";

    public synchronized void changeLight() {
        try {
            if (currentLight.equals("green")) {
                wait(600);
                currentLight = "yellow";
                prevcollor = "green";
            } else if ((currentLight.equals("yellow"))&&(prevcollor.equals("green"))) {
                wait(100);
                currentLight = "red";
            }
            else if ((currentLight.equals("yellow"))&&(prevcollor.equals("red"))) {
                wait(100);
                currentLight = "green";
                notifyAll();  // light is green, let's go

            } else if (currentLight.equals("red")) {
                wait(400);
                currentLight = "yellow";
                prevcollor = "red";

            }

            System.out.println("Traffic light is " + currentLight);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            changeLight();

            if(SimpleTrafficLight.carsPassedCrossRoad.incrementAndGet() >= 1000) {
                System.exit(0);
            }
        }
    }
    public synchronized boolean verifyThatLightIsGreen() {
        while (!currentLight.equals("green")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

class Car implements Runnable {
    private final TrafficLight trafficLight;
    private static int count = 0;
    private final int id;

    public Car(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
        this.id = ++count;
    }

    public void go() {
        try {
            System.out.println("Car "+ Thread.currentThread().getName() + " is moving...");
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(SimpleTrafficLight.carsPassedCrossRoad.get() < 100) {
            while (this.trafficLight.verifyThatLightIsGreen()) {
                System.out.println(SimpleTrafficLight.carsPassedCrossRoad);

                go();
            }

            System.out.println("Car " + id + " passed the traffic light.");
            SimpleTrafficLight.carsPassedCrossRoad.incrementAndGet();
        }
    }
}

public class SimpleTrafficLight {
    public static AtomicInteger carsPassedCrossRoad = new AtomicInteger(0);

    public static void main(String[] args) {
        TrafficLight trafficLight = new TrafficLight();

        Thread lightThread = new Thread(trafficLight);
        lightThread.start();

        for (int i = 0; i < 100; i++) {
            Thread carThread = new Thread(new Car(trafficLight));
            carThread.start();
        }
    }
}