FROM openjdk:17-alpine

COPY ./target/DecisionEngine*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]