FROM gradle:8.8-jdk AS build
WORKDIR /build/libs
COPY . /build/libs
RUN ./gradlew cleanBuild

FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY build/libs/Events-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8282
ENTRYPOINT ["java", "-jar", "/app.jar"]
