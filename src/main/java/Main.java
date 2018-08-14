import org.apache.commons.cli.ParseException;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class Main {

    private static String CHARSET = "UTF-8";

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        SymbolCounter counter = new SymbolCounter();
        Charset charset = Charset.forName(CHARSET);
        Path file;
        try {
            handler.parseArgs(args);
            file = Paths.get(handler.getInput());
            try (Stream<String> stream = Files.lines(file)) {
                stream.map(x -> x.toLowerCase().replaceAll("\\s+", ""))
                        .filter(x -> !x.isEmpty())
                        .forEach(counter::process);
            }
            file = Paths.get(handler.getOutput());
            try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
                for (String line : counter.collectStatistics()) {
                    writer.write(line + "\n");
                }
            }
        } catch (ParseException x) {
            System.err.println("A ParseException was caught: " + x.getMessage());
        } catch (FileNotFoundException x) {
            System.err.println(x.getMessage());
        } catch (IOException x) {
            System.err.println("General I/O exception: " + x.getMessage());
            System.err.println("Exception belongs to " + x.getClass());
        }
    }
}