package org.viralscale.common.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.viralscale.common.utils.config.ConfigReader;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class KafkaMessenger {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessenger.class);

    private KafkaProducer<String, byte[]> producer;

    public static final String KAFKA_SERVER_URL = "localhost";
    public static final Integer KAFKA_SERVER_PORT = 9092;
    public static final String CLIENT_ID = "DEFAULT_PRODUCER__" + UUID.randomUUID().toString();

    private static KafkaMessenger instance;

    private KafkaMessenger() {
        // Read out the config file

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
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        producer = new KafkaProducer<>(properties);

        logger.debug("Kafka initialized with: " + properties.toString());
    }


    public static KafkaMessenger getInstance() {
        if (instance == null) {
            instance = new KafkaMessenger();
        }
        return instance;
    }


    public void sendMessage(KafkaTopic topic, String key, byte[] value, Callback callback) {
        this.producer.send(new ProducerRecord<>(topic.getTopic(), key, value), callback);
    }
}
