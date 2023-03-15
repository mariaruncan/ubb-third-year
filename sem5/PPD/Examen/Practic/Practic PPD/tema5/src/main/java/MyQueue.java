import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue {
    private final Queue<Node> queue = new LinkedList<>();

    synchronized public void add(Node node) {
        queue.add(node);
        notifyAll();
    }

    synchronized public Node remove() throws InterruptedException {
        while (queue.isEmpty())
            wait();

        Node object = queue.remove();
        notify();
        return object;
    }

    synchronized public int getSize() {
        return queue.size();
    }
}
