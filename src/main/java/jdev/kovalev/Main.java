package jdev.kovalev;

import jdev.kovalev.exception.ClosedBufferException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        RingBuffer<Integer> buffer = new RingBuffer<>(10);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> {
            int i = 0;
            while (i < 20) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    buffer.put(i);
                    System.out.printf("Поток %s добавил элемент %d \n", Thread.currentThread().getName(), i);
                    i++;
                } catch (ClosedBufferException e) {
                    System.err.println(e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        executorService.execute(() -> {
            int i = 20;
            while (i < 50) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    buffer.put(i);
                    System.out.printf("Поток %s добавил элемент %d \n", Thread.currentThread().getName(), i);
                    i++;
                } catch (ClosedBufferException e) {
                    System.err.println(e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        executorService.execute(() -> {
            int counter = 0;
            while (counter < 50) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Integer e = buffer.take();
                    System.out.printf("Поток %s прочитал %d \n", Thread.currentThread().getName(), e);
                    counter++;
                } catch (ClosedBufferException e) {
                    System.err.println(e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Все задачи выполнены успешно.");
    }
}