package org.cavalodetroia.atk.infra.kafka;

import org.cavalodetroia.contracts.template.v1.SampleCreated;
import org.cavalodetroia.atk.domain.sample.Sample;
import org.cavalodetroia.atk.domain.sample.port.out.SamplePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SampleCreatedPublisher implements SamplePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleCreatedPublisher.class);

    private final KafkaTemplate<String, SampleCreated> template;
    private final SampleCreatedConverter converter;
    private final String topic;

    public SampleCreatedPublisher(final KafkaTemplate<String, SampleCreated> template,
                                 final SampleCreatedConverter converter,
                                 @Value("${atk.kafka.topic}") final String topic) {
        this.template = template;
        this.converter = converter;
        this.topic = topic;
    }

    @Override
    public void publish(final Sample sample) {
        final String key = sample.id().toString();

        // Never block the request on the broker: the outcome is logged when it arrives
        template.send(topic, key, converter.toMessage(sample)).whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("operation=publish, message=Send failed topic='{}' key='{}'", topic, key, ex);
            }
        });
    }
}
