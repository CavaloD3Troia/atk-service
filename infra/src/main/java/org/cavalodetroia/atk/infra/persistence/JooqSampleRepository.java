package org.cavalodetroia.atk.infra.persistence;

import org.cavalodetroia.atk.domain.sample.Sample;
import org.cavalodetroia.atk.domain.sample.port.out.SampleRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class JooqSampleRepository implements SampleRepository {

    private final DSLContext dsl;
    private final SampleRecordConverter converter;

    public JooqSampleRepository(final DSLContext dsl, final SampleRecordConverter converter) {
        this.dsl = dsl;
        this.converter = converter;
    }

    @Override
    public void save(final Sample sample) {
        dsl.executeInsert(converter.toRecord(sample));
    }
}
