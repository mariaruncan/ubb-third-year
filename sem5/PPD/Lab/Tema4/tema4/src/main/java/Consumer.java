public class Consumer extends Thread {
    private final MyQueue queue;
    private final MyList resultList;

    public Consumer(MyQueue queue, MyList resultList) {
        this.queue = queue;
        this.resultList = resultList;
    }

    @Override
    public void run() {
        try {
            consume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consume() throws InterruptedException {
        Node crt;
        while (true) {
            crt = queue.remove();
            if (crt == null) break;
            resultList.add(crt);
        }
    }
}
