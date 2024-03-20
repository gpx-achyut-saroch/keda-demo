FROM openjdk:17
ADD target/keda-kafka-producer.jar keda-kafka-producer.jar
ENTRYPOINT ["java", "-jar","keda-kafka-producer.jar"]
EXPOSE 8080