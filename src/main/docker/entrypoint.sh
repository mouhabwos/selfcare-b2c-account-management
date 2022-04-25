#!/bin/sh

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}
exec java -XshowSettings:vm ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/${project.artifactId}.${project.packaging}" "$@"
