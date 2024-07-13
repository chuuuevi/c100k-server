FROM maven:3.9.8-amazoncorretto-21-al2023 as builder

WORKDIR /source

ADD pom.xml /source

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

ADD . /source

RUN mvn -DskipTests package

# https://stackoverflow.com/questions/42208442/maven-docker-cache-dependencies

FROM amazoncorretto:21-al2023

WORKDIR /app

COPY --from=builder /source/target/java-http-server-0.1.0-jar-with-dependencies.jar server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/server.jar"]
