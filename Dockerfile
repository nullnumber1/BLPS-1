FROM openjdk:18

WORKDIR /usr/local/bin/
COPY build/libs/Lab1-1.0.jar ./blps.jar

CMD ["java", "-jar", "blps.jar"]

LABEL maintainer="nullnumber1"