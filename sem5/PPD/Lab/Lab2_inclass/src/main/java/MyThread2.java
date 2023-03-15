import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MyThread2 extends Thread {
    private final int id, p;
    private final List<Double> a, b, c;

    public MyThread2(int id, int p, List<Double> a, List<Double> b, List<Double> c) {
        this.id = id;
        this.p = p;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void run() {
        for (int i = id; i < a.size(); i = i + p) {
            c.set(i, sqrt(pow(a.get(i), 2) + pow(b.get(i), 2)));
        }
    }
}
