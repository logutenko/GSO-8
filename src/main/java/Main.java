import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static String CHARSET = "UTF-8";
    public static String INPUT_DFLT = "input.txt";
    public static String OUTPUT_DFLT = "output.txt";
    private String input;
    private String output;

    private void parseArgs(String[] args) throws ParseException {
        Option optInput = new Option("i", "input", true, "Fill to provide input file (\"input.txt\" by default)");
        optInput.setArgs(1);
        Option optOutput = new Option("o", "output", true, "Fill to provide output file (\"output.txt\" by default)");
        optOutput.setArgs(1);
        Option optHelp = new Option("h", "help", false, "Help information");
        Options optionList = new Options();
        optionList.addOption(optInput);
        optionList.addOption(optOutput);
        optionList.addOption(optHelp);
        CommandLineParser cmdLinePosixParser = new DefaultParser();
        CommandLine commandLine = cmdLinePosixParser.parse(optionList, args);

        if (commandLine.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("app counts frequency of symbols' occurrence:", optionList);
        }
        if (commandLine.hasOption("i")) {
            String[] arguments = commandLine.getOptionValues("i");
            input = arguments[0];
        } else {
            input = INPUT_DFLT;
        }
        if (commandLine.hasOption("o")) {
            String[] arguments = commandLine.getOptionValues("o");
            output = arguments[0];
        } else {
            output = OUTPUT_DFLT;
        }


    }

    private String read() {
        Charset charset = Charset.forName(CHARSET);
        Path file = Paths.get(input);
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return text.toString();
    }


    private void write(String text) {

        Charset charset = Charset.forName(CHARSET);
        Path file = Paths.get(output);
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(text, 0, text.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public static void main(String[] args) {

        SymbolCounter smblCntr = new SymbolCounter();
        Main handler = new Main();
        try {

            handler.parseArgs(args);
            smblCntr.process(handler.read());
            handler.write(smblCntr.statistics());

        } catch (ParseException x) {
            System.err.format("ParseException: %s%n", x);
        }


    }
}
