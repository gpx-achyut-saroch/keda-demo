FROM openjdk:17
ADD target/original-keda-kafka-consumer.jar keda-kafka-consumer.jar
ENTRYPOINT ["java", "-jar","keda-kafka-consumer.jar"]
EXPOSE 8080