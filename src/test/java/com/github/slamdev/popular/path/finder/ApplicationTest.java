package com.github.slamdev.popular.path.finder;

import com.github.slamdev.popular.path.finder.asserts.SystemOutAssert;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.github.slamdev.popular.path.finder.asserts.SystemExitAssert.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {

    @InjectMocks
    private Application app;

    @Mock
    private CommandLineParser commandLineParser;

    @Mock
    private AccessLogReader accessLogReader;

    @Mock
    private MetricAggregator metricAggregator;

    @Mock
    private MetricWriter metricWriter;

    @BeforeEach
    void configureDefaultMocks() {
        when(commandLineParser.parse(any())).thenReturn(new CommandLineOptions(null, 0));
    }

    @Test
    void shouldCallCommandLineParserWithInputArgs() {
        String arg = "arg";
        app.run(arg);
        verify(commandLineParser).parse(arg);
    }

    @Test
    void shouldCallAccessLogReaderWithInputFile() {
        Path file = Paths.get(".");
        when(commandLineParser.parse(any())).thenReturn(new CommandLineOptions(file, 0));
        app.run();
        verify(accessLogReader).read(file);
    }

    @Test
    void shouldCallMetricAggregatorWithLogEntries() {
        List<LogEntry> logEntries = singletonList(new LogEntry("u", "/"));
        when(accessLogReader.read(any())).thenReturn(logEntries);
        app.run();
        verify(metricAggregator).aggregate(eq(logEntries), anyInt());
    }

    @Test
    void shouldCallMetricWriterWithSystemOutAndMetrics() {
        List<Metric> metrics = singletonList(new Metric(singletonList("/"), 1));
        when(metricAggregator.aggregate(anyList(), anyInt())).thenReturn(metrics);
        app.run();
        verify(metricWriter).write(System.out, metrics);
    }

    @Test
    void shouldExitAppWithErrorCodeAndMessageFromUserInputException() {
        int exitCode = 1;
        String message = "some message";
        when(commandLineParser.parse(any())).thenThrow(new UserInputException(exitCode, message));
        assertThat(() -> app.run())
                .hasExitCode(exitCode)
                .hasExitMessage(message);
    }

    @Test
    void shouldAggregateTopTwoPathsFromRealInput() {
        String inputFile = classPathResource("sampleInput.json").getPath();
        String expectedOutput = classPathResourceToString("expectedOutput_2.json");
        SystemOutAssert.assertThat(() -> Application.main(new String[]{"2", inputFile}))
                .isJsonEqualTo(expectedOutput);
    }

    @Test
    void shouldAggregateTopTenPathsFromRealInput() {
        String inputFile = classPathResource("sampleInput.json").getPath();
        String expectedOutput = classPathResourceToString("expectedOutput_10.json");
        SystemOutAssert.assertThat(() -> Application.main(new String[]{"10", inputFile}))
                .isJsonEqualTo(expectedOutput);
    }

    private URI classPathResource(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            return classLoader.getResource(resource).toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private String classPathResourceToString(String resource) {
        try {
            URI path = classPathResource(resource);
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return new String(bytes, UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
