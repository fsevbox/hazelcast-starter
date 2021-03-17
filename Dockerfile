FROM adoptopenjdk/openjdk11:jre-11.0.10_9

ARG JAR_FILE=target/hazelcast-starter.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=k8s"]