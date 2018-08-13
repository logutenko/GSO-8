import java.text.DecimalFormat;
import java.util.*;

public class SymbolCounter {

    private Map<Character, Integer> smblFreq = new LinkedHashMap<>();

    private int countTotal() {
        int total = 0;
        for (Map.Entry<Character, Integer> entry : smblFreq.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public void sort() {
        List<Map.Entry<Character, Integer>> entryList = new LinkedList(smblFreq.entrySet());
        entryList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        smblFreq.clear();
        for (Map.Entry<Character, Integer> entry : entryList) {
            smblFreq.put(entry.getKey(), entry.getValue());
        }
    }

    public void process(String text) {
        text = text.toLowerCase().replaceAll("\\s+", "");
        for (int i = 0; i < text.length(); i++) {
            Character ch = text.charAt(i);
            Integer count = smblFreq.get(ch);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            smblFreq.put(ch, count);
        }
    }

    public String statistics() {
        StringBuilder s = new StringBuilder();
        int total = countTotal();
        if (total == 0) {
            s.append("No data");
        } else {
            DecimalFormat df = new DecimalFormat("#0.0");
            for (Map.Entry<Character, Integer> entry : smblFreq.entrySet()) {
                s.append(entry.getKey()).append(" (").append(df.format(entry.getValue() * 100.0 / total)).append(" %):  ");
                int rate = (int) Math.ceil(entry.getValue() * 100.0 / total);
                for (int i = 0; i < rate; i++) {
                    s.append("#");
                }
                s.append("\n");
            }
        }
        return s.toString();
    }
}
