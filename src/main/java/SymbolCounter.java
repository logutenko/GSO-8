import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolCounter {

    private Map<Character, Integer> symbols = new HashMap<>();

    public void process(String text) {

        text.chars()
                .mapToObj(ch -> (char) ch)
                .forEach(s -> symbols.merge(s, 1, (a, b) -> a + b));
    }

    public List<String> collectStatistics() {
        int total = symbols.values().stream().mapToInt(Number::intValue).sum();
        if (total == 0) {
            return Stream.of("No data")
                    .collect(Collectors.toList());
        } else {
            DecimalFormat df = new DecimalFormat("#0.0");
            return symbols.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                    .map((x) -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append(x.getKey()).append(" (").append(df.format(x.getValue() * 100.0 / total)).append(" %):  ");
                        int rate = (int) Math.ceil(x.getValue() * 100.0 / total);
                        for (int i = 0; i < rate; i++) {
                            sb.append("#");
                        }
                        return sb.toString();
                    })
                    .collect(Collectors.toList());
        }
    }
}
