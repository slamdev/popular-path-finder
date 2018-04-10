package com.github.slamdev.popular.path.finder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.github.slamdev.popular.path.finder.AccessLogReader.INVALID_FILE_CONTENT_CODE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AccessLogReaderTest {

    private static final List<Path> TEMP_FILES = new ArrayList<>();

    private final AccessLogReader reader = new AccessLogReader();

    @AfterAll
    static void cleanUpTempFiles() {
        TEMP_FILES.forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException ignore) {
            }
        });
        TEMP_FILES.clear();
    }

    @Test
    void shouldThrowExceptionWhenFileContentIsNotValidJson() {
        Path file = createFile("invalid data");
        assertThatThrownBy(() -> reader.read(file))
                .isInstanceOf(UserInputException.class)
                .hasMessage("File should contain valid JSON array")
                .hasFieldOrPropertyWithValue("code", INVALID_FILE_CONTENT_CODE);
    }

    @Test
    void shouldConvertJsonArrayToListOfLogEntries() {
        String user = "u";
        String page = "/";
        Path file = createFile(String.format("[{\"user\": \"%s\",\"page\": \"%s\"}]", user, page));
        List<LogEntry> logEntries = reader.read(file);
        LogEntry logEntry = new LogEntry(user, page);
        Assertions.assertThat(logEntries).containsOnly(logEntry);
    }

    private Path createFile(String content) {
        try {
            Path file = Files.createTempFile(null, null);
            Files.write(file, content.getBytes(UTF_8));
            TEMP_FILES.add(file);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
