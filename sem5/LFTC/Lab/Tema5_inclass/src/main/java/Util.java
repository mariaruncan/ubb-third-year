import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Util {
    private static String filepath = "D:\\University\\Sem5\\LFTC\\Lab\\Tema5_inclass\\src\\main\\resources\\in.txt";

    public static Gramatica readFromFile() {
        Gramatica gram = new Gramatica();
        try(Scanner scanner = new Scanner(new File(filepath))) {
            while(scanner.hasNextLine()) {
                List<String> entry = List.of(scanner.nextLine().split(" "));
                gram.addRule(entry.get(0), entry.subList(1, entry.size()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gram;
    }
}
