FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiază JAR-ul deja generat
COPY target/*.jar app.jar

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "app.jar"]