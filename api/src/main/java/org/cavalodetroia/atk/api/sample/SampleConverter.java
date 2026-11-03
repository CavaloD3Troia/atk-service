package org.cavalodetroia.atk.api.sample;

import org.cavalodetroia.atk.domain.sample.Sample;
import org.springframework.stereotype.Component;

@Component
public class SampleConverter {

    public SampleResponse toResponse(final Sample sample) {
        return new SampleResponse(sample.id(), sample.name());
    }
}
