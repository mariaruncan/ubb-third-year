import java.io.*;
import java.util.*;

public class Generator {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void generatePolynomials(int count, int maxExponent, int maxMembers) {
        for (int index = 0; index < count; index++) {
            Map<Integer, Integer> monoms = new HashMap<>();
            int membersCount = random.nextInt(maxMembers) + 1;
            while(membersCount < maxMembers * 2 / 3) {
                membersCount = random.nextInt(maxMembers) + 1;
            }

            for (int i = 0; i < membersCount; i++) { // generate each member
                int coefficient = random.nextInt(201) - 100; // [-100, 100]
                while (coefficient == 0) {
                    coefficient = random.nextInt(21) - 10;
                }

                int exponent = random.nextInt(maxExponent) + 1;
                while (monoms.containsKey(exponent)) {
                    exponent = random.nextInt(maxExponent) + 1;
                }

                monoms.put(exponent, coefficient);
            }

            writePolynomial(index, monoms);
        }
    }

    private static void writePolynomial(int index, Map<Integer, Integer> monoms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\University\\Sem5\\PPD\\Lab\\Tema5\\tema5\\src\\main\\resources\\input\\polynomial" + index + ".txt"))) {
            for (Map.Entry<Integer, Integer> entry : monoms.entrySet()) {
                writer.write(entry.getKey().toString() + " " + entry.getValue().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
