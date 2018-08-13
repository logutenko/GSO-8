import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    private static String CHARSET = "UTF-8";

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        SymbolCounter counter = new SymbolCounter();
        Charset charset = Charset.forName(CHARSET);
        Path file;
        try {
            handler.parseArgs(args);
            file = Paths.get(handler.inputPath());
            try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    counter.process(line);
                }
                counter.sort();
            }
            file = Paths.get(handler.outputPath());
            try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
                String result = counter.statistics();
                writer.write(result, 0, result.length());
            }
        } catch (ParseException x) {
            System.err.format("ParseException: %s%n", x);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        } catch (Exception x) {
            System.err.format("Exception: %s%n", x);
        }
    }
}
