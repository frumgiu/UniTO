package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ReadTools {
    public static boolean readGrammarFile(String filename, HashMap<String, ArrayList<ArrayList<String>>> grammar, String startSymbol, ArrayList<String> terminals, ArrayList<String> nonTerminals) {
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

    public static boolean readInputFile(String filename, ArrayList<ArrayList<String>> result) {
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