import utils.Casella;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CYK {
    static HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> CykAlgorithm (ArrayList<String> line, HashMap<String, ArrayList<ArrayList<String>>> grammar) {
        HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> table = new HashMap<>();                 // Table for result
        int numWords = line.size();
        for (int j = 1; j <= numWords; j++) {                                                            // Controllo tutte le regole per riempire la diagonale
            for (Map.Entry<String, ArrayList<ArrayList<String>>> entry: grammar.entrySet()) {
                Casella temp = new Casella(entry.getKey());
                ArrayList<ArrayList<String>> listRulesForEntry = entry.getValue();
                for (ArrayList<String> rule : listRulesForEntry) {                                      // Sicuramente la parola singola sarà un simbolo terminale
                    if (rule.size() == 1 && rule.get(0).equals(line.get(j-1))) {
                        table.computeIfAbsent(j-1, k -> new HashMap<>());
                        if (!table.get(j-1).computeIfAbsent(j, k -> new ArrayList<>()).contains(temp))
                            table.get(j-1).get(j).add(temp);
                    }
                }
            }
            for (int i = j-2; i >= 0; i--) {
                for (int k = i+1; k <= j-1; k++) {
                    for (Map.Entry<String, ArrayList<ArrayList<String>>> entry: grammar.entrySet()) {
                        String keyRule = entry.getKey();
                        ArrayList<ArrayList<String>> listRulesForEntry = entry.getValue();
                        for (ArrayList<String> rule : listRulesForEntry) {
                            if (rule.size() == 2
                                    && table.get(i) != null && table.get(i).get(k) != null && haveParents(table.get(i).get(k), rule.get(0))
                                    && table.get(k) != null && table.get(k).get(j) != null && haveParents(table.get(k).get(j), rule.get(1))) {
                                table.computeIfAbsent(i, t -> new HashMap<>()).computeIfAbsent(j, t -> new ArrayList<>());
                                table.get(i).computeIfAbsent(j, t -> new ArrayList<>());
                                Casella tempAdd = new Casella(keyRule, getParent(table.get(i).get(k), rule.get(0)).get(), getParent(table.get(k).get(j), rule.get(1)).get());
                                table.get(i).get(j).add(tempAdd);
                            }
                        }
                    }
                }
            }
        }
        return table;
    }

    private static boolean haveParents(ArrayList<Casella> list, String rule) {
        return list.stream().anyMatch(str -> str.getElement().equals(rule));
    }

    private static Optional<Casella> getParent(ArrayList<Casella> list, String rule) {
        return list.stream().filter(s -> s.getElement().equals(rule)).findFirst();
    }
}