import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class Main {

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        SymbolCounter counter = new SymbolCounter();
        try {
            handler.parseArgs(args);
            Path input = Paths.get(handler.getInput());
            Path output = Paths.get(handler.getOutput());
            int bound = handler.getBound();
            try (Stream<String> s = Files.lines(input)) {
                s.forEach(counter::process);
            }
            Files.write(output, counter.collectStatistics(bound));
        } catch (ParseException | FileNotFoundException x) {
            System.err.println("Invalid arguments: " + x.getMessage());
        } catch (IOException x) {
            System.err.println("General I/O exception: " + x.getMessage() + "\nException belongs to " + x.getClass());
        }
    }
}