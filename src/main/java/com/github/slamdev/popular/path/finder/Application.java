package com.github.slamdev.popular.path.finder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final CommandLineParser commandLineParser;

    private final AccessLogReader accessLogReader;

    private final MetricAggregator metricAggregator;

    private final MetricWriter metricWriter;

    public static void main(String[] args) {
        CommandLineParser commandLineParser = new CommandLineParser();
        AccessLogReader accessLogReader = new AccessLogReader();
        MetricAggregator metricAggregator = new MetricAggregator();
        MetricWriter metricWriter = new MetricWriter();
        new Application(commandLineParser, accessLogReader, metricAggregator, metricWriter)
                .run(args);
    }

    private Application(CommandLineParser commandLineParser, AccessLogReader accessLogReader,
                        MetricAggregator metricAggregator, MetricWriter metricWriter) {
        this.commandLineParser = commandLineParser;
        this.accessLogReader = accessLogReader;
        this.metricAggregator = metricAggregator;
        this.metricWriter = metricWriter;
    }

    public void run(String... args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> handleException(e));
        CommandLineOptions options = commandLineParser.parse(args);
        List<LogEntry> logEntries = accessLogReader.read(options.getFile());
        List<Metric> metrics = metricAggregator.aggregate(logEntries, options.getLimit());
        metricWriter.write(System.out, metrics);
    }

    private static void handleException(Throwable throwable) {
        if (throwable instanceof UserInputException) {
            UserInputException exception = (UserInputException) throwable;
            System.err.println(exception.getMessage());
            System.exit(exception.getCode());
        }
        LOGGER.error("", throwable);
    }
}
