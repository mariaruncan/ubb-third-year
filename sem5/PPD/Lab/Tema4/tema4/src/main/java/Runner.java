import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Runner {

    public static MyList runParallel(int polynomialsCount, int consumersCount) throws InterruptedException {
        MyList resultList = new MyList();
        MyQueue queue = new MyQueue();

        Producer producer = new Producer(polynomialsCount, consumersCount, queue);
        producer.start();

        List<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < consumersCount; i++) {
            Consumer consumer = new Consumer(queue, resultList);
            consumers.add(consumer);
            consumer.start();
        }

        producer.join();

        for (Consumer c : consumers) {
            c.join();
        }

        return resultList;
    }

    public static MyList runSequentially(int polynomialsCount) throws FileNotFoundException, InterruptedException {
        MyQueue queue = new MyQueue();
        MyList list = new MyList();

        for (int i = 0; i < polynomialsCount; i++) {
            Scanner scanner = new Scanner(new File("D:\\University\\Sem5\\PPD\\Lab\\Tema4\\tema4\\src\\main\\resources\\input\\polynomial" + i + ".txt"));
            while (scanner.hasNextLine()) {
                List<String> words = List.of(scanner.nextLine().split(" "));
                queue.add(new Node(Integer.parseInt(words.get(0)), Integer.parseInt(words.get(1))));
            }
        }
        queue.add(null);

        Node crt;
        while (true) {
            crt = queue.remove();
            if (crt == null) break;
            list.add(crt);
        }

        return list;
    }
}
