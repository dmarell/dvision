FROM java:8-jre
MAINTAINER daniel@marell.se
COPY dvision-server-${project.version}.jar /dvision-server.jar
EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "dvision-server.jar" ]
