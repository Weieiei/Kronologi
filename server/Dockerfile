FROM maven:3-jdk-8

WORKDIR /home/appointmentscheduler/

COPY . .
COPY src/main/resources/application.docker.properties src/main/resources/application.properties

# Skipping tests because it throws an ApplicationContext error, caused by a lack of a connection to the database
RUN mvn package --quiet -DskipTests

WORKDIR /home/appointmentscheduler/target/

EXPOSE 3000

CMD ["java", "-jar", "appointment-scheduler-server.jar"]
