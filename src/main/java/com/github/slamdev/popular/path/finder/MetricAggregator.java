package com.github.slamdev.popular.path.finder;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class MetricAggregator {

    static final int PAGE_AGGREGATION_SIZE = 3;

    public List<Metric> aggregate(List<LogEntry> logEntries, int limit) {
        return logEntries.stream()
                // group log entries by user
                .collect(groupingBy(LogEntry::getUser))
                .entrySet().stream()
                .map(Map.Entry::getValue)
                // remove log entries that are less than minimum aggregation size
                .filter(this::isThreePagePath)
                // partition log entries by aggregation size
                .map(this::splitByPathGroups)
                .flatMap(List::stream)
                // count frequency of aggregated entries
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .map(e -> new Metric(e.getKey(), e.getValue()))
                // sort results by frequency
                .sorted(comparing(Metric::getFrequency).reversed())
                // skip results with the lowest frequency
                .limit(limit)
                .collect(toList());
    }

    private boolean isThreePagePath(List<LogEntry> logEntries) {
        return logEntries.size() >= PAGE_AGGREGATION_SIZE;
    }

    private List<List<String>> splitByPathGroups(List<LogEntry> logEntries) {
        return IntStream
                .rangeClosed(0, logEntries.size() - PAGE_AGGREGATION_SIZE)
                .mapToObj(index -> logEntries.subList(index, index + PAGE_AGGREGATION_SIZE))
                .map(subEntries -> subEntries.stream().map(LogEntry::getPage).collect(toList()))
                .collect(toList());
    }
}
