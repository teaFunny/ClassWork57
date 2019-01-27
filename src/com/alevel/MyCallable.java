package com.alevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        String name = Thread.currentThread().getName();
        // возвращает имя потока, который выполняет callable таск
        return name;
    }

    public static void main(String args[]){
        //Получаем ExecutorService утилитного класса Executors с размером gпула потоков равному 10
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        //создаем список с Future, которые ассоциированы с Callable
        List<Future<String>> list = new ArrayList<>();
        // создаем экземпляр MyCallable
        Callable<String> callable = new MyCallable();
        for(int i=0; i< 100; i++){
            //сабмитим Callable таски, которые будут
            //выполнены пулом потоков
            Future<String> future = threadPool.submit(callable);
            //добавляя Future в список,
            //мы сможем получить результат выполнения
            list.add(future);
        }
        for(Future<String> fut : list){
            try {
                System.out.println(fut.isDone());
                // печатаем в консоль возвращенное значение Future
                // будет задержка в 1 секунду, потому что Future.get()
                // ждет пока таск закончит выполнение
                System.out.println(new Date()+ "::" + fut.get());
                System.out.println(fut.isDone());
                System.out.println("");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        threadPool.shutdown();
    }

}