package org.cavalodetroia.atk.componenttests.steps;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.google.protobuf.InvalidProtocolBufferException;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.cavalodetroia.contracts.template.v1.SampleCreated;
import org.cavalodetroia.atk.infra.kafka.SampleCreatedListener;
import org.jooq.DSLContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Drives the app from the outside: HTTP in, then checks the database, the topic, and the listener's log.
 */
public class SampleSteps {

    // Written out on purpose: the topic is part of the contract, so renaming it should break this test
    private static final String TOPIC = "atk.sample.created.v1";
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private final RestClient http;
    private final DSLContext dsl;
    private final String bootstrapServers;
    private final Logger listenerLogger = (Logger) LoggerFactory.getLogger(SampleCreatedListener.class);
    private final ListAppender<ILoggingEvent> listenerLogs = new ListAppender<>();

    private String name;
    private ResponseEntity<String> response;

    public SampleSteps(@LocalServerPort final int port,
                      final DSLContext dsl,
                      @Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers) {
        this.http = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                // A 4xx is something to assert on, not an exception
                .defaultStatusHandler(status -> true, (request, ignored) -> { })
                .build();
        this.dsl = dsl;
        this.bootstrapServers = bootstrapServers;
    }

    @Before
    public void captureListenerLogs() {
        listenerLogs.start();
        listenerLogger.addAppender(listenerLogs);
    }

    @After
    public void stopCapturingListenerLogs() {
        listenerLogger.detachAppender(listenerLogs);
    }

    @When("a sample {string} is created")
    public void aSampleIsCreated(final String name) {
        this.name = name;
        response = http.post()
                .uri("/api/v1/samples")
                .contentType(MediaType.APPLICATION_JSON)
                .body(JsonMapper.shared().writeValueAsString(Map.of("name", name)))
                .retrieve()
                .toEntity(String.class);
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int status) {
        assertThat(response.getStatusCode().value()).as(response.getBody()).isEqualTo(status);
    }

    @Then("the sample is stored in the database")
    public void theSampleIsStoredInTheDatabase() {
        assertThat(dsl.fetchValue("select name from sample where id = ?", UUID.fromString(sampleId()))).isEqualTo(name);
    }

    @Then("a SampleCreated message is published")
    public void aSampleCreatedMessageIsPublished() throws InvalidProtocolBufferException {
        final SampleCreated message = SampleCreated.parseFrom(awaitMessage(sampleId()).value());

        assertThat(message.getSampleId()).isEqualTo(sampleId());
        assertThat(message.getName()).isEqualTo(name);
    }

    @Then("the service consumes the message and logs its payload")
    public void theServiceConsumesTheMessageAndLogsItsPayload() {
        await().atMost(TIMEOUT).untilAsserted(() -> assertThat(loggedLines())
                .anySatisfy(line -> assertThat(line).contains(sampleId()).contains(name)));
    }

    private String sampleId() {
        return JsonPath.read(response.getBody(), "$.id");
    }

    private List<String> loggedLines() {
        // The listener appends on its own thread; the appender synchronizes on itself while doing so
        synchronized (listenerLogs) {
            return listenerLogs.list.stream().map(ILoggingEvent::getFormattedMessage).toList();
        }
    }

    /**
     * The first message with this key, reading every partition of the topic from the beginning.
     */
    private ConsumerRecord<String, byte[]> awaitMessage(final String key) {
        try (var consumer = new KafkaConsumer<>(
                Map.<String, Object>of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
                new StringDeserializer(),
                new ByteArrayDeserializer())) {
            final List<TopicPartition> partitions = consumer.partitionsFor(TOPIC, TIMEOUT).stream()
                    .map(partition -> new TopicPartition(TOPIC, partition.partition()))
                    .toList();
            consumer.assign(partitions);
            consumer.seekToBeginning(partitions);

            final Instant deadline = Instant.now().plus(TIMEOUT);
            while (Instant.now().isBefore(deadline)) {
                for (final ConsumerRecord<String, byte[]> record : consumer.poll(Duration.ofMillis(200))) {
                    if (key.equals(record.key())) {
                        return record;
                    }
                }
            }
        }
        throw new AssertionError("No message with key '" + key + "' on " + TOPIC + " within " + TIMEOUT);
    }
}
