FROM maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml /app

RUN mvn dependency:go-offline

COPY ./src /app/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine-3.22

COPY --from=builder /app/target/*.jar app_clientes.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app_clientes.jar"]
