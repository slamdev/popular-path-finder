package com.github.slamdev.popular.path.finder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.slamdev.popular.path.finder.CommandLineParser.INVALID_ARGUMENT_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CommandLineParserTest {

    private static Path file;
    private final CommandLineParser parser = new CommandLineParser();

    @BeforeAll
    static void createTempFile() {
        try {
            file = Files.createTempFile(null, null);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterAll
    static void deleteTempFile() {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void shouldThrowExceptionWhenNoArgumentsPassed() {
        assertThatThrownBy(parser::parse)
                .isInstanceOf(UserInputException.class)
                .hasMessage("Aggregation limit and path to a file should be provided")
                .hasFieldOrPropertyWithValue("code", INVALID_ARGUMENT_CODE);
    }

    @Test
    void shouldThrowExceptionWhenMoreThanTwoArgumentPassed() {
        assertThatThrownBy(() -> parser.parse("1", "2", "3"))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Only two arguments are supported")
                .hasFieldOrPropertyWithValue("code", INVALID_ARGUMENT_CODE);
    }

    @Test
    void shouldThrowExceptionWhenPassedArgumentIsNotAFile() {
        assertThatThrownBy(() -> parser.parse("1", "not-existing-file"))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Passed argument is not a file")
                .hasFieldOrPropertyWithValue("code", INVALID_ARGUMENT_CODE);
    }

    @Test
    void shouldReturnFileByPath() {
        assertThat(parser.parse("1", file.toString()).getFile())
                .isEqualTo(file);
    }

    @Test
    void shouldThrowExceptionWhenPassedArgumentIsNotANumber() {
        assertThatThrownBy(() -> parser.parse("test", file.toString()))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Aggregation limit should be a number")
                .hasFieldOrPropertyWithValue("code", INVALID_ARGUMENT_CODE);
    }

    @Test
    void shouldThrowExceptionWhenPassedArgumentIsNotLessThanOne() {
        assertThatThrownBy(() -> parser.parse("0", file.toString()))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Aggregation limit should be a positive number")
                .hasFieldOrPropertyWithValue("code", INVALID_ARGUMENT_CODE);
    }
}
