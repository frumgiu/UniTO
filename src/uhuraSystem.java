import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

        //runAlgorithm2();

        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println("Thanks for using ours demo! Good bye and... long life and prosperity.");
    }

    private static void runAlgorithm(String grammarFile, String inputFile) {
        HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> resultTable;
        HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<>();
        ArrayList<ArrayList<String>> inputLines = new ArrayList<>();
        if (readGrammarFile(grammarFile, grammar)) {
            if (readInputFile(inputFile, inputLines)) {
                for (ArrayList<String> inputLine : inputLines) {
                    resultTable = CYKTest.CykAlgorithm(inputLine, grammar);                                                                         // USO ALGORITMO
                    System.out.println("INPUT LINE: " + inputLine.toString().replace(",", ""));
                    System.out.println("The Context Free Grammar is G = (" + terminals + ", " + nonTerminals + ", P, " + startSymbol + " )\n");
                    PrintTools.printResultMatrixCasella(inputLine.size(), startSymbol, inputLine, resultTable);
                    //printTree(resultTable);
                }
            }
        }
    }

    private static void runAlgorithm2() {
        HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> resultTable = new HashMap<>();
        Casella c1 = new Casella("Casa");
        Casella c2 = new Casella("Cane");
        Casella c3 = new Casella("Giardino");
        Casella c4 = new Casella("Tisana");
        Casella c5 = new Casella("Gatto", c1, c2);
        Casella c6 = new Casella("Missipissi", c3, c4);
        Casella c7 = new Casella("Oronzo", c5, c6);
        Casella c8 = new Casella("Giulia");

        resultTable.put(0, new HashMap<>());
        resultTable.put(1, new HashMap<>());
        resultTable.put(2, new HashMap<>());
        resultTable.put(3, new HashMap<>());
        resultTable.get(0).put(0, new ArrayList<>());
        resultTable.get(1).put(0, new ArrayList<>());
        resultTable.get(2).put(0, new ArrayList<>());
        resultTable.get(3).put(0, new ArrayList<>());
        resultTable.get(0).put(1, new ArrayList<>());
        resultTable.get(1).put(1, new ArrayList<>());
        resultTable.get(0).put(2, new ArrayList<>());

        resultTable.get(0).get(0).add(c1);
        resultTable.get(1).get(0).add(c2);
        resultTable.get(2).get(0).add(c3);
        resultTable.get(3).get(0).add(c4);
        resultTable.get(0).get(1).add(c5);
        resultTable.get(1).get(1).add(c6);
        resultTable.get(0).get(2).add(c7);
        resultTable.get(0).get(2).add(c8);

        printTree(resultTable);
    }

    public static void printTree(HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> result) {
        ArrayList<Casella> leaves = result.get(0).get(2); // prendi le foglie dell'albero
        for (Casella leaf : leaves) {
            if (leaf.getElement().equals(startSymbol)) {
                printNode(leaf, 0); // stampa la foglia
            }
        }
    }

    private static void printNode(Casella node, int depth) {
        if (node != null) {
            for (int i = 0; i < depth; i++) {
                System.out.print("\t"); // stampa una tabulazione per ogni livello di profondità
            }
            System.out.println(node.getElement()); // stampa l'elemento del nodo corrente
            ArrayList<Casella> parents = new ArrayList<>(); // prendi i genitori del nodo corrente
            parents.add(node.getParentOne());
            parents.add(node.getParentTwo());
            for (Casella parent : parents) {
                printNode(parent, depth + 1); // stampa i genitori del nodo corrente
            }
        }
    }

    public static boolean readGrammarFile(String filename, HashMap<String, ArrayList<ArrayList<String>>> grammar) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            startSymbol = scanner.nextLine();
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