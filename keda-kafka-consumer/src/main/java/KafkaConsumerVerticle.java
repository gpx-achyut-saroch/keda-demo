import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerVerticle extends AbstractVerticle {

    String topicName = "autoscaling-preprod";

    String bootstrapServer = "172.31.0.120:9092";

    Logger logger = LogManager.getLogger(KafkaConsumerVerticle.class.getName());
    @Override
    public void start()
    {
        System.out.println("Kafka Verticle Started");
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", bootstrapServer);
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "autoscaling");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "true");
        config.put("max.poll.records","3");

        for(String key : config.keySet())
            System.out.println("key : " + key + " value : "+config.get(key));

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);



        consumer.subscribe(topicName)
                .onSuccess(v -> {

                    System.out.println("subscribed");
                    vertx.setPeriodic(10000, timerId ->
                            {
                                System.out.println("Inside consumer period");
                                consumer
                                        .poll(Duration.ofMillis(1000))
                                        .onSuccess(records -> {
                                            System.out.println("records size : " + records.size());
                                            for (int i = 0; i < records.size(); i++) {
                                                KafkaConsumerRecord<String, String> record = records.recordAt(i);
                                                System.out.println("key=" + record.key() + ",value=" + record.value() +
                                                        ",partition=" + record.partition() + ",offset=" + record.offset());
                                            }
                                        })
                                        .onFailure(cause -> {
                                            System.out.println("Something went wrong when polling " + cause.toString());
                                            cause.printStackTrace();

                                            // Stop polling if something went wrong
                                            vertx.cancelTimer(timerId);
                                        });
                            }
                    );
                })
                .onFailure(System.out::println);



    }

}
