package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {
    public static ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100_000);

                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        Thread thread1 = new Thread(() -> {
            char a = 'a';
            int maxA = findMaxCharCount(queue1, a);
            System.out.println("Максималльное колличество символов " + a + " составляет " + maxA);
        });

        thread1.start();

        Thread thread2 = new Thread(() -> {
            char b = 'b';
            int maxA = findMaxCharCount(queue2, b);
            System.out.println("Максималльное колличество символов " + b + " составляет " + maxA);
        });

        thread2.start();

        Thread thread3 = new Thread(() -> {
            char c = 'c';
            int maxA = findMaxCharCount(queue3, c);
            System.out.println("Максималльное колличество символов " + c + " составляет " + maxA);
        });

        thread3.start();
    }

    public static int findMaxCharCount(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10_000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted");
            return -1;
        }
        return max;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}