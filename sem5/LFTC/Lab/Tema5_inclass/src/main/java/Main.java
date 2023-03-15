import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        Gramatica gramatica = Util.readFromFile();
        String cmd;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter a non-terminal or x to exit.");
            cmd = reader.readLine();
            if (cmd.equals("x") || cmd.equals("X")) {
                break;
            }
            gramatica.printRulesOf(cmd);
        }
    }
}