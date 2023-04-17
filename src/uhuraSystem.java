import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import static utils.ReadTools.*;
import static utils.PrintTools.*;

public class uhuraSystem {
    private static String startSymbol;
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
        if (readGrammarFile(grammarFile, grammar, startSymbol, terminals, nonTerminals)) {
            if (readInputFile(inputFile, inputLines)) {
                for (ArrayList<String> inputLine : inputLines) {
                    resultTable = CYK.CykAlgorithm(inputLine, grammar);                                                                         // USO ALGORITMO
                    System.out.println("INPUT LINE: " + inputLine.toString().replace(",", ""));
                    System.out.println("The Context Free Grammar is G = (" + terminals + ", " + nonTerminals + ", P, " + startSymbol + " )\n");
                    printResult(inputLine.size(), startSymbol, inputLine, resultTable);
                }
            }
        }
    }
}