import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Consumer extends Thread {
    private final int id;
    private final MyQueue globalQueue;
    private final MyQueue queue1;
    private final MyQueue queue3;
    private final MyQueue queue4;
    private final CyclicBarrier barrier;

    public Consumer(int id, MyQueue globalQueue, MyQueue queue1, MyQueue queue3, MyQueue queue4, CyclicBarrier barrier) {
        this.id = id;
        this.globalQueue = globalQueue;
        this.queue1 = queue1;
        this.queue3 = queue3;
        this.queue4 = queue4;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            consume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consume() throws InterruptedException, BrokenBarrierException {
        Node crt;
        while (true) {
            crt = globalQueue.remove();
            if (crt == null) break;
            if (crt.getServiceId() == 1) {
                queue1.add(crt);
            } else if (crt.getServiceId() == 3) {
                queue3.add(crt);
            } else if (crt.getServiceId() == 4) {
                queue4.add(crt);
            }
        }

        barrier.await();
        if(id == 0) {
            queue1.add(null);
            queue3.add(null);
            queue4.add(null);
        }
    }
}
