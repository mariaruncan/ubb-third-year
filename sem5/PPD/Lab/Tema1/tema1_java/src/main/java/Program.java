import java.io.IOException;

public class Program {
    /**
     * Entry point for our program
     * */
    public static void main(String[] args) throws IOException, InterruptedException {
        generate();

//        long startTime = System.nanoTime();
//        run(args);
//        long endTime = System.nanoTime();
//        System.out.println((endTime - startTime)/1E6);
    }

    private static void run(String[] args) throws IOException, InterruptedException {
        int p = Integer.parseInt(args[0]);
        if (p == 1) {
            SequentiallyUtil.run();
        } else {
            ParallelUtil.run(p);
        }
    }

    private static void generate() throws IOException {
        MatrixGenerator.generate(10000, 10, "image");
        MatrixGenerator.generate(5, 5, "filter");
    }
}
