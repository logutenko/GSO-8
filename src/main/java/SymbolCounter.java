import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolCounter {

    private Map<Character, Integer> symbols = new HashMap<>();

    public void process(String text) {
        text.toLowerCase().chars()
                .mapToObj(ch -> (char) ch)
                .filter(ch -> ch != ' ')
                .forEach(ch -> symbols.compute(ch, (key, value) -> value == null ? 1 : value + 1));
    }

    public List<String> collectStatistics(int bound) {
        int total = symbols.values().stream().mapToInt(Number::intValue).sum();
        if (total == 0) {
            return Arrays.asList("No data");
        }
        String formatter = "%c (%.1f %%): %s";
        return symbols.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(x -> {
                    int rate = (int) Math.ceil(x.getValue() * 100.0 / total);
                    String bar = Stream.generate(() -> "#").limit(rate).reduce("", String::concat);
                    return String.format(formatter, x.getKey(), x.getValue() * 100.0 / total, bar);
                })
                .limit(bound != 0 ? bound : symbols.entrySet().size())
                .collect(Collectors.toList());
    }
}
