FROM docker.1ms.run/maven:3.9.8-amazoncorretto-21-al2023 AS builder

WORKDIR /source

ADD pom.xml /source

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

ADD . /source

RUN mvn -DskipTests package

# https://stackoverflow.com/questions/42208442/maven-docker-cache-dependencies

FROM docker.1ms.run/amazoncorretto:21-al2023

WORKDIR /app

COPY --from=builder /source/target/c100k-server-0.1.0-jar-with-dependencies.jar server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dsun.net.httpserver.nodelay=true", "-jar", "/app/server.jar"]
