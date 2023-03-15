import java.io.FileNotFoundException;

public class Main {
    private static final int polynomialsCount = 10;
    private static final int consumersCount = 2;
    private static final int maxExponent = 10000;
    private static final int maxMembers = 100;

    public static void main(String[] args) {
        try {
//            generate();
//            runSequentially();
            runParallel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void runSequentially() throws FileNotFoundException, InterruptedException {
        long startTime = System.currentTimeMillis();
        MyList resultList = Runner.runSequentially(polynomialsCount);
        long endTime = System.currentTimeMillis();
        System.out.println("Time sequentially: " + (endTime - startTime));
        resultList.printList("D:\\University\\Sem5\\PPD\\Lab\\Tema4\\tema4\\src\\main\\resources\\output\\out-sequentially.txt");
    }

    private static void runParallel() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        MyList resultList = Runner.runParallel(polynomialsCount, consumersCount);
        long endTime = System.currentTimeMillis();
        System.out.println("Time parallel: " + (endTime - startTime));
        resultList.printList("D:\\University\\Sem5\\PPD\\Lab\\Tema4\\tema4\\src\\main\\resources\\output\\out-parallel.txt");
    }

    private static void generate() {
        Generator.generatePolynomials(polynomialsCount, maxExponent, maxMembers);
    }
}