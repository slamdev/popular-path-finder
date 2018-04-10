package com.github.slamdev.popular.path.finder;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class MetricWriterTest {

    private final MetricWriter writer = new MetricWriter();

    @Test
    void shouldWriteEmptyArrayWhenNoMetricsProvided() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.write(stream, emptyList());
        assertThat(stream.toString()).isEqualTo("[]");
    }

    @Test
    void shouldWriteMetricsAsJsonArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.write(stream, singletonList(new Metric(singletonList("/"), 1)));
        assertThat(stream.toString()).isEqualTo("[{\"paths\":[\"/\"],\"frequency\":1}]");
    }
}
