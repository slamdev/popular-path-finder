package com.github.slamdev.popular.path.finder;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.slamdev.popular.path.finder.MetricAggregator.PAGE_AGGREGATION_SIZE;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class MetricAggregatorTest {

    private final MetricAggregator aggregator = new MetricAggregator();

    @Test
    void shouldReturnEmptyListForEmptyLogEntries() {
        List<Metric> metrics = aggregator.aggregate(emptyList(), 10);
        assertThat(metrics).isEmpty();
    }

    @Test
    void shouldReturnEmptyListForLogEntriesWithPagesLessThanAggregationSize() {
        List<LogEntry> logEntries = IntStream
                .range(1, PAGE_AGGREGATION_SIZE)
                .mapToObj(String::valueOf)
                .map(index -> new LogEntry("u", index))
                .collect(toList());
        List<Metric> metrics = aggregator.aggregate(logEntries, 10);
        assertThat(metrics).isEmpty();
    }

    @Test
    void shouldSortMetricsByFrequency() {
        List<LogEntry> logEntries = Stream.concat(
                IntStream
                        .rangeClosed(1, PAGE_AGGREGATION_SIZE)
                        .mapToObj(String::valueOf)
                        .map(index -> new LogEntry("u", "less-frequent")),
                IntStream
                        .rangeClosed(1, PAGE_AGGREGATION_SIZE + 1)
                        .mapToObj(String::valueOf)
                        .map(index -> new LogEntry("u", "most-frequent"))
        ).collect(toList());
        List<Metric> metrics = aggregator.aggregate(logEntries, 10);
        assertThat(metrics)
                .extracting("frequency")
                .containsExactly(2L, 1L, 1L, 1L);
    }

    @Test
    void shouldLimitAggregatedMetrics() {
        List<LogEntry> logEntries = IntStream
                .rangeClosed(1, PAGE_AGGREGATION_SIZE + 1)
                .mapToObj(String::valueOf)
                .map(index -> new LogEntry("u", index))
                .collect(toList());
        List<Metric> metrics = aggregator.aggregate(logEntries, 1);
        assertThat(metrics).hasSize(1);
    }
}
