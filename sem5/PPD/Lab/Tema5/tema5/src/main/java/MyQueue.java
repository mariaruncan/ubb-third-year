import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue {
    private final int max;
    private final Queue<Node> queue = new LinkedList<>();

    public MyQueue(int max) {
        this.max = max;
    }

    synchronized public void add(Node node) throws InterruptedException {
        while(queue.size() == max) {
            wait();
        }

        queue.add(node);
        notifyAll();
    }

    synchronized public Node remove() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        Node object = queue.remove();
        notify();
        return object;
    }
}
