FROM maven:3.9.14-amazoncorretto-25 AS builder

WORKDIR /source

ADD pom.xml /source

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

ADD . /source

RUN mvn -DskipTests package

# https://stackoverflow.com/questions/42208442/maven-docker-cache-dependencies

FROM amazoncorretto:25

WORKDIR /app

COPY --from=builder /source/target/c100k-server-0.1.2-jar-with-dependencies.jar server.jar

EXPOSE 22222

ENTRYPOINT ["java", "-Dsun.net.httpserver.nodelay=true", "-jar", "/app/server.jar"]
