package org.cavalodetroia.atk.domain.sample;

import org.cavalodetroia.atk.domain.sample.port.out.SamplePublisher;
import org.cavalodetroia.atk.domain.sample.port.out.SampleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @Mock
    private SampleRepository repository;

    @Mock
    private SamplePublisher publisher;

    @InjectMocks
    private SampleService service;

    @Test
    void should_saveThenPublishSample_when_created() {
        final Sample sample = service.create("first sample");

        assertThat(sample.id()).isNotNull();
        assertThat(sample.name()).isEqualTo("first sample");

        final InOrder inOrder = inOrder(repository, publisher);
        inOrder.verify(repository).save(sample);
        inOrder.verify(publisher).publish(sample);
    }
}
