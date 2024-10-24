package LTM_07;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynchronousExample {
    public static void main(String[] args) {
        String[] fileUrls = {
                "http://testa.roberta.free.fr/My%20Books/Computer%20programming/Java/Addison%20Wesley%20-%20Java%20Network%20Programming%20and%20Distributed%20Computing.pdf",
                "https://wpollock.com/AJava/MultiThreadingInJava.pdf",
                "https://github.com/AngelSanchezT/books-1/blob/master/concurrency/Java%20Concurrency%20in%20Practice.pdf"
        };

        // Create directories if they do not exist
        createDirectory("Syn");
        createDirectory("Asyn");

        // Asynchronous Processing
        System.out.println("Asynchronous Download started at " + getCurrentTime());
        long startTimeAsync = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create Runnables for each download task
        Runnable task1 = () -> downloadFileAsync(fileUrls[0], "Asyn");
        Runnable task2 = () -> downloadFileAsync(fileUrls[1], "Asyn");
        Runnable task3 = () -> downloadFileAsync(fileUrls[2], "Asyn");

        // Start each download task in a new thread using the Executor
        executor.execute(task1);
        executor.execute(task2);
        executor.execute(task3);

        // Shut down the Executor after all tasks are completed
        executor.shutdown();

        // Create a new thread to wait for the Asynchronous Processing to complete
        Thread asyncThread = new Thread(() -> {
            while (!executor.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long endTimeAsync = System.currentTimeMillis();
            System.out.println("Total Time (Asynchronous): " + (endTimeAsync - startTimeAsync) + "ms");
        });
        asyncThread.start();

        // Synchronous Processing
        System.out.println("Synchronous Download started at " + getCurrentTime());
        long startTimeSync = System.currentTimeMillis();
        for (String url : fileUrls) {
            downloadFileSync(url, "Syn");
        }
        long endTimeSync = System.currentTimeMillis();
        System.out.println("Total Time (Synchronous): " + (endTimeSync - startTimeSync) + "ms");
    }

    private static void createDirectory(String folder) {
        File dir = new File(folder);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory: " + folder);
            } else {
                System.out.println("Failed to create directory: " + folder);
            }
        }
    }

    private static void downloadFileAsync(String url, String folder) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(folder + "/" + getFileName(url))) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadFileSync(String url, String folder) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(folder + "/" + getFileName(url))) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
