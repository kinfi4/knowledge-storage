package threads;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficLightTask {
    public static void main(String[] args) {

    }

    class CrossRoad {
        public final TrafficLight trafficLight;
        public final ArrayDeque<Car> carsQueue;
        CrossRoad(TrafficLight trafficLight, ArrayDeque<Car> initCarsQueue) {
            this.trafficLight = trafficLight;
            this.carsQueue = initCarsQueue;
        }

        public synchronized void carBackToQueue(Car car) {
            carsQueue.add(car);
        }
        public synchronized void makeMePassedCrossRoad() {
            carsQueue.pollFirst();
        }
        public synchronized boolean carCanMove() {
            return this.trafficLight.waitUntilCarCanMove();
        }
    }

    class TrafficLight extends Thread {
        public final ReentrantLock lock = new ReentrantLock();
        public final Condition conditionGo = lock.newCondition();
        private String currentLight = "green";
        private String previousColor = "";
        @Override
        public void run() {
            lock.lock();

            try {
                if (currentLight.equals("green")) {
                    conditionGo.awaitNanos(60_000_000L);
                    currentLight = "yellow";
                    previousColor = "green";

                } else if ((currentLight.equals("yellow"))&&(previousColor.equals("green"))) {
                    conditionGo.awaitNanos(10_000_000L);
                    currentLight = "red";
                    previousColor = "yellow";

                }
                else if ((currentLight.equals("yellow"))&&(previousColor.equals("red"))) {
                    conditionGo.awaitNanos(10_000_000L);
                    currentLight = "green";
                    previousColor = "yellow";
                    conditionGo.signalAll();  // light is green, let's go

                } else if (currentLight.equals("red")) {
                    conditionGo.awaitNanos(40_000_000L);
                    currentLight = "yellow";
                    previousColor = "red";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(currentLight);
            }
        }

        public boolean waitUntilCarCanMove() {
            while(!currentLight.equals("green")) {
                try {
                    this.conditionGo.await();
                } catch (InterruptedException e) {}
            }

            return true;
        }
    }

    class Car {
        public final int id;
        Car(int id) {
            this.id = id;
        }
    }
}
