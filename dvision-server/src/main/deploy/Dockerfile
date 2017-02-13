FROM maven:3-jdk-8
ENV BUILD_HOME=/dvision-server/target/

RUN mkdir -p $BUILD_HOME
WORKDIR $BUILD_HOME

# add source
ADD . $BUILD_HOME
ARG BUILD_VERSION

RUN mvn versions:set -DnewVersion=${BUILD_VERSION} -B
RUN mvn clean package -B