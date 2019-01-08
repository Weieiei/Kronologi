FROM maven:3-jdk-8

WORKDIR /home/appointmentscheduler/

COPY . .
COPY src/main/resources/application.docker.properties src/main/resources/application.properties

RUN mvn package --quiet

WORKDIR /home/appointmentscheduler/target/

EXPOSE 3000

CMD ["java", "-jar", "appointment-scheduler-server.jar"]
