package homework3_multithreading;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

public class ThreadPool {
    // Потокобезопасная очередь задач
    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    // Массив рабочих потоков
    private final Thread[] workers;
    // Флаг "завершён ли пул" (Atomic для thread-safe проверки и установки)
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    // Счетчик для awaitTermination, чтобы дождаться завершения всех потоков
    private final CountDownLatch terminationLatch;

    public ThreadPool(int nThreads) {
        this.workers = new Thread[nThreads];
        this.terminationLatch = new CountDownLatch(nThreads);
        // Создаем и запускаем рабочие потоки
        for (int i = 0; i < nThreads; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    // Добавляет задачу в очередь на выполнение
    public void execute(Runnable task) {
        // Если пул завершен - бросаем исключение
        if (isShutdown.get()) {
            throw new IllegalStateException("ThreadPool is shutdown");
        }
        queue.offer(task); // Добавляем задачу в очередь
        // Пробуждаем все рабочие потоки, чтобы кто-то взял задачу
        for (Thread worker : workers) {
            LockSupport.unpark(worker);
        }
    }

    // Запрещает добавление новых задач и завершает потоки, когда все задачи выполнены
    public void shutdown() {
        isShutdown.set(true); // Устанавливаем флаг завершения
        // Пробуждаем все потоки, чтобы они вышли из ожидания, если очередь пуста
        for (Thread worker : workers) {
            LockSupport.unpark(worker);
        }
    }

    // Ожидание завершения всех рабочих потоков
    public void awaitTermination() throws InterruptedException {
        terminationLatch.await();
    }

    // Класс-рабочий поток
    private class Worker extends Thread {
        public void run() {
            try {
                while (true) {
                    Runnable task = queue.poll(); // Пытаемся взять задачу из очереди
                    if (task != null) {
                        // Если есть задача - выполняем
                        try {
                            task.run();
                        } catch (Throwable t) {
                            // Обработка ошибок задачи (например, логирование)
                        }
                        continue;
                    }
                    // Если shutdown и задач больше нет - завершаем поток
                    if (isShutdown.get()) {
                        if (queue.isEmpty()) {
                            break;
                        } else {
                            continue;
                        }
                    }
                    // Нет задач и не завершен пул — поток "засыпает" до появления задачи или shutdown
                    LockSupport.park();
                }
            } finally {
                // Сигнализируем, что поток завершился (для awaitTermination)
                terminationLatch.countDown();
            }
        }
    }
}