package org.cavalodetroia.atk.infra.kafka;

import com.google.protobuf.TextFormat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.cavalodetroia.contracts.template.v1.SampleCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Reads back what {@link SampleCreatedPublisher} sends, and only logs it.
 */
@Component
public class SampleCreatedListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleCreatedListener.class);

    @KafkaListener(topics = "${atk.kafka.topic}")
    public void onSampleCreated(final ConsumerRecord<String, SampleCreated> record) {
        LOGGER.info("operation=consume, message=Sample created key='{}' partition={} offset={} payload={{}}",
                record.key(), record.partition(), record.offset(),
                TextFormat.printer().emittingSingleLine(true).printToString(record.value()));
    }
}
