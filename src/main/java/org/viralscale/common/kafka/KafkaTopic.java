package org.viralscale.common.kafka;

import java.util.List;

public class KafkaTopic {
    public static final KafkaTopic CRAWLED_POST = new KafkaTopic("crawled_post");
    public static final KafkaTopic FILTERED_POST = new KafkaTopic("filtered_post");
    public static final KafkaTopic PLATEAUD_POST = new KafkaTopic("plateaud_post");
    public static final KafkaTopic SCALE_DOWN = new KafkaTopic("scale_down");
    public static final KafkaTopic SCALE_UP = new KafkaTopic("scale_up");

    public static final List<KafkaTopic> ALL_TOPICS = List.of(
            CRAWLED_POST,
            FILTERED_POST,
            PLATEAUD_POST,
            SCALE_DOWN,
            SCALE_UP
    );


    private String topic;

    private KafkaTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return this.topic;
    }

    public boolean isEqualTo(String topic) {
        return this.getTopic().equals(topic);
    }

    public List<KafkaTopic> asList() {
        return List.of(this);
    }
}
