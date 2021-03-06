import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolCounter {

    public static final String FORMATTER = "%c (%.1f %%): %s";
    private Map<Character, Integer> symbols = new HashMap<>();

    public void process(String text) {
        text.toLowerCase().chars()
                .mapToObj(symbol -> (char) symbol)
                .filter(symbol -> symbol != ' ')
                .forEach(symbol -> symbols.compute(symbol, (key, value) -> value == null ? 1 : value + 1));
    }

    public List<String> collectStatistics(int bound) {
        int total = symbols.values().stream().mapToInt(Number::intValue).sum();
        if (total == 0) {
            return Collections.singletonList("No data");
        }
        bound = (bound != 0) ? bound : symbols.size();
        Function<Integer, String> bar = value -> {
            int rate = (int) Math.ceil(value * 100.0 / total);
            return Stream.generate(() -> "#").limit(rate).collect(Collectors.joining());
        };
        return symbols.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .limit(bound)
                .map(entry -> String.format(FORMATTER, entry.getKey(), entry.getValue() * 100.0 / total, bar.apply(entry.getValue())))
                .collect(Collectors.toList());
    }
}
