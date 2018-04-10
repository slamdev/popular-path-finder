package com.github.slamdev.popular.path.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

public class Metric {

    private final List<String> paths;
    private final long frequency;

    public Metric(List<String> paths, long frequency) {
        this.paths = new ArrayList<>(paths);
        this.frequency = frequency;
    }

    public List<String> getPaths() {
        return unmodifiableList(paths);
    }

    public long getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metric metric = (Metric) o;
        return frequency == metric.frequency
                && Objects.equals(paths, metric.paths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paths, frequency);
    }

    @Override
    public String toString() {
        return "Metric{"
                + "paths=" + paths
                + ", frequency=" + frequency
                + '}';
    }
}
