FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY *.jar /opt/app/app.jar
CMD ["java", "-Dserver.port=${PORT}", "-jar", "/opt/app/app.jar"]
EXPOSE 8080