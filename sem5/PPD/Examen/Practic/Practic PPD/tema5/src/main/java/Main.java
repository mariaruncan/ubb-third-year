import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private static final int r = 5;
    private static final int w = 3;
    private static final int tc = 25;
    private static final int tr = 15;
    private static final int tt = 30;
    private static final int td = 30;

    public static void main(String[] args) {
        try {
//            generate();
            execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void execute() {
        long startTime = System.currentTimeMillis();

        MyQueue globalQueue = new MyQueue();
        MyQueue queue1 = new MyQueue();
        MyQueue queue2 = new MyQueue();
        MyQueue queue3 = new MyQueue();
        MyQueue queue4 = new MyQueue();

        CyclicBarrier barrierP = new CyclicBarrier(r);
        CyclicBarrier barrierC = new CyclicBarrier(w);

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();
        List<Thread> workers = new ArrayList<>();

        Map<Integer, MapEntry> map = new HashMap<>();

        for (int i = 0; i < r; i++) {
            producers.add(new Producer(i, w, globalQueue, barrierP));
        }

        consumers.add(new Consumer(0, globalQueue, queue1, queue3, queue4, barrierC));
        consumers.add(new Consumer(1, globalQueue, queue1, queue3, queue4, barrierC));
        consumers.add(new Consumer(2, globalQueue, queue1, queue3, queue4, barrierC));

        workers.add(new Worker(1, tc, queue1, queue2, map));
        workers.add(new Worker(2, tr, queue2, queue2, map));
        workers.add(new Worker(3, tt, queue3, queue2, map));
        workers.add(new Worker(4, td, queue4, queue2, map));

        producers.forEach(Thread::start);
        consumers.forEach(Thread::start);
        workers.forEach(Thread::start);

        try {
            for (Thread producer : producers) {
                producer.join();
            }
            for (Thread consumer : consumers) {
                consumer.join();
            }
            for (Thread worker : workers) {
                worker.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time parallel: " + (endTime - startTime));
    }

    private static void generate() {
        Generator.generateData(5);
    }
}