package org.cavalodetroia.atk.domain.sample.port.out;

import org.cavalodetroia.atk.domain.sample.Sample;

/**
 * Tells the rest of the platform that a sample was created.
 */
public interface SamplePublisher {

    void publish(Sample sample);
}
