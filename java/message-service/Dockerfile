FROM gradle:8.1.0-jdk17 AS build
WORKDIR /home/gradle/
RUN mkdir ./libs
COPY --chown=gradle:gradle *.gradle ./
COPY --chown=gradle:gradle ./src ./src
RUN gradle bootJar --no-daemon

FROM openjdk:17-jdk-alpine
VOLUME /tmp
ENV JAVA_AGENT=otel-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:${JAVA_AGENT}
COPY --from=build /home/gradle/agent/${JAVA_AGENT} ./${JAVA_AGENT}
COPY --from=build /home/gradle/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
