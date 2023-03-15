import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class ParallelUtil {
    /**
     * Filters an image on multiple threads
     *
     * @param p number of threads
     * @throws IOException
     * @throws InterruptedException
     */
    static void run(int p) throws IOException, InterruptedException {
        long startTime = System.nanoTime();

        int[][] mat = Utils.readMatrix(Utils.matrixFileName);
        int[][] filter = Utils.readMatrix(Utils.filterFileName);

        int N = mat.length, M = mat[0].length;

        int div = (N * M) / p;
        int mod = (N * M) % p;

        CyclicBarrier barrier = new CyclicBarrier(p);

        List<MyThread> threads = new ArrayList<>();
        int iStart = 0, jStart = 0;

        for (int i = 0; i < p; i++) {
            int count = div;
            if (i < mod) {
                count++;
            }
            MyThread th = new MyThread(iStart, jStart, count, mat, filter, barrier);
            threads.add(th);
            th.start();

            jStart += count;
            while (jStart >= M) {
                iStart++;
                jStart = jStart - M;
            }
        }

        for (int i = 0; i < p; i++) {
            threads.get(i).join();
        }

        long endTime = System.nanoTime();
        System.out.println((endTime - startTime)/1E6);

        Utils.validate(mat);
    }
}
