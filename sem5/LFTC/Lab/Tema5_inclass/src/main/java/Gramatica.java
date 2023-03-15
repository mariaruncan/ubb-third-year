import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gramatica {
    private Map<String, List<List<String>>> rules = new HashMap<>();

    public void addRule(String left, List<String> right) {
        if (rules.containsKey(left)) {
            rules.get(left).add(right);
        } else {
            List<List<String>> newList = List.of(right);
            rules.put(left, newList);
        }
    }

    public void printRulesOf(String left) {
        List<List<String>> rulesOf = rules.get(left);
        rulesOf.forEach(rule -> {
            String ruleString = rule.stream().reduce("", (substr, elem) -> substr + " " + elem);
            System.out.println(left + " -> " + ruleString);
        });
    }
}
