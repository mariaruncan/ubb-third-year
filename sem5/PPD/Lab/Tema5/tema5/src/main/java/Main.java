import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private static final int p = 3;
    private static final int p1 = 2;
    private static final int max = 30;
    private static final int polynomialsCount = 5;
    private static final int maxExponent = 10000;
    private static final int maxMembers = 100;

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

        MyList resultList = new MyList();
        MyQueue queue = new MyQueue(max);
        // for threads to know when all files are read
        CyclicBarrier barrier = new CyclicBarrier(p1);

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();

        int mod = polynomialsCount % p1;
        int div = polynomialsCount / p1;
        int start = 0, end = div;

        for (int i = 0; i < p1; i++) {
            if (mod > 0) {
                mod--;
                end++;
            }

            producers.add(new Producer(start, end, p - p1, queue, barrier));

            start = end;
            end += div;
        }

        for (int i = 0; i < p - p1; i++) {
            consumers.add(new Consumer(queue, resultList));
        }

        producers.forEach(Thread::start);
        consumers.forEach(Thread::start);

        try {
            for (Thread producer : producers) {
                producer.join();
            }
            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time parallel: " + (endTime - startTime));
        resultList.printList("D:\\University\\Sem5\\PPD\\Lab\\Tema5\\tema5\\src\\main\\resources\\output\\out.txt");
    }

    private static void generate() {
        Generator.generatePolynomials(polynomialsCount, maxExponent, maxMembers);
    }
}