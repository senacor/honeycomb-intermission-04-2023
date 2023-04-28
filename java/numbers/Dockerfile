FROM gradle:8.1.1-jdk17 AS build
RUN mkdir -p /src
WORKDIR /src
COPY --chown=gradle:gradle . /src/

RUN gradle bootJar --no-daemon

FROM openjdk:17-alpine

RUN mkdir -p /app
COPY --from=build /src/build/libs/numbers.jar /app/
COPY --from=build /src/build/opentelemetry-javaagent-1.25.0.jar /app/opentelemetry-javaagent.jar
WORKDIR /app

EXPOSE 8080
CMD [ "java", "-javaagent:opentelemetry-javaagent.jar", "-jar", "numbers.jar", "-Dotel.instrumentation.jdbc.enable=true", "-Dotel.instrumentation.jdbc-datasource=true"]
