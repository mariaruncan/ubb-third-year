import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Producer extends Thread {
    private final MyQueue globalQueue;
    private final int fileId;
    private final int w;
    private final CyclicBarrier barrier;

    public Producer(int fileId, int w, MyQueue globalQueue, CyclicBarrier barrier) {
        this.fileId = fileId;
        this.w = w;
        this.globalQueue = globalQueue;
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
        try (Scanner scanner = new Scanner(new File("C:\\Users\\rmari\\Desktop\\Tema5\\tema5\\src\\main\\resources\\input\\in." + fileId + "txt"))) {
            while (scanner.hasNextLine()) {
                List<String> words = List.of(scanner.nextLine().split(" "));
                addNode(Integer.parseInt(words.get(0)), Integer.parseInt(words.get(1)));
            }
        }

        barrier.await();
        if (fileId == 0) {
            for (int i = 0; i < w; i++) {
                globalQueue.add(null);
            }
        }
    }

    private void addNode(int id, int serviceId) {
        Node node = new Node(id, serviceId);
        globalQueue.add(node);
    }
}
