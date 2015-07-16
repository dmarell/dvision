FROM java:8-jre
MAINTAINER daniel@marell.se
COPY dvision-server-${project.version}.jar /dvision-server.jar

VOLUME ["/logs", "/cam-motion-grabber"]

ENV DVISION_ENV prod
EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=${DVISION_ENV}", "-jar", "dvision-server.jar"]
