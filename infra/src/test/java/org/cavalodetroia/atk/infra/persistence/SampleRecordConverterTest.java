package org.cavalodetroia.atk.infra.persistence;

import org.cavalodetroia.atk.domain.sample.Sample;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SampleRecordConverterTest {

    private final SampleRecordConverter converter = new SampleRecordConverter();

    @Test
    void should_mapAllFields_when_convertingToRecord() {
        final Sample sample = new Sample(UUID.randomUUID(), "first sample");

        final var record = converter.toRecord(sample);

        assertThat(record.getId()).isEqualTo(sample.id());
        assertThat(record.getName()).isEqualTo("first sample");
    }
}
