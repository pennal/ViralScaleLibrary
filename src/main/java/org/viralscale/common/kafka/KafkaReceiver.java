package org.viralscale.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KafkaReceiver {
    private static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);
    private final KafkaConsumer<String, byte[]> consumer;
    private final List<String> topics;

    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    private boolean shouldRun;

    public KafkaReceiver(List<KafkaTopic> topics, String clientID) {


        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
        properties.put("client.id", clientID);
        properties.put("zookeeper.session.timeout.ms", "6000");
        properties.put("zookeeper.sync.time.ms","2000");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("consumer.timeout.ms", "-1");
        properties.put("max.poll.records", "1");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "viralscale-" + clientID);

        consumer = new KafkaConsumer<>(properties);
        this.topics = topics.stream().map(t -> t.getTopic()).collect(Collectors.toList());

        consumer.subscribe(this.topics);

        this.shouldRun = true;
    }

    public void kill() {
        this.shouldRun = false;
    }

    public void acknowledge(ConsumerRecord<String, byte[]> record) {
        // Commit the message
        Map<TopicPartition, OffsetAndMetadata> commitMessage = new HashMap<>();

        commitMessage.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));

        consumer.commitSync(commitMessage);

        logger.info("Record with key " + record.key() + " committed");
    }

    public void runSingleWorker(Consumer<ConsumerRecord<String, byte[]>> callback) {

        Duration t = Duration.of(1000, ChronoUnit.MILLIS);
        while (shouldRun) {

            ConsumerRecords<String, byte[]> records = consumer.poll(t);

            records.forEach(record -> {
                // First ack the message
                acknowledge(record);
                // Apply the function chosen by the user
                callback.accept(record);
            });
        }
    }

    public void runSingleWorkerWithoutAck(Consumer<ConsumerRecord<String, byte[]>> callback) {

        Duration t = Duration.of(1000, ChronoUnit.MILLIS);
        while (true) {

            ConsumerRecords<String, byte[]> records = consumer.poll(t);

            records.forEach(record -> {
                // Apply the function chosen by the user
                callback.accept(record);
            });
        }
    }
}
