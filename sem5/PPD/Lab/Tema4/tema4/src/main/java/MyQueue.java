import java.util.Queue;
import java.util.LinkedList;

public class MyQueue {
    private final Queue<Node> queue = new LinkedList<>();

    synchronized public void add(Node node) {
        queue.add(node);
        notifyAll();
    }

    synchronized public Node remove() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.remove();
    }
}
