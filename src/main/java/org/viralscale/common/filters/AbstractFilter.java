//package org.viralscale.common.filters;
//
//import org.viralscale.common.models.DataModel;
//import org.viralscale.common.kafka.*;
//
//import java.io.IOException;
//import java.util.List;
//
//
//public abstract class AbstractFilter {
//
//    public AbstractFilter() {
//        KafkaTopic
//    }
//
//
//
//    public void execute() {
//        KafkaReceiver c = new KafkaReceiver(KafkaTopic.CRAWLED_POST.asList(), "FFFF");
//        c.runSingleWorker(record -> {
//            byte[] body = record.value();
//
//            try {
//                DataModel data = DataModel.fromJson(body);
//
//                List<String> matches = RegexUtils.execute(regex, data.getOutboundUrl(), true);
//
//                if (!matches.isEmpty()) {
//
//
//                    System.err.println(data.toJson());
//                }
//
//
//
//
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        });
//    }
//
//
//}
