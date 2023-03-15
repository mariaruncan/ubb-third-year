import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    private final int iStart;
    private final int jStart;
    private final int count;
    private final int[][] mat;
    private final int[][] filter;
    private final CyclicBarrier barrier;
    private final int N, M, n;

    public MyThread(int iStart, int jStart, int count, int[][] mat, int[][] filter, CyclicBarrier barrier) {
        this.iStart = iStart;
        this.jStart = jStart;
        this.count = count;
        this.mat = mat;
        this.filter = filter;
        this.barrier = barrier;
        N = mat.length;
        M = mat[0].length;
        n = filter.length;
    }

    /**
     * Run function for thread; applies the filter on a region of pixels
     */
    @Override
    public void run() {

        //copy
        int[][] tmp = copyNeighbours();

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        //work
        int i = iStart, j = jStart;

        for (int zona = 0; zona < count; zona++) {
            int newValue = 0;
            for (int iTmp = 0; iTmp < n; iTmp++) {
                for (int jTmp = 0; jTmp < n; jTmp++) {
                    int x = tmp[iTmp][jTmp + n * zona];
                    int y  = filter[iTmp][jTmp];
                    newValue += x * y;
                }
            }

            mat[i][j] = newValue;

            j++;
            if (j >= M) {
                i++;
                j -= M;
            }
        }
    }

    /**
     * Copy the pixels to be calculated neighbours
     * @return a matrix with n lines and n*count columns, representing the neighbours
     */
    private int[][] copyNeighbours() {
        int tmpRows = n;
        int tmpColumns = n * count;
        int offset = tmpRows / 2;

        int iCopy = iStart;
        int jCopy = jStart;

        int[][] tmp = new int[tmpRows][tmpColumns];
        for (int k = 0; k < count; k++) { // pt fiecare elem, copiem zona lui
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int iMat = iCopy + i - offset;
                    if (iMat < 0) iMat = 0;
                    if (iMat >= N) iMat = N - 1;

                    int jMat = jCopy + j - offset;
                    if (jMat < 0) jMat = 0;
                    if (jMat >= M) jMat = M - 1;

                    tmp[i][j + n * k] = mat[iMat][jMat];
                }
            }

            jCopy++;
            if (jCopy >= M) {
                iCopy++;
                jCopy -= M;
            }
        }

        return tmp;
    }
}
