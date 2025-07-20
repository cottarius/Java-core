package homework3_multithreading;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool pool = new ThreadPool(4);

        for (int i = 0; i < 20; i++) {
            int finalI = i;
            pool.execute(() -> {
                System.out.println("Task " + finalI + " executed by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("All done!");
    }
}

