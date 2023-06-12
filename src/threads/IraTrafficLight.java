package threads;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IraTrafficLight {
    public static void main(String[] args) {
        TrafficLight2 trafficLight = new TrafficLight2();

        trafficLight.start();

        for (int i = 1; i <= 500; i++) {
            Car2 car = new Car2(i, trafficLight);
            car.start();
        }
    }
}

class TrafficLight2 extends Thread {
    private boolean isGreen = true;

    public synchronized void changeLight() {
        isGreen = !isGreen;
        notifyAll();
    }

    public synchronized void verifyThatColorIsGreen() {
        while (!isGreen) {
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
                this.changeLight();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Car2 extends Thread {
    private static final Random rand = new Random();
    private final int carId;
    private final TrafficLight2 trafficLightIra;

    public Car2(int carId, TrafficLight2 trafficLightIra) {
        this.carId = carId;
        this.trafficLightIra = trafficLightIra;
    }

    @Override
    public void run() {
        while (true) {
            try {
                trafficLightIra.verifyThatColorIsGreen();

                System.out.println("Car " + carId + " is crossing the intersection...");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Car " + carId + " has crossed the intersection.");
                TimeUnit.SECONDS.sleep(rand.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}