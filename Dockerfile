FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY /build/libs/*-SNAPSHOT.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
EXPOSE 8080