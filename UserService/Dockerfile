FROM openjdk:22-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "application.jar"]