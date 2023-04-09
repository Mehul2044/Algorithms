import java.util.*;

class CFG_CNF {

    HashMap<String, Set<String>> grammar;
    Set<String> terminal = new HashSet<>();
    Set<String> variable = new HashSet<>();
    Set<String> nullable = new HashSet<>();
    Set<String> lhs = new HashSet<>();
    Set<String> generating = new HashSet<>();
    Set<String> nonGenerating = new HashSet<>();
    Set<String> reachable = new HashSet<>();
    Set<String> nonReachable = new HashSet<>();

    CFG_CNF(HashMap<String, Set<String>> grammar) {
        this.grammar = grammar;
        driverFunction();
    }

    private void addTerminal_Variable() {
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            String symbols = entry.getKey();
            Set<String> ruleList = entry.getValue();
            variable.add(symbols);
            lhs.add(symbols);
            for (String rhs : ruleList) {
                int length = rhs.length();
                for (int i = 0; i < length; i++) {
                    char c = rhs.charAt(i);
                    if (c != '&' && Character.isLowerCase(c)) {
                        terminal.add(String.valueOf(c));
                    } else if (Character.isUpperCase(c)) {
                        variable.add(String.valueOf(c));
                    }
                }

            }
        }
    }

    private void findNullable() {
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            String symbols = entry.getKey();
            Set<String> ruleList = entry.getValue();
            for (String rhs : ruleList) {
                if (Objects.equals(rhs, "&")) {
                    nullable.add(symbols);
                    queue.add(symbols);
                }
            }
        }
        while (!queue.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
                String symbols = entry.getKey();
                Set<String> ruleList = entry.getValue();
                helperFunction(queue, ruleList, symbols, nullable);
            }
            queue.remove();
        }
    }

    private void findCombination(String symbols, String rhs) {
        if (rhs.length() != 0) {
            int x = rhs.length();
            for (int i = 0; i < x; i++) {
                if (nullable.contains(String.valueOf(rhs.charAt(i)))) {
                    String s = rhs.substring(0, i) + rhs.substring(i + 1);
                    if (s.length() > 0) {
                        grammar.get(symbols).add(s);
                    }
                    findCombination(symbols, s);
                }
            }
        }
    }

    private void nullableRemove() {
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            String symbols = entry.getKey();
            Set<String> ruleList = entry.getValue();
            Set<String> copyRule = new HashSet<>(ruleList);
            for (String rhsCopy : copyRule) {
                findCombination(symbols, rhsCopy);
            }
        }
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            Set<String> ruleList = entry.getValue();
            ruleList.remove("&");
        }
    }

    private void unitProductions() {
        boolean change = true;
        while (change) {
            change = false;
            for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
                Set<String> ruleList = entry.getValue();
                String symbols = entry.getKey();
                Set<String> copyRuleList = new HashSet<>(ruleList);
                for (String rhs : copyRuleList) {
                    if (rhs.length() == 1 && Character.isUpperCase(rhs.charAt(0))) {
                        change = true;
                        ruleList.remove(rhs);
                        grammar.get(symbols).addAll(grammar.get(rhs));
                    }
                }
            }
        }
    }

    private void removeNonGenerating() {
        helperFunction_2(nonGenerating);
    }

    private void findNonGenerating() {
        generating.add("S");
        generating.addAll(terminal);
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            Set<String> ruleList = entry.getValue();
            String symbols = entry.getKey();
            for (String rhs : ruleList) {
                if (generating.contains(rhs)) {
                    generating.add(symbols);
                    queue.add(symbols);
                }
            }
        }
        while (!queue.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
                Set<String> ruleList = entry.getValue();
                String symbols = entry.getKey();
                helperFunction(queue, ruleList, symbols, generating);
            }
            queue.remove();
        }
        nonGenerating.addAll(variable);
        nonGenerating.removeAll(generating);
    }

    private void removeNonReachable() {
        helperFunction_2(nonReachable);
    }

    private void findReachable(String start) {
        if (terminal.contains(start)) {
            reachable.add(start);
            return;
        }
        if (reachable.contains(start)) {
            return;
        }
        reachable.add(start);
        if (lhs.contains(start)) {
            for (String rh : grammar.get(start)) {
                for (int i = 0; i < rh.length(); i++) {
                    findReachable(String.valueOf(rh.charAt(i)));
                }
            }
        }
        nonReachable.addAll(terminal);
        nonReachable.addAll(variable);
        nonReachable.addAll(lhs);
        nonReachable.removeAll(reachable);
    }

    private String ruleExists(String value) {
        for (String variable : grammar.keySet()) {
            for (String rule : grammar.get(variable)) {
                if (rule.contains(value)) {
                    return rule;
                }
            }
        }
        return null;
    }

    private void finalStep() {
        Set<String> newChar = new HashSet<>(Set.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        newChar.removeAll(variable);
        newChar.removeAll(lhs);
        List<String> available = new ArrayList<>(newChar);
        boolean flag = true;
        List<String[]> finalAdd = new ArrayList<>();
        while (flag) {
            flag = false;
            for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
                String sym = entry.getKey();
                Set<String> rule = entry.getValue();
                for (String rhs : new HashSet<>(rule)) {
                    if (rhs.length() == 2) {
                        if (rhs.charAt(0) == rhs.charAt(1) && Character.isLowerCase(rhs.charAt(0)) && Character.isLowerCase(rhs.charAt(1))) {
                            String key = ruleExists(String.valueOf(rhs.charAt(0)));
                            if (key != null) {
                                grammar.get(sym).add(key + key);
                            }
                            grammar.get(sym).add(available.get(0) + available.get(0));
                            flag = helperFunction_3(available, finalAdd, sym, rhs);
                        } else if (Character.isLowerCase(rhs.charAt(0)) && Character.isUpperCase(rhs.charAt(1))) {
                            grammar.get(sym).add(available.get(0) + rhs.charAt(1));
                            flag = helperFunction_3(available, finalAdd, sym, rhs);
                        } else if (Character.isLowerCase(rhs.charAt(1)) && Character.isUpperCase(rhs.charAt(0))) {
                            grammar.get(sym).add(rhs.charAt(1) + available.get(0));
                            grammar.get(sym).remove(rhs);
                            Set<String> ad = new HashSet<>();
                            ad.add(Character.toString(rhs.charAt(1)));
                            finalAdd.add(new String[]{available.get(0), ad.iterator().next()});
                            available.remove(0);
                            flag = true;
                        }
                    }
                }
            }
        }
        for (String[] x : finalAdd) {
            String symbol = x[0];
            String rhs = x[1];
            grammar.computeIfAbsent(symbol, k -> new HashSet<>()).add(rhs);
        }
        flag = true;
        List<String[]> finding = new ArrayList<>();
        while (flag) {
            flag = false;
            for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
                String sym = entry.getKey();
                Set<String> rule = entry.getValue();
                for (String rhs : new HashSet<>(rule)) {
                    if (rhs.length() > 2) {
                        grammar.get(sym).add(rhs.charAt(0) + available.get(0));
                        finding.add(new String[]{available.get(0), rhs.substring(1)});
                        grammar.get(sym).remove(rhs);
                        available.remove(0);
                        flag = true;
                    }
                }
            }
        }
        for (String[] x : finding) {
            Set<String> nex = new HashSet<>();
            nex.add(x[1]);
            grammar.put(x[0], nex);
        }
    }


    private void helperFunction(Queue<String> queue, Set<String> ruleList, String symbols, Set<String> generating) {
        for (String rhs : ruleList) {
            Set<String> check = new HashSet<>();
            for (int i = 0; i < rhs.length(); i++) {
                check.add(String.valueOf(rhs.charAt(i)));
            }
            boolean find = true;
            for (String j : check) {
                if (!generating.contains(j)) {
                    find = false;
                    break;
                }
            }
            if (find) {
                if (!generating.contains(symbols)) {
                    queue.add(symbols);
                }
                generating.add(symbols);
            }
        }
    }

    private void helperFunction_2(Set<String> nonGenerating) {
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            Set<String> ruleList = entry.getValue();
            Set<String> copyRuleList = new HashSet<>(ruleList);
            for (String rhs : copyRuleList) {
                for (int i = 0; i < rhs.length(); i++) {
                    if (nonGenerating.contains(String.valueOf(rhs.charAt(i)))) {
                        ruleList.remove(rhs);
                    }
                }
            }
        }
        Queue<String> keysDelete = new LinkedList<>();
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            String symbols = entry.getKey();
            if (nonGenerating.contains(symbols)) {
                keysDelete.add(symbols);
            }
        }
        for (String i : keysDelete) {
            grammar.remove(i);
        }
    }

    private boolean helperFunction_3(List<String> available, List<String[]> finalAdd, String sym, String rhs) {
        grammar.get(sym).remove(rhs);
        Set<String> ad = new HashSet<>();
        ad.add(Character.toString(rhs.charAt(0)));
        finalAdd.add(new String[]{available.get(0), ad.iterator().next()});
        available.remove(0);
        return true;
    }

    private void driverFunction() {
        addTerminal_Variable();
        findNullable();
        nullableRemove();
        unitProductions();
        findNonGenerating();
        removeNonGenerating();
        findReachable("S");
        removeNonReachable();
        finalStep();
        print();
    }

    private void print() {
        System.out.println("The terminals are:" + terminal);
        System.out.println("The variables are:" + variable);
        System.out.println("The nullable variables are:" + nullable);
        System.out.println("The non-generating variables are:" + nonGenerating);
        System.out.println("The non-reachable variables are:" + nonReachable);
    }
}

class CYKAlgorithm {
    public static boolean cykAlgorithm(HashMap<String, Set<String>> grammar, String string) {
        int n = string.length();
        Set<String>[][] table = new Set[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                table[i][j] = new HashSet<>();
            }
        }
        for (int i = 0; i < n; i++) {
            for (String rule : grammar.keySet()) {
                if (grammar.get(rule).contains(String.valueOf(string.charAt(i)))) {
                    table[i][0].add(rule);
                }
            }
        }
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                for (int k = 0; k < j; k++) {
                    for (String sym : grammar.keySet()) {
                        for (String rhs : grammar.get(sym)) {
                            if (rhs.length() == 2 && table[i][k].contains(String.valueOf(rhs.charAt(0)))
                                    && table[i + k + 1][j - k - 1].contains(String.valueOf(rhs.charAt(1)))) {
                                table[i][j].add(sym);
                            }
                        }
                    }
                }
            }
        }
        return table[0][n - 1].contains("S");
    }
}

public class S20210010142_code {
    public static void main(String[] args) {
        HashMap<String, Set<String>> grammar = new HashMap<>();

        Set<String> s = new HashSet<>();
        s.add("AB");
        s.add("aB");
        grammar.put("S", s);

        Set<String> a = new HashSet<>();
        a.add("S");
        a.add("B");
        grammar.put("A", a);

        Set<String> b = new HashSet<>();
        b.add("&");
        b.add("b");
        grammar.put("B", b);

        System.out.println("The given grammar is:" + grammar);

        new CFG_CNF(grammar);
        System.out.println("The final grammar is:" + grammar);

        System.out.println("Membership test for string bb is:" + CYKAlgorithm.cykAlgorithm(grammar, "bb"));
        System.out.println("Membership test for string aa is:" + CYKAlgorithm.cykAlgorithm(grammar, "aa"));
    }
}