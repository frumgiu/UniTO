import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class uhuraSystem {
    private static String startSymbol = "S";
    private static final ArrayList<String> terminals = new ArrayList<>();
    private static final ArrayList<String> nonTerminals = new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println("Hi and welcome to the Uhura System! This software implements the CYK algorithm and it helps communication " +
                "officers to understand if a phrase belongs to a given Context Free Grammar.\n");
        System.out.print("""
                The system offers a demo in 2 language: English and Klingon. Which one would you like to try?
                1. English
                2. Klingon
                Insert number:\s""");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.close();
        System.out.println("Starting the demo...\n");

        if (choice == 1) {
            runAlgorithm("englishRules.txt", "inputEnglish.txt");
        } else {
            runAlgorithm("klingonTest.txt", "inputKlingon.txt");
        }

        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println("Thanks for using ours demo! Good bye and... long life and prosperity.");
    }

    private static void runAlgorithm(String grammarFile, String inputFile) {
        HashMap<Integer, HashMap<Integer, ArrayList<String>>> resultTable;
        HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<>();
        ArrayList<ArrayList<String>> inputLines = new ArrayList<>();
        if (readGrammarFile(grammarFile, grammar)) {
            if (readInputFile(inputFile, inputLines)) {
                for (ArrayList<String> inputLine : inputLines) {
                    resultTable = CYK.CykAlgorithm(inputLine, grammar);                                                                         // USO ALGORITMO
                    System.out.println("INPUT LINE: " + inputLine.toString().replace(",", ""));
                    System.out.println("The Context Free Grammar is G = (" + terminals + ", " + nonTerminals + ", P, " + startSymbol + " )\n");
                    printResult(inputLine.size(), inputLine, resultTable);
                }
            }
        }
    }

    private static void printResult(int n, ArrayList<String> line, HashMap<Integer, HashMap<Integer, ArrayList<String>>> table) {
        System.out.print("RESULT: ");
        if (table.get(0) != null && table.get(0).get(n - 1) != null && table.get(0).get(n - 1).size() != 0 && table.get(0).get(n - 1).contains(startSymbol))
            System.out.println("The line IS an element of the Context Free Grammar defined");
        else
            System.out.println("The line IS NOT an element of the Context Free Grammar defined1");

        System.out.println("Table created from the CYK algorithm");
        int numSpace = findLongString(n, table);
        int numSpaceLine = findLongestWord(line);
        for (int j = 0; j < n; j++) {
            System.out.printf("%" + numSpaceLine + "s %-" + numSpaceLine + "s", line.get(j), " | ");
            for (int i = j; i < n; i++) {
                System.out.printf("%" + numSpace + "s", table.get(j).get(i));
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    private static int findLongestWord(ArrayList<String> line) {
        if (line.stream().max(Comparator.comparing(String::length)).isPresent()) {
            String s = line.stream().max(Comparator.comparing(String::length)).get();
            return s.length();
        }
        return 10;
    }

    private static int findLongString(int n, HashMap<Integer, HashMap<Integer, ArrayList<String>>> table) {
        int total = 0;
        for (int j = 0; j < n; j++) {
            for (int i = j; i < n; i++) {
                if (table.containsKey(j) && table.get(j).containsKey(i) && table.get(j).get(i).stream().max(Comparator.comparing(String::length)).isPresent()) {
                    String s = table.get(j).get(i).stream().max(Comparator.comparing(String::length)).get();
                    if (s.length() > total)
                        total = s.length();
                }
            }
        }
        return total*4;
    }

    private static boolean readGrammarFile(String filename, HashMap<String, ArrayList<ArrayList<String>>> grammar) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            startSymbol = scanner.next();                                       // Il carattere iniziale è alla prima riga
            scanner.nextLine();
            terminals.addAll(Arrays.asList(splitLine(scanner.nextLine())));     // La seconda riga sono i simboli terminali
            nonTerminals.addAll(Arrays.asList(splitLine(scanner.nextLine())));  // La terza riga sono i simboli non terminali

            String lastLeftRule = null;
            while (scanner.hasNextLine()) {
                ArrayList<String> tempLineSplited = new ArrayList<>(Arrays.asList(splitLine(scanner.nextLine())));
                String currentLeftRule = tempLineSplited.get(0);
                tempLineSplited.remove(0);
                ArrayList<ArrayList<String>> temp;
                if (Objects.equals(lastLeftRule, currentLeftRule)) {
                    temp = grammar.get(lastLeftRule);
                } else {
                    lastLeftRule = currentLeftRule;
                    temp = new ArrayList<>();
                }
                temp.add(tempLineSplited);
                grammar.put(lastLeftRule, temp);
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("The file does not exist in our system.");
            return false;
        }
    }

    private static boolean readInputFile(String filename, ArrayList<ArrayList<String>> result) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                ArrayList<String> tempLineSplited = new ArrayList<>(Arrays.asList(splitLine(scanner.nextLine())));
                result.add(tempLineSplited);
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("The file does not exist in our system.");
            return false;
        }
    }

    private static String[] splitLine(String line) {
        return line.split("\\s");
    }
}