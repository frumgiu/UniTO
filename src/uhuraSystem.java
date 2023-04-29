import utils.Casella;
import static utils.PrintTools.*;
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
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println("Thanks for using ours demo! Good bye and... long life and prosperity.");
    }

    /**
     * In questo metodo vengono letti i file riguardanti la grammatica e le frasi input, salvati i dati nelle opportune strutture
     * e poi eseguito l'algoritmo CYK.
     * Il risultato viene poi stampato a schermo come matrice. Inoltre vengono stampati gli alberi di parsificazione di ogni frasi
     * @param grammarFile File che contiene la lista delle regole in CNF della grammatica
     * @param inputFile File che contiene le frasi da analizzare tramite algoritmo CYK
     */
    private static void runAlgorithm(String grammarFile, String inputFile) {
        HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> resultTable;
        HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<>();
        ArrayList<ArrayList<String>> inputLines = new ArrayList<>();
        if (readGrammarFile(grammarFile, grammar)) {
            if (readInputFile(inputFile, inputLines)) {
                for (ArrayList<String> inputLine : inputLines) {
                    resultTable = CYK.CykAlgorithm(inputLine, grammar);                                                                         // USO ALGORITMO
                    System.out.println("INPUT LINE: " + inputLine.toString().replace(",", ""));
                    System.out.println("The Context Free Grammar is G = (" + terminals + ", " + nonTerminals + ", P, " + startSymbol + " )\n");
                    printResultMatrix(inputLine.size(), startSymbol, inputLine, resultTable);
                    printTree(inputLine.size(), startSymbol, resultTable);
                }
            }
        }
    }

    /**
     * Dato un file lo legge e inserisce opportunamente il contenuto nella struttura dati.
     * Per ogni regola letta il primo simbolo sarà la chiave della hashmap, mentre i successivi simboli saranno scomposti
     * e inseriti in una lista.
     * Le prime tre righe del file contengono informazioni sulla grammatica.
     * @param filename Nome del file che contiene la grammatica
     * @param grammar Struttura dove salvare la grammatica
     * @return
     */
    private static boolean readGrammarFile(String filename, HashMap<String, ArrayList<ArrayList<String>>> grammar) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            startSymbol = scanner.nextLine();                                   // La prima riga è il simbolo iniziale della grammatica
            terminals.addAll(Arrays.asList(splitLine(scanner.nextLine())));     // La seconda riga sono i simboli terminali
            nonTerminals.addAll(Arrays.asList(splitLine(scanner.nextLine())));  // La terza riga sono i simboli non terminali

            String lastLeftRule = null;
            while (scanner.hasNextLine()) {
                ArrayList<String> tempLineSplited = new ArrayList<>(Arrays.asList(splitLine(scanner.nextLine())));  // Leggo la riga spezzandola per lo spazio vuoto
                String currentLeftRule = tempLineSplited.get(0);                                                    // Levo il primo simbolo, è la chiave della hashmap
                tempLineSplited.remove(0);
                ArrayList<ArrayList<String>> temp;
                if (Objects.equals(lastLeftRule, currentLeftRule)) {                                                // Se il primo simbolo della nuova riga è = all'ulitmo primo simbolo
                    temp = grammar.get(lastLeftRule);                                                               // Devo aggiungere la nuova regola in una chiave già esistente
                } else {
                    lastLeftRule = currentLeftRule;                                                                 // Altrimenti devo creare una nuova lista di regole per una nuova chiave
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