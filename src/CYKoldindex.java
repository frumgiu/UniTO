import utils.Casella;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CYKoldindex {
    static HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> CykAlgorithm (ArrayList<String> line, HashMap<String, ArrayList<ArrayList<String>>> grammar) {
        HashMap<Integer, HashMap<Integer, ArrayList<Casella>>> table = new HashMap<>();                 // Table for result
        int numWords = line.size();
        for (int j = 0; j < numWords; j++) {                                                            // Controllo tutte le regole per riempire la colonna base
            for (Map.Entry<String, ArrayList<ArrayList<String>>> entry: grammar.entrySet()) {
                Casella temp = new Casella(entry.getKey());
                ArrayList<ArrayList<String>> listRulesForEntry = entry.getValue();
                for (ArrayList<String> rule : listRulesForEntry) {                                      // Sicuramente la parola singola sarà un simbolo terminale
                    if (rule.size() == 1 && rule.get(0).equals(line.get(j))) {
                        table.computeIfAbsent(j, k -> new HashMap<>());
                        if (!table.get(j).computeIfAbsent(j, k -> new ArrayList<>()).contains(temp))
                            table.get(j).get(j).add(temp);
                    }
                }
            }
            for (int i = j; i >= 0; i--) {
                for (int k = i; k <= j; k++) {
                    for (Map.Entry<String, ArrayList<ArrayList<String>>> entry: grammar.entrySet()) {
                        String keyRule = entry.getKey();
                        ArrayList<ArrayList<String>> listRulesForEntry = entry.getValue();
                        for (ArrayList<String> rule : listRulesForEntry) {
                            if (rule.size() == 2
                                    && table.get(i) != null && table.get(i).get(k) != null && haveParents(table.get(i).get(k), rule.get(0))
                                    && table.get(k+1) != null && table.get(k+1).get(j) != null && haveParents(table.get(k+1).get(j), rule.get(1))) {
                                table.computeIfAbsent(i, t -> new HashMap<>()).computeIfAbsent(j, t -> new ArrayList<>());
                                table.get(i).computeIfAbsent(j, t -> new ArrayList<>());
                                Casella tempAdd = new Casella(keyRule, getParent(table.get(i).get(k), rule.get(0)).get(), getParent(table.get(k+1).get(j), rule.get(1)).get());
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