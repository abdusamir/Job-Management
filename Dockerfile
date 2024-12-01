FROM maven:3.6.3-openjdk-17-slim AS MAVEN_BUILD

MAINTAINER Abdelrahman Ahmed

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:19-jdk-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/JobManager.jar /app/

ENTRYPOINT ["java", "-jar", "JobManager.jar"]