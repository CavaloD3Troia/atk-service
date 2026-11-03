package org.cavalodetroia.atk.infra.kafka;

import org.apache.kafka.common.errors.SerializationException;
import org.cavalodetroia.contracts.template.v1.SampleCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SampleCreatedSerdeTest {

    private static final String TOPIC = "atk.sample.created.v1";

    private final ProtobufSerializer<SampleCreated> serializer = new ProtobufSerializer<>();
    private final SampleCreatedDeserializer deserializer = new SampleCreatedDeserializer();

    @Test
    void should_roundTripMessage_when_serialized() {
        final SampleCreated message = SampleCreated.newBuilder()
                .setSampleId("0b5c3c38-4a8e-4f53-8c0e-5b0b8d8f2a11")
                .setName("first sample")
                .build();

        assertThat(deserializer.deserialize(TOPIC, serializer.serialize(TOPIC, message))).isEqualTo(message);
    }

    @Test
    void should_throwSerializationException_when_bytesAreNotProtobuf() {
        assertThatThrownBy(() -> deserializer.deserialize(TOPIC, new byte[]{(byte) 0xFF, 0x00}))
                .isInstanceOf(SerializationException.class)
                .hasMessageContaining(TOPIC);
    }
}
