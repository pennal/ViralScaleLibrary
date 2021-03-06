package org.viralscale.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.viralscale.common.utils.config.ConfigReader;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KafkaReceiver implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);
    private final KafkaConsumer<String, byte[]> consumer;
    private final List<String> topics;

    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final String CLIENT_ID = "DEFAULT_RECEIVER__" + UUID.randomUUID().toString();

    private final AtomicBoolean running;
    private Thread worker;
    private Consumer<ConsumerRecord<String, byte[]>> callback;

    public KafkaReceiver(List<KafkaTopic> topics, Consumer<ConsumerRecord<String, byte[]>> messageCallback) {
        Properties initProperties = new Properties();

        try {
            ConfigReader configReader = new ConfigReader("/config/kafka.properties");
            configReader.readFile();

            // Parse props
            String kafkaServerUrl = configReader.getValue("kafka.server.url", KAFKA_SERVER_URL);
            Integer kafkaServerPort = configReader.getValueAsInteger("kafka.server.port", KAFKA_SERVER_PORT);
            String kafkaClientId = configReader.getValue("kafka.client.id", CLIENT_ID);

            // The client id must be unique. What we do is simply add a UUID
            kafkaClientId += "__" + UUID.randomUUID().toString();

            initProperties.putAll(Map.of(
                    "server_url", kafkaServerUrl,
                    "server_port", kafkaServerPort,
                    "server_client_id", kafkaClientId
            ));

        } catch (IOException e) {
            // If it all fails, go for defaults
            initProperties.putAll(Map.of(
                    "server_url", KAFKA_SERVER_URL,
                    "server_port", KAFKA_SERVER_PORT,
                    "server_client_id", CLIENT_ID
            ));
            logger.warn("Initializing Kafka with DEFAULTS!");
        }



        Properties properties = new Properties();
        properties.put("bootstrap.servers", initProperties.get("server_url") + ":" + initProperties.get("server_port"));
        properties.put("client.id", initProperties.get("server_client_id"));
        //properties.put("zookeeper.session.timeout.ms", "6000");
        //properties.put("zookeeper.sync.time.ms","2000");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("consumer.timeout.ms", "-1");
        properties.put("max.poll.records", "1");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Group id is composed by viralscale-<CLIENT_ID>
        properties.put("group.id", "viralscale-" + initProperties.get("server_client_id"));

        logger.debug("Kafka initialized with: " + properties.toString());

        consumer = new KafkaConsumer<>(properties);
        this.topics = topics.stream().map(t -> t.getTopic()).collect(Collectors.toList());
        consumer.subscribe(this.topics);

        this.callback = messageCallback;

        // Create the needed thread data
        this.running = new AtomicBoolean(false);
        this.worker = new Thread(this);

    }

    public void start() {
        logger.info("Starting receiver with topics [" + String.join(", ", this.topics) + "]");
        this.worker.start();
    }

    public void stop() {
        logger.info("Stopping receiver thread");
        this.running.set(false);
    }

    public void interrupt() {
        logger.info("Interrupt called");
        this.stop();
        this.worker.interrupt();
    }


    @Override
    public void run() {
        this.running.set(true);
        Duration t = Duration.of(1000, ChronoUnit.MILLIS);
        while (running.get()) {
            ConsumerRecords<String, byte[]> records = consumer.poll(t);

            records.forEach(record -> {
                // First ack the message
                acknowledge(record);
                // Apply the function chosen by the user
                callback.accept(record);
            });
        }
        this.worker = null;
        this.consumer.close();
        logger.warn("Exiting receiver thread");
    }

    public void acknowledge(ConsumerRecord<String, byte[]> record) {
        // Commit the message
        Map<TopicPartition, OffsetAndMetadata> commitMessage = new HashMap<>();

        commitMessage.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));

        consumer.commitSync(commitMessage);

        logger.debug("Record with key " + record.key() + " committed");
    }
}
