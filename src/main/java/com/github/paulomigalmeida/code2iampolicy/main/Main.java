package com.github.paulomigalmeida.code2iampolicy.main;

import com.github.paulomigalmeida.code2iampolicy.controller.ExecutionController;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    private static Options createOptions() {
        // create Options object
        Options options = new Options();

        Option directory = new Option("d", "directory", true,
                "directory in which the java code to be parsed is");
        directory.setRequired(true);
        Option awsSdkDirectory = new Option("s", "sdk", true,
                "directory in which the aws java sdk code is located");
        awsSdkDirectory.setRequired(true);

        options.addOption(directory);
        options.addOption(awsSdkDirectory);

        return options;
    }

    public static void main(String[] args) throws IOException {
        // create the parser
        DefaultParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(createOptions(), args);
            new ExecutionController(
                    Paths.get(line.getOptionValue("d")),
                    Paths.get(line.getOptionValue("s"))
            ).execute();
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
    }
}
