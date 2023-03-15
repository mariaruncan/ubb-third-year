import java.util.Map;
import java.util.Random;

public class Worker extends Thread {
    private final int serviceId;
    private final int time;
    private final MyQueue queue;
    private final MyQueue eventuallyQueue2;
    private final Map<Integer, MapEntry> map;

    public Worker(int serviceId, int time, MyQueue queue, MyQueue eventuallyQueue2, Map<Integer, MapEntry> map) {
        this.serviceId = serviceId;
        this.time = time;
        this.queue = queue;
        this.eventuallyQueue2 = eventuallyQueue2;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            work();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void work() throws InterruptedException {
        boolean shouldVerifyQueueSize = false;
        while (true) {
            if (shouldVerifyQueueSize) {
                if (queue.getSize() == 0) break;
            }
            Node crt = queue.remove();
            if (crt == null) {
                shouldVerifyQueueSize = true;
            } else {
                synchronized (map) {
                    MapEntry x = map.get(crt.getId());
//                    System.out.println("service: " + serviceId + " time: " + time + " petId" + crt.getId() + " entry: " + x);
                    if (x == null) {
                        map.put(crt.getId(), new MapEntry(0, 0));
                    } else if (x.getCod() == 0) {
                        map.put(crt.getId(), new MapEntry(serviceId, x.istoric));
                        sleep(time);
                        map.put(crt.getId(), new MapEntry(0, x.istoric * 10 + serviceId));
                        if (serviceId == 1) {
                            Random rand = new Random();
                            int r = rand.nextInt(10) + 1;
                            if (r > 5) {
                                eventuallyQueue2.add(new Node(crt.getId(), 2));
                            }
                        }
                    } else {
                        queue.add(crt);
                    }
                }
            }
            if (serviceId == 1) {
                eventuallyQueue2.add(null);
            }
        }
    }
}

class MapEntry {
    int cod;
    int istoric;

    public MapEntry(int cod, int istoric) {
        this.cod = cod;
        this.istoric = istoric;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getIstoric() {
        return istoric;
    }

    public void setIstoric(int istoric) {
        this.istoric = istoric;
    }

    public void addToHistory(int s) {
        this.istoric = this.istoric * 10 + s;
    }

    @Override
    public String toString() {
        return "{" +
                "cod=" + cod +
                ", istoric=" + istoric +
                '}';
    }
}
