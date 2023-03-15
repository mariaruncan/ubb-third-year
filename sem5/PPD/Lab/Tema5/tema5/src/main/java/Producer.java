import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Producer extends Thread {
    private final MyQueue queue;
    private final int start;
    private final int end;
    private final int consumersCount;
    private final CyclicBarrier barrier;

    public Producer(int start, int end, int consumersCount, MyQueue queue, CyclicBarrier barrier) {
        this.start = start;
        this.end = end;
        this.consumersCount = consumersCount;
        this.queue = queue;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void produce() throws FileNotFoundException, BrokenBarrierException, InterruptedException {
        for (int i = start; i < end; i++) {
            try (Scanner scanner = new Scanner(new File("D:\\University\\Sem5\\PPD\\Lab\\Tema5\\tema5\\src\\main\\resources\\input\\polynomial" + i + ".txt"))) {
                while (scanner.hasNextLine()) {
                    List<String> words = List.of(scanner.nextLine().split(" "));
                    addNode(Integer.parseInt(words.get(0)), Integer.parseInt(words.get(1)));
                }
            }
        }

        barrier.await();
        if(start == 0) {
            for(int i = 0; i < consumersCount; i++) {
                queue.add(null);
            }
        }
    }

    private void addNode(int exponent, int coefficient) throws InterruptedException {
        Node node = new Node(exponent, coefficient);
        queue.add(node);
    }
}
