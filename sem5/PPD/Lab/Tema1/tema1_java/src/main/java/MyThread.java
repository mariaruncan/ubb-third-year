public class MyThread extends Thread {
    private final int iStart;
    private final int jStart;
    private final int step;
    private final int[][] mat;
    private final int[][] filter;
    private final int[][] result;

    public MyThread(int iStart, int jStart, int step, int[][] mat, int[][] filter, int[][] result) {
        this.iStart = iStart;
        this.jStart = jStart;
        this.step = step;
        this.mat = mat;
        this.filter = filter;
        this.result = result;
    }

    /**
     * Run function for thread; applies the filter on pixels
     */
    @Override
    public void run() {
        int N = mat.length, M = mat[0].length, n = filter.length;
        int i = iStart;
        int j = jStart;
        int offset = n / 2;

        while (i < N) {
            int newValue = 0;
            for (int k = -n / 2; k <= n / 2; k++) {
                for (int l = -n / 2; l <= n / 2; l++) {
                    int iMat = i - k;
                    if (iMat < 0) iMat = 0;
                    if (iMat >= N) iMat = N - 1;

                    int jMat = j - l;
                    if (jMat < 0) jMat = 0;
                    if (jMat >= M) jMat = M - 1;

                    newValue += mat[iMat][jMat] * filter[k + offset][l + offset];
                }
            }
            result[i][j] = newValue;

            // mergem mai departe
            j = j + step;
            while (j >= M) {
                j = j - M;
                i++;
            }
        }
    }
}
