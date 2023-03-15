import java.io.IOException;

public class SequentiallyUtil {
    /**
     * Filters an image sequentially
     * @throws IOException
     */
    static void run() throws IOException {
        long startTime = System.nanoTime();

        int[][] mat = Utils.readMatrix(Utils.matrixFileName);
        int[][] filter = Utils.readMatrix(Utils.filterFileName);

        int N = mat.length, M = mat[0].length, n = filter.length;

        // copy the neighbours
        int[][] tmp = new int[N][M];
        for(int i = 0; i < N; i++)
            System.arraycopy(mat[i], 0, tmp[i], 0, M);

        int offset = n / 2;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int newValue = 0;
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < n; l++) {
                        int iMat = i + k - offset;
                        if (iMat < 0) iMat = 0;
                        if (iMat >= N) iMat = N - 1;

                        int jMat = j + l - offset;
                        if (jMat < 0) jMat = 0;
                        if (jMat >= M) jMat = M - 1;

                        int x = tmp[iMat][jMat];
                        int y = filter[k][l];

                        newValue += x * y;
                    }
                }
                mat[i][j] = newValue;
            }
        }

        long endTime = System.nanoTime();
        System.out.println((endTime - startTime)/1E6);

        Utils.writeMatrix(mat, Utils.seqOut);
    }
}
