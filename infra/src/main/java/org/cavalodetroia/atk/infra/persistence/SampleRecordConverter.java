package org.cavalodetroia.atk.infra.persistence;

import org.cavalodetroia.atk.domain.sample.Sample;
import org.cavalodetroia.atk.infra.persistence.jooq.tables.records.SampleRecord;
import org.springframework.stereotype.Component;

@Component
public class SampleRecordConverter {

    public SampleRecord toRecord(final Sample sample) {
        return new SampleRecord(sample.id(), sample.name());
    }
}
