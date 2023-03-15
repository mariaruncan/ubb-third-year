import java.io.IOException;

public class SequentiallyUtil {
    /**
     * Filters an image sequentially
     * @throws IOException
     */
    static void run() throws IOException {
        int[][] mat = Utils.readMatrix(Utils.matrixFileName);
        int[][] filter = Utils.readMatrix(Utils.filterFileName);

        int N = mat.length, M = mat[0].length, n = filter.length;

        int[][] result = new int[N][M];
        int offset = n / 2;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
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
            }
        }
    }
}
