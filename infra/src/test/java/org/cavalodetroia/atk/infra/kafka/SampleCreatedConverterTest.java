package org.cavalodetroia.atk.infra.kafka;

import org.cavalodetroia.atk.domain.sample.Sample;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SampleCreatedConverterTest {

    private final SampleCreatedConverter converter = new SampleCreatedConverter();

    @Test
    void should_mapAllFields_when_convertingToMessage() {
        final Sample sample = new Sample(UUID.randomUUID(), "first sample");

        final var message = converter.toMessage(sample);

        assertThat(message.getSampleId()).isEqualTo(sample.id().toString());
        assertThat(message.getName()).isEqualTo("first sample");
    }
}
