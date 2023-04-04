import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CYK {

    /**
     * SCRIVERE
     * @param line
     * @param grammar
     * @return
     */
    static HashMap<Integer, HashMap<Integer, ArrayList<String>>> CykAlgorithm (ArrayList<String> line, HashMap<String, ArrayList<ArrayList<String>>> grammar) {
        HashMap<Integer, HashMap<Integer, ArrayList<String>>> table = new HashMap<>();                  // Table for the result
        int numWords = line.size();
        for (int j = 0; j < numWords; j++) {

            // Controllo tutte le regole per riempire la riga base
            for (Map.Entry<String, ArrayList<ArrayList<String>>> entry: grammar.entrySet()) {
                String keyRule = entry.getKey();
                ArrayList<ArrayList<String>> listRulesForEntry = entry.getValue();

                // Sicuramente la parola singola sarà un simbolo terminale
                for (ArrayList<String> rule : listRulesForEntry) {
                    if (rule.size() == 1 && rule.get(0).equals(line.get(j))) {
                        table.computeIfAbsent(j, k -> new HashMap<>());
                        if (!table.get(j).computeIfAbsent(j, k -> new ArrayList<>()).contains(keyRule))
                            table.get(j).get(j).add(keyRule);                                           // Aggiungo il simbolo sx alla tabella
                    }
                }
            }

            for (int i = j; i >= 0; i--) {
                for (int k = i; k <= j; k++) {
                    for (Map.Entry<String, ArrayList<ArrayList<String>>> entry: grammar.entrySet()) {
                        String keyRule = entry.getKey();
                        ArrayList<ArrayList<String>> listRulesForEntry = entry.getValue();

                        for (ArrayList<String> rule : listRulesForEntry) {
                            // Controllo se table[i, k] e table[k, j] hanno dei valori e se tra quelli ci sono dei simboli della regola che sto considerando
                            if (rule.size() == 2 && table.get(i) != null && table.get(i).get(k) != null && table.get(i).get(k).contains(rule.get(0))
                                && table.get(k+1) != null && table.get(k+1).get(j) != null && table.get(k+1).get(j).contains(rule.get(1))) {
                                table.computeIfAbsent(i, t -> new HashMap<>());
                                if (!table.get(i).computeIfAbsent(j, t -> new ArrayList<>()).contains(keyRule))
                                    table.get(i).get(j).add(keyRule);
                            }
                        }
                    }
                }
            }
        }
        return table;
    }
}