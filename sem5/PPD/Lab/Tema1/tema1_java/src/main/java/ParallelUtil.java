import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParallelUtil {
    /**
     * Filters an image on multiple threads
     * @param p number of threads
     * @throws IOException
     * @throws InterruptedException
     */
    static void run(int p) throws IOException, InterruptedException {
        int[][] mat = Utils.readMatrix(Utils.matrixFileName);
        int[][] filter = Utils.readMatrix(Utils.filterFileName);

        long startTime = System.nanoTime();

        int N = mat.length, M = mat[0].length;

        int[][] result = new int[N][M];
        List<MyThread> threads = new ArrayList<>();
        int iStart = 0, jStart = 0;

        for (int i = 0; i < p; i++) {
            MyThread th = new MyThread(iStart, jStart, p, mat, filter, result);
            threads.add(th);
            th.start();

            jStart++;
            if (jStart >= M) {
                iStart++;
                jStart = jStart - M;
            }
        }

        for (int i = 0; i < p; i++) {
            threads.get(i).join();
        }
    }
}
