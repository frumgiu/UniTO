import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PrintTools {
    public static void printResultMatrix(int n, String startSymbol, ArrayList<String> line, HashMap<Integer, HashMap<Integer, ArrayList<String>>> table) {
        System.out.print("RESULT: ");
        if (table.get(0) != null && table.get(0).get(n - 1) != null && table.get(0).get(n - 1).size() != 0 && table.get(0).get(n - 1).contains(startSymbol))
            System.out.println("The line IS an element of the Context Free Grammar defined");
        else
            System.out.println("The line IS NOT an element of the Context Free Grammar defined");

        System.out.println("Table created from the CYK algorithm");
        int numSpace = 10; //findLongString(n, table);
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

    public static void printResultMatrixCasella(int n, String startSymbol, ArrayList<String> line, HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> table) {
        System.out.print("RESULT: ");
        if (table.get(0) != null && table.get(0).get(n - 1) != null && table.get(0).get(n - 1).size() != 0 && table.get(0).get(n - 1).stream().anyMatch(s -> s.getElement().equals(startSymbol)))
            System.out.println("The line IS an element of the Context Free Grammar defined");
        else
            System.out.println("The line IS NOT an element of the Context Free Grammar defined");

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

    private static int findLongString(int n, HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> table) {
        int total = 10;
       /* for (int j = 0; j < n; j++) {
            for (int i = j; i < n; i++) {
                if (table.containsKey(j)
                        && table.get(j).containsKey(i)
                        && table.get(j).get(i).stream().max(Comparator.comparing(String::length)).isPresent()) {
                    String s = table.get(j).get(i).stream().max(Comparator.comparing(String::length)).get();
                    if (s.length() > total)
                        total = s.length();
                }
            }
        }*/
        return total*4;
    }
}