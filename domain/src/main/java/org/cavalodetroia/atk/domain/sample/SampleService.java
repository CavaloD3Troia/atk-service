package org.cavalodetroia.atk.domain.sample;

import org.cavalodetroia.atk.domain.sample.port.in.CreateSample;
import org.cavalodetroia.atk.domain.sample.port.out.SamplePublisher;
import org.cavalodetroia.atk.domain.sample.port.out.SampleRepository;

import java.util.UUID;

public class SampleService implements CreateSample {

    private final SampleRepository repository;
    private final SamplePublisher publisher;

    public SampleService(final SampleRepository repository, final SamplePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public Sample create(final String name) {
        final Sample sample = new Sample(UUID.randomUUID(), name);

        repository.save(sample);
        publisher.publish(sample);
        return sample;
    }
}
