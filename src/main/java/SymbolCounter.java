

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import org.apache.commons.cli.*;

import java.util.*;

public class SymbolCounter {

    public static String CHARSET = "UTF-8";
    public static String INPUT_DFLT = "input.txt";
    public static String OUTPUT_DFLT = "output.txt";
    private Map<Character, Integer> smblFreq = new LinkedHashMap<>();
    private String input;
    private String output;

    private void fillMap(String line) {
        line = line.toLowerCase().replaceAll("\\s+", "");
        for (int i = 0; i < line.length(); i++) {
            Character ch = line.charAt(i);
            Integer count = smblFreq.get(ch);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            smblFreq.put(ch, count);
        }
    }

    private void sortMap() {
        List<Map.Entry<Character, Integer>> entryList = new LinkedList(smblFreq.entrySet());
        entryList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        smblFreq.clear();
        for (Map.Entry<Character, Integer> entry : entryList) {
            smblFreq.put(entry.getKey(), entry.getValue());
        }

    }

    private int countTotal() {
        int total = 0;
        for (Map.Entry<Character, Integer> entry : smblFreq.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public void write() {
        int total = countTotal();
        Charset charset = Charset.forName(CHARSET);
        Path file = Paths.get(output);
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            if (total == 0) {
                writer.write("No data");
            } else {
                DecimalFormat df = new DecimalFormat("#0.0");
                for (Map.Entry<Character, Integer> entry : smblFreq.entrySet()) {

                    StringBuffer s = new StringBuffer(entry.getKey() + " (" + df.format(entry.getValue() * 100.0 / total) + " %):  ");
                    int rate = (int) Math.ceil(entry.getValue() * 100.0 / total);
                    for (int i = 0; i < rate; i++) {
                        s.append("#");
                    }
                    s.append("\n");
                    writer.write(s.toString(), 0, s.length());

                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }


    public void read() {
        Charset charset = Charset.forName(CHARSET);
        Path file = Paths.get(input);
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {

            String line;
            while ((line = reader.readLine()) != null) {
                fillMap(line);
            }

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public void parseArgs(String[] args) throws ParseException {
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


    public static void main(String[] args) {

        SymbolCounter smblCntr = new SymbolCounter();

        try {
            smblCntr.parseArgs(args);
            smblCntr.read();
            smblCntr.sortMap();
            smblCntr.write();

        } catch (ParseException x) {
            System.err.format("ParseException: %s%n", x);
        }


    }
}
