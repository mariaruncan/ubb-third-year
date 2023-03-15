import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Program {
    private static final ArrayList<String> separators = new ArrayList<>();
    private static final ArrayList<String> firstTable = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> FIP = new ArrayList<>();
    private static final SortedLinkedList TS_ID = new SortedLinkedList();
    private static final SortedLinkedList TS_CONST = new SortedLinkedList();

    /**
     * Entry point of the program
     */
    public static void main(String[] args) {
        initSeparators();
        firstTable.add("ID");
        firstTable.add("CONST");

        System.out.println("Enter file name: ");
        Scanner in = new Scanner(System.in);
        String filename = "src/" + in.nextLine();

        try {
            processFile(filename);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            writeFirstTable();
            writeFIP();
            writeTS();
        }
    }

    /**
     * Init separators list
     */
    private static void initSeparators() {
        separators.add(";");
        separators.add("(");
        separators.add(")");
        separators.add("{");
        separators.add("}");
        separators.add(",");
    }

    /**
     * Reads from file line by line and processes word by word for lexical analysis
     * @param filename name of the file to read the program from
     * @throws RuntimeException if file is not found or if an atom is invalid
     */
    private static void processFile(String filename) throws RuntimeException {
        int lineNumber = 0;
        try (Scanner in = new Scanner(new File(filename))) {
            while (in.hasNext()) {
                lineNumber++;
                String line = in.nextLine();
                String[] words = line.split(" ");

                int finalLineNumber = lineNumber;
                Arrays.stream(words).forEach(word -> processWord(word, finalLineNumber));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Splits a word in prefix, word and suffix and processes it
     * @param word the word to be split
     * @param lineNumber line number of the word
     * @throws RuntimeException if a part of the word is invalid
     */
    private static void processWord(String word, int lineNumber) throws RuntimeException {
        String prefix = null, suffix = null;

        /*
            verify if atom starts or ends with a separator and splits it in prefix, word and suffix
         */
        for (String separator : separators) {
            if (word.startsWith(separator) && prefix == null) {
                word = word.replace(separator, "");
                prefix = separator;
            }

            if (word.endsWith(separator) && suffix == null) {
                word = word.replace(separator, "");
                suffix = separator;
            }
        }

        if (prefix != null) {
            processAtom(prefix, lineNumber);
        }

        word = word.trim();
        if (!word.isEmpty()) {
            processAtom(word, lineNumber);
        }

        if (suffix != null) {
            processAtom(suffix, lineNumber);
        }
    }

    /**
     * Verifies if a string is keyword, separator, operator, id or constant
     * @param atom the string to be verified
     * @param lineNumber the line that contains the atom
     * @throws RuntimeException if the atom in not in a category
     */
    private static void processAtom(String atom, int lineNumber) throws RuntimeException {
        int indexInFirstTable;
        int indexInTS = -1;
        if (firstTable.contains(atom)) {
            indexInFirstTable = firstTable.indexOf(atom);
        } else if (MyRegex.isKeyWord(atom) || MyRegex.isOperator(atom) || MyRegex.isSeparator(atom)) {
            firstTable.add(atom);
            indexInFirstTable = firstTable.indexOf(atom);
        } else if (MyRegex.isConstant(atom)) {
            TS_CONST.sortedInsert(atom);
            indexInFirstTable = firstTable.indexOf("CONST");
            indexInTS = TS_CONST.findPosition(atom);
        } else if (MyRegex.isId(atom)) {
            TS_ID.sortedInsert(atom);
            indexInFirstTable = firstTable.indexOf("ID");
            indexInTS = TS_ID.findPosition(atom);
        } else {
            throw new RuntimeException("Something went wrong on line: " + lineNumber);
        }

        ArrayList<Integer> lineInFip = new ArrayList<>();
        lineInFip.add(indexInFirstTable);
        if (indexInTS != -1) lineInFip.add(indexInTS);
        FIP.add(lineInFip);
    }

    /**
     * Writes the first table to console
     */
    private static void writeFirstTable() {
        System.out.println("\n");
        System.out.println("------- First Table -------");
        System.out.println("Cod \tAtom lexical");
        for (int i = 0; i < firstTable.size(); i++) {
            System.out.println(i + "\t\t" + firstTable.get(i));
        }
        System.out.println("\n\n");
    }

    /**
     * Writes FIP to console
     */
    private static void writeFIP() {
        System.out.println("------- FIP -------");
        System.out.println("Cod atom \tPoz TS");
        for (ArrayList<Integer> line : FIP) {
            if (line.get(0) == 0 || line.get(0) == 1) {
                System.out.println(line.get(0) + "\t\t\t" + line.get(1));
            } else {
                System.out.println(line.get(0));
            }
        }
        System.out.println("\n\n");
    }

    /**
     * Writes TS_ID and TS_CONST to console
     */
    private static void writeTS() {
        System.out.println("------- TS_ID -------");
        TS_ID.printList();
        System.out.println("------- TS_CONST -------");
        TS_CONST.printList();
    }
}
