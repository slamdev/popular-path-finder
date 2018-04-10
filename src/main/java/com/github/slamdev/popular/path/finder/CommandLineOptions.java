package com.github.slamdev.popular.path.finder;

import java.nio.file.Path;
import java.util.Objects;

public class CommandLineOptions {

    private final Path file;

    private final int limit;

    public CommandLineOptions(Path file, int limit) {
        this.file = file;
        this.limit = limit;
    }

    public Path getFile() {
        return file;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandLineOptions that = (CommandLineOptions) o;
        return limit == that.limit
                && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, limit);
    }

    @Override
    public String toString() {
        return "CommandLineOptions{"
                + "file=" + file
                + ", limit=" + limit
                + '}';
    }
}
