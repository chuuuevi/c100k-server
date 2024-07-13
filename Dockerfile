FROM maven:3.9.8-amazoncorretto-21-al2023 as builder

WORKDIR /source

ADD pom.xml /source

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

ADD . /source

RUN mvn -DskipTests package

# https://stackoverflow.com/questions/42208442/maven-docker-cache-dependencies

FROM amazoncorretto:22-al2023

WORKDIR /app

COPY --from=builder /source/target/*.jar server.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar /app/server.jar
