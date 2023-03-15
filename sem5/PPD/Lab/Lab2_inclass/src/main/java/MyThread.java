import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MyThread extends Thread {
    private final int start;
    private final int end;
    private final List<Double> a, b, c;

    public MyThread(int start, int end, List<Double> a, List<Double> b, List<Double> c) {
        this.start = start;
        this.end = end;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            c.set(i, sqrt(pow(a.get(i), 2) + pow(b.get(i), 2)));
        }
    }
}
