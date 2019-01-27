package com.alevel;


import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class HorseRace {
    //Создаем CountDownLatch на 8 "условий"
    private static CountDownLatch START ;
    //Условная длина гоночной трассы
    private static final int trackLength = 1000;

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Quantity of horses: ");
        int quantity = scanner.nextInt();

        START = new CountDownLatch(quantity+1);
        for (int i = 1; i <= quantity; i++) {
            new Thread(new Horse(i)).start();
            Thread.sleep(1000);
        }

        while (START.getCount() > 1 ) //Проверяем, собрались ли все автомобили
            Thread.sleep(100);              //у стартовой прямой. Если нет, ждем 100ms

        Thread.sleep(1000);
        System.out.println("На старт!");

        START.countDown();//Команда дана, уменьшаем счетчик на 1
        //счетчик становится равным нулю, и все ожидающие потоки
        //одновременно разблокируются
    }

    public static class Horse implements Runnable {
        private int number;


        public Horse(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к стартовой прямой.\n", number);
                //Автомобиль подъехал к стартовой прямой - условие выполнено
                //уменьшаем счетчик на 1
                START.countDown();
                //метод await() блокирует поток, вызвавший его, до тех пор, пока
                //счетчик CountDownLatch не станет равен 0
                START.await();
                int remainedDist = trackLength;
                Random random = new Random();
                do {
                    remainedDist -= (random.nextInt(20) + 30);

                    Thread.sleep(random.nextInt(100) + 300);
                } while (remainedDist > 0);
                System.out.printf("Horse №%d финишировал!\n", number);
            } catch (InterruptedException e) {
            }
        }
    }
}
