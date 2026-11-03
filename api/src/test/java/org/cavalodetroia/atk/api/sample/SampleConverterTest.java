package org.cavalodetroia.atk.api.sample;

import org.cavalodetroia.atk.domain.sample.Sample;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SampleConverterTest {

    private final SampleConverter converter = new SampleConverter();

    @Test
    void should_mapAllFields_when_convertingToResponse() {
        final Sample sample = new Sample(UUID.randomUUID(), "first sample");

        assertThat(converter.toResponse(sample)).isEqualTo(new SampleResponse(sample.id(), "first sample"));
    }
}
