FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p /app/images
ENTRYPOINT ["java","-jar","/app.jar"]test