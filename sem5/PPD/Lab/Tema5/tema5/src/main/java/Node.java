import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private final int exponent;
    private int coefficient;
    private Node next;
    private final Lock lock;

    public Node(int exponent, int coefficient) {
        this.exponent = exponent;
        this.coefficient = coefficient;
        this.next = null;
        this.lock = new ReentrantLock();
    }

    public int getExponent() {
        return exponent;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }
}
