FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh jhipster
WORKDIR /home/jhipster

ADD entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh && chown jhipster:jhipster entrypoint.sh

RUN mkdir esapi
#WORKDIR /home/jhipster/esapi


ADD ESAPI.properties /esapi/ESAPI.properties
RUN chmod 755 /esapi/ESAPI.properties && chown jhipster:jhipster /esapi/ESAPI.properties

USER jhipster

ADD ${project.artifactId}-${project.version}.${project.packaging} ${project.artifactId}.${project.packaging}

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE ${server.port.docker.expose}
