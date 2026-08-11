FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/api-consulta-cnj-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/api-consulta-cnj.jar"]




