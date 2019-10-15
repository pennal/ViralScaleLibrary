//package org.viralscale.common;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.apache.kafka.clients.producer.Callback;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.viralscale.common.kafka.KafkaMessenger;
//import org.viralscale.common.kafka.KafkaTopic;
//import org.viralscale.common.models.DataModel;
//import org.viralscale.common.utils.config.ConfigReader;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//public class Main {
//
//    private static final Logger logger = LoggerFactory.getLogger(Main.class);
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        //ConfigReader r = new ConfigReader("kafka.properties");
//        //r.readFile();
//        //
//        //
//        //KafkaMessenger.getInstance().sendMessage(KafkaTopic.CRAWLED_POST, "helloWorld", "AA".getBytes(), new Callback() {
//        //    @Override
//        //    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//        //        System.out.println(recordMetadata);
//        //    }
//        //});
//
//
//    }
//}
