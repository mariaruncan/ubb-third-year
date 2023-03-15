import java.io.*;
import java.util.*;

public class Generator {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void generateData(int filesNumber) {
        int idMax = 50;
        for (int index = 0; index < filesNumber; index++) {
            List<Integer> ids = new ArrayList<>();
            List<Integer> services = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                int service = 2;
                int id = random.nextInt(50);
                ids.add(id);
                while (service == 2) {
                    service = random.nextInt(4) + 1;
                }
                services.add(service);
            }
            writeToFile(ids, services, index);
        }
    }

    private static void writeToFile(List<Integer> ids, List<Integer> services, int index) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\rmari\\Desktop\\Tema5\\tema5\\src\\main\\resources\\input\\in." + index + "txt"))) {
            for (int i = 0; i < 50; i++) {
                writer.write(ids.get(i) + " " + services.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
