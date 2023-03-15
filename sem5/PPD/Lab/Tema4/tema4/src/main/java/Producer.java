import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Producer extends Thread {
    private final MyQueue queue;
    private final int polynomialsCount;
    private final int consumersCount;

    public Producer(int polynomialsCount, int consumersCount, MyQueue queue) {
        this.polynomialsCount = polynomialsCount;
        this.consumersCount = consumersCount;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void produce() throws FileNotFoundException {
        for (int i = 0; i < polynomialsCount; i++) {
            try (Scanner scanner = new Scanner(new File("D:\\University\\Sem5\\PPD\\Lab\\Tema4\\tema4\\src\\main\\resources\\input\\polynomial" + i + ".txt"))) {
                while (scanner.hasNextLine()) {
                    List<String> words = List.of(scanner.nextLine().split(" "));
                    addNode(Integer.parseInt(words.get(0)), Integer.parseInt(words.get(1)));
                }
            }
        }

        // for consumers to know when to stop
        for (int i = 0; i < consumersCount; i++) {
            queue.add(null);
        }
    }

    private void addNode(int exponent, int coefficient) {
        Node node = new Node(exponent, coefficient);
        queue.add(node);
    }
}
