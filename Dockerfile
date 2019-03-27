FROM openjdk:8-jdk-alpine as builder

ARG WORKDIR="/app"

WORKDIR ${WORKDIR}

COPY build.gradle gradlew settings.gradle ${WORKDIR}/
COPY gradle ${WORKDIR}/gradle
# The task `gradle dependencies` will list the dependencies and download them as a side-effect.
RUN ./gradlew --no-daemon clean dependencies --configuration runtime
COPY . ${WORKDIR}
RUN ./gradlew --no-daemon bootJar


FROM openjdk:8-jdk-alpine

ARG WORKDIR="/app"

WORKDIR ${WORKDIR}

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

ENV JAVA_TOOL_OPTIONS "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2"

COPY --from=builder ${WORKDIR}/build/libs/*.jar ${WORKDIR}/