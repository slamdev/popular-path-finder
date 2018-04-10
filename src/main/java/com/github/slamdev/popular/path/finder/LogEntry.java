package com.github.slamdev.popular.path.finder;

import java.util.Objects;

public class LogEntry {

    private final String user;
    private final String page;

    public LogEntry(String user, String page) {
        this.user = user;
        this.page = page;
    }

    public String getUser() {
        return user;
    }

    public String getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogEntry logEntry = (LogEntry) o;
        return Objects.equals(user, logEntry.user)
                && Objects.equals(page, logEntry.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, page);
    }

    @Override
    public String toString() {
        return "LogEntry{"
                + "user='" + user + '\''
                + ", page='" + page + '\''
                + '}';
    }
}
