package org.viralscale.common.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaMessenger {
    private KafkaProducer<String, byte[]> producer;

    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final String CLIENT_ID = "FilterProducer";

    private static KafkaMessenger instance;

    private KafkaMessenger() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
        properties.put("client.id", CLIENT_ID);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        producer = new KafkaProducer<>(properties);
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
