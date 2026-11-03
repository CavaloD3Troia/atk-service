package org.cavalodetroia.atk.infra.kafka;

import org.cavalodetroia.contracts.template.v1.SampleCreated;
import org.cavalodetroia.atk.domain.sample.Sample;
import org.springframework.stereotype.Component;

@Component
public class SampleCreatedConverter {

    public SampleCreated toMessage(final Sample sample) {
        return SampleCreated.newBuilder()
                .setSampleId(sample.id().toString())
                .setName(sample.name())
                .build();
    }
}
