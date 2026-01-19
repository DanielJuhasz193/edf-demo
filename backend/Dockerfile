FROM amazoncorretto:25-alpine-jdk AS build

RUN mkdir /source
WORKDIR /source

COPY . /source

RUN ./gradlew --no-daemon clean build -x test --info && \
    cp build/libs/demo-*-SNAPSHOT.jar app.jar

FROM amazoncorretto:25-alpine-jdk

COPY --from=build /source/app.jar /

ENTRYPOINT ["java", "-jar", "app.jar"]
