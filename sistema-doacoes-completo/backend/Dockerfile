FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Create uploads directory and expose as volume
RUN mkdir -p /app/uploads
VOLUME ["/app/uploads"]

ENTRYPOINT ["java", "-jar", "app.jar"]
