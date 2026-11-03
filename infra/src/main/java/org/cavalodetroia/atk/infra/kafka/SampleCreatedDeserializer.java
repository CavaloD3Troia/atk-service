package org.cavalodetroia.atk.infra.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.cavalodetroia.contracts.template.v1.SampleCreated;

/**
 * Wrapped in {@code ErrorHandlingDeserializer} (see {@code application.yml}), so a message that is not an
 * {@link SampleCreated} is logged and skipped instead of stopping the consumer.
 */
public class SampleCreatedDeserializer implements Deserializer<SampleCreated> {

    @Override
    public SampleCreated deserialize(final String topic, final byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return SampleCreated.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new SerializationException("Cannot parse a SampleCreated from topic " + topic, e);
        }
    }
}
