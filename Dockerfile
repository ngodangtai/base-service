ARG JAR_FILE
ARG JAR_DIRECTORY

# 1st-stage: build (compile, test)
# Docker image that build a Spring Boot application
FROM hub.company.com.vn/build/spring:2.5.14 AS MAVEN_BUILD

ARG POM_FILE=pom.xml
ARG SRC_DIR=src/

# Copy pom.xml & Dockerfile
COPY ${POM_FILE} .

#set time
USER root

ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Download& cache Maven dependencies
RUN mvn -B -s $HOME/.m2/settings.xml dependency:go-offline

# Copy source code
COPY ${SRC_DIR} ./${SRC_DIR}

# Show arch
RUN echo "Running on $(uname -m)"

# Build Spring Boot app& Docker image then push to Docker Registry
RUN mvn -e -B package

# 2nd-stage: copy file to nexus
FROM scratch AS ARTIFACT

ARG JAR_FILE=base-app.jar
ARG JAR_DIRECTORY=target

COPY --from=MAVEN_BUILD /home/jboss/build/${JAR_DIRECTORY}/*.jar .


# 3rd-stage: package
# Docker image that contains a Spring Boot application
FROM hub.company.com.vn/base/openjdk-8-rhel8-amd64:latest

ARG JAR_FILE=base-app.jar
ARG JAR_DIRECTORY=target

USER root

RUN mkdir -p /var/log/service-base
RUN mkdir -p /opt/company/service-base/cert

RUN chown -R $_USER:$_USER /var/log/service-base
RUN chown -R $_USER:$_USER /opt/company/service-base/cert

# Absolute path to the JAR file to be launched when a Docker container is started
ENV JAR_APP=${JAR_FILE}
	# Add Certificate for SSL
COPY private.keystore $JAR_PATH/cert/private.keystore
# Copy all the static contents to be included in the Docker image
COPY --chown=$_USER:$_USER --from=MAVEN_BUILD /home/jboss/build/${JAR_DIRECTORY}/*.jar $JAR_PATH/${JAR_FILE}

# Expose port
EXPOSE ${EXPOSE_PORTS}

# Show arch
RUN echo "Running on $(uname -m)"

ENTRYPOINT [ "sh", "-c", "cd $JAR_PATH && java $JAVA_OPTS -jar $JAR_APP" ]