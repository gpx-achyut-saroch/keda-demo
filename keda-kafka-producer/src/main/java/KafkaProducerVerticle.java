import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaProducerVerticle extends AbstractVerticle {

    String topicName = "autoscaling-preprod";

    String bootstrapServer = "172.31.0.120:9092";

    Logger logger = LogManager.getLogger(KafkaProducerVerticle.class.getName());
    @Override
    public void start()
    {
        System.out.println("Kafka Verticle Started");
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", bootstrapServer);
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks","1");

        for(String key : config.keySet())
            System.out.println("key : " + key + " value : "+config.get(key));

//        HttpServer httpServer = vertx.createHttpServer();
//        Router router = Router.router(vertx);
        KafkaProducer<String,String> producer = KafkaProducer.create(vertx,config);
//
//        router.get("/produce").handler( ctx -> {


            AtomicInteger z = new AtomicInteger();
            vertx.setPeriodic(1000, x -> {

                Integer w = z.get()%4;

                KafkaProducerRecord<String, String> record =
                        KafkaProducerRecord.create(topicName, w.toString() ,"message_" + z.getAndIncrement(),w);

                producer.send(record).onSuccess(recordMetadata ->
                        System.out.println(
                                "Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
                                        ", partition=" + recordMetadata.getPartition() +
                                        ", offset=" + recordMetadata.getOffset()
                        ));
            });


//
//        });
//
//        httpServer.requestHandler(router).listen(8080).onSuccess(x -> System.out.println("server started at port 8080"))
//                .onFailure(fail -> System.out.println("failed to start server at port 8080"));









    }

}
