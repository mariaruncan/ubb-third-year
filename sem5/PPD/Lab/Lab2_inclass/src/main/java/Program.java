import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * adunarea a doi vectori
 * a = [1, 2, ..., n]
 * b = [1, 2, ..., n]
 * -----------------
 * c = [2, 4, ... , 2n]
 * c[i] = a[i] + b[i] de n ori
 */
public class Program {
    private static final int n = 10000000, p = 8;
//    private static final double[] a = new double[n], b = new double[n], c = new double[n];


    private static void printVector(double[] v) {
        for (double value : v) {
            System.out.print(value + " ");
        }
    }

    private static void secvential() {
        List<Double> a = new ArrayList<>(n);
        List<Double> b = new ArrayList<>(n);
        List<Double> c = Arrays.asList(new Double[n]);

        init2(a, b);
        long startTime = System.nanoTime();

        for (int i = 0; i < n; i++) {
            c.set(i, sqrt(pow(a.get(i), 2) + pow(b.get(i), 2)));
        }
        long endTime = System.nanoTime();

//        printVector(c);
        System.out.println("Time secvential: " + (endTime - startTime));
    }

//    private static void init() {
//        Random random = new Random();
//        for (int i = 0; i < n; i++) {
//            a[i] = random.nextInt(900000);
//            b[i] = random.nextInt(900000);
//        }
//    }

    private static void init2(List<Double> a, List<Double> b) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            a.add((double) random.nextInt(900000));
            b.add((double) random.nextInt(900000));
        }
    }


    private static void paralel1() throws InterruptedException {
        List<Double> a = new ArrayList<>(n);
        List<Double> b = new ArrayList<>(n);
        List<Double> c = Arrays.asList(new Double[n]);

        init2(a, b);
        long startTime = System.nanoTime();

        int intreg = n / p;
        int rest = n % p;
        int start = 0, end = intreg;
        List<MyThread> myThreads = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            MyThread thread = new MyThread(start, end, a, b, c);
            myThreads.add(thread);
            thread.start();

//            System.out.println(i + " " + start + " " + end);
            start = end;
            end += intreg;
        }

        for (int i = 0; i < p; i++) {
            myThreads.get(i).join();
        }
        long endTime = System.nanoTime();

//        printVector(c);
        System.out.println("Time paralel1: " + (endTime - startTime));
    }

    private static void paralel2() throws InterruptedException {
        List<Double> a = new ArrayList<>(n);
        List<Double> b = new ArrayList<>(n);
        List<Double> c = Arrays.asList(new Double[n]);

        init2(a, b);
        long startTime = System.nanoTime();

        List<MyThread2> myThreads = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            MyThread2 thread = new MyThread2(i, p, a, b, c);
            myThreads.add(thread);
            thread.start();
        }

        for (int i = 0; i < p; i++) {
            myThreads.get(i).join();
        }
        long endTime = System.nanoTime();

//        printVector(c);
        System.out.println("Time paralel2: " + (endTime - startTime));
    }

    public static void main(String[] args) throws InterruptedException {
        secvential();
        paralel1();
        paralel2();
    }
}
