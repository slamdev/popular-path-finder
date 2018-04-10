package com.github.slamdev.popular.path.finder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineParser.class);

    static final int INVALID_ARGUMENT_CODE = 2;

    public CommandLineOptions parse(String... args) {
        validateArgs(args);
        String limit = args[0];
        validateLimit(limit);
        Path file = Paths.get(args[1]);
        validateFile(file);
        return new CommandLineOptions(file, Integer.parseInt(limit));
    }

    private void validateLimit(String value) {
        int limit;
        try {
            limit = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.debug("", e);
            throw new UserInputException(INVALID_ARGUMENT_CODE, "Aggregation limit should be a number", e);
        }
        if (limit <= 0) {
            throw new UserInputException(INVALID_ARGUMENT_CODE, "Aggregation limit should be a positive number");
        }
    }

    private void validateArgs(String... args) {
        if (args.length == 0) {
            throw new UserInputException(INVALID_ARGUMENT_CODE, "Aggregation limit and path to a file should be provided");
        }
        if (args.length > 2) {
            throw new UserInputException(INVALID_ARGUMENT_CODE, "Only two arguments are supported");
        }
    }

    private void validateFile(Path file) {
        if (!Files.isRegularFile(file)) {
            throw new UserInputException(INVALID_ARGUMENT_CODE, "Passed argument is not a file");
        }
    }
}
